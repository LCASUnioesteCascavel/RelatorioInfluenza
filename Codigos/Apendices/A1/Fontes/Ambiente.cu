#include "Ambiente.h"
#include "Fontes/Macros/MacrosGerais.h"
#include "Fontes/Macros/MacrosSO.h"
#include "Fontes/Uteis/RandPerc.h"

using std::cerr;
using std::endl;
using std::fstream;
using std::ifstream;
using std::make_tuple;
using std::numeric_limits;
using std::string;
using std::streamsize;
using thrust::raw_pointer_cast;
using std::tie;

/*
  Construtor da classe Ambiente. 

  O caminho para a pasta de entrada contendo os arquivos da simulacao 
  Monte Carlo e passado como argumento ao metodo, por meio do parametro 
  "entradaMC". O valor desta variavel segue o padrao "Entradas/MonteCarlo_{1}/", 
  em que "{1}" designa o id numerico da simulacao Monte Carlo. 
  
  Este metodo realiza a leitura dos arquivos contidos na pasta "Ambiente", 
  especificamente os arquivos "0-AMB.csv", "1-MOV.csv" e 
  DistribuicaoHumanos.csv". Os metodos "lerVetoresAmbientais", 
  "lerVetoresControles" e "lerArquivoDistribuicaoHumanos" realizam a leitura 
  dos respectivos dados as respectivas variaveis.

  Apis a leitura dos arquivos os dados obtidos sao copiados a GPU pelo 
  metodo "toGPU". 
*/
Ambiente::Ambiente(string entradaMC) {
  this->entradaMC = entradaMC;
  lerVetoresAmbientais();
  lerVetoresControles();
  lerArquivoDistribuicaoHumanos();
  toGPU();
}

/*
  Metodo responsavel pela obtencao do consumo de memiria da classe Ambiente. 
*/
int Ambiente::getMemoriaGPU() {
  int totMem = 0;
  totMem += (sizeNLotes * sizeof(int));
  totMem += (sizeIndQuadras * sizeof(int));
  totMem += (sizeIndViz * sizeof(int));
  totMem += (sizeViz * sizeof(Vizinhanca));
  totMem += (sizeIndPos * sizeof(int));
  totMem += (sizePos * sizeof(Posicao));
  totMem += (sizeDistHumanos * sizeof(Caso));
  totMem += (sizeFEVac * sizeof(int));
  totMem += (sizePerVac * sizeof(int));
  totMem += (sizeCicVac * sizeof(int));
  totMem += (sizeComp * sizeof(double));
  totMem += (sizeQuaren * sizeof(double));
  return totMem;
}

/*
  Destrutor da classe Ambiente. 

  Neste metodo sao desalocados da memiria principal e da GPU 
  os dados necessarios a classe Ambiente. 
*/
Ambiente::~Ambiente() {
  delete[](nLotes); delete[](indQuadras); delete[](indViz); delete[](viz);
  delete[](indPos); delete[](pos); 
  delete[](fEVac); delete[](perVac); delete[](cicVac);
  delete[](distHumanos); delete[](comp); delete[](quaren);

  delete(nLotesDev); delete(indQuadrasDev);
  delete(indVizDev); delete(vizDev); delete(indPosDev);
  delete(posDev);
  delete(fEVacDev); delete(perVacDev); delete(cicVacDev);
  delete(distHumanosDev); delete(compDev); delete(quarenDev);
}

/*
  Metodo responsavel pela cipia dos dados da classe Ambiente a GPU. 

  Primeiramente sao instanciadas classes "DVector", que armazenam seus  
  dados na memiria da GPU. No construtor desta classe sao passados dois 
  ponteiros, que indicam o inicio e final dos dados em CPU que devem ser 
  copiados para a GPU. 

  Por fim sao obtidos ponteiros diretos aos dados armazenados pelas classes 
  "DVector" por meio da funcao "raw_pointer_cast", com o objetivo de facilitar 
  o acesso aos dados. 
*/
void Ambiente::toGPU() {
  nLotesDev = new DVector<int>(nLotes, nLotes + sizeNLotes);
  indQuadrasDev = new DVector<int>(indQuadras, indQuadras + sizeIndQuadras);
  indVizDev = new DVector<int>(indViz, indViz + sizeIndViz);
  vizDev = new DVector<Vizinhanca>(viz, viz + sizeViz);
  indPosDev = new DVector<int>(indPos, indPos + sizeIndPos);
  posDev = new DVector<Posicao>(pos, pos + sizePos);
  fEVacDev = new DVector<int>(fEVac, fEVac + sizeFEVac);
  perVacDev = new DVector<int>(perVac, perVac + sizePerVac);
  cicVacDev = new DVector<int>(cicVac, cicVac + sizeCicVac);
  distHumanosDev = new DVector<Caso>(distHumanos, distHumanos + sizeDistHumanos);
  compDev = new DVector<double>(comp, comp + sizeComp);
  quarenDev = new DVector<double>(quaren, quaren + sizeQuaren);

  PnLotesDev = raw_pointer_cast(nLotesDev->data());
  PposDev = raw_pointer_cast(posDev->data());
  PindQuadrasDev = raw_pointer_cast(indQuadrasDev->data());
  PindVizDev = raw_pointer_cast(indVizDev->data());
  PvizDev = raw_pointer_cast(vizDev->data());
  PindPosDev = raw_pointer_cast(indPosDev->data());
  PfEVacDev = raw_pointer_cast(fEVacDev->data());
  PperVacDev = raw_pointer_cast(perVacDev->data());
  PcicVacDev = raw_pointer_cast(cicVacDev->data());
  PdistHumanosDev = raw_pointer_cast(distHumanosDev->data());
  PcompDev = raw_pointer_cast(compDev->data());
  PquarenDev = raw_pointer_cast(quarenDev->data());
}

/*
  Metodo responsavel pela leitura do arquivo "Ambiente/0-AMB.csv". 

  Cada linha do arquivo "Ambiente/0-AMB.csv" corresponde a um vetor de dados 
  especifico, que e necessario a simulacao (desconsiderando linhas em branco ou 
  com comentarios). Os dados neste arquivo sao armazenados da seguinte maneira: 

  Linha 1: Vetor com informacoes sobre quadras e lotes; 
  Linha 2: Vetor com informacoes sobre vizinhancas de Moore; 
  Linha 3: Vetor com informacoes sobre as posicoes do ambiente; 

  Os metodos "lerQuadrasLotes", "lerVizinhancas" e "lerPosicoes" sao 
  responsaveis pela leitura dos dados correspondentes, na ordem que foram 
  apresentadas anteriormente. Efetivamente, cada metodo realiza a leitura de 
  uma linha de dados do arquivo. 
*/
void Ambiente::lerVetoresAmbientais() {
  string entrada = entradaMC;
  entrada += string("Ambiente");
  entrada += SEP;
  entrada += string("0-AMB.csv");

  arquivo.open(entrada);

  if (not arquivo.is_open()) {
    cerr << "Arquivo: ";
    cerr << entrada;
    cerr << " nao foi aberto!" << endl;
    exit(1);
  }

  arquivo.ignore(sMax, '\n');
  lerQuadrasLotes();
  arquivo.ignore(sMax, '\n');
  
  arquivo.ignore(sMax, '\n');
  lerVizinhancas();
  arquivo.ignore(sMax, '\n');
  
  arquivo.ignore(sMax, '\n');
  lerPosicoes();
  arquivo.ignore(sMax, '\n');

  arquivo.close();
}

/*
  Metodo responsavel pela leitura de um vetor de dados com "n" elementos. 
*/
int *Ambiente::lerVetor(int n) {
  int *vec = new int[n]();
  for (int i = 0; i < n; ++i) {
    arquivo >> vec[i];
    arquivo.get();
  }
  return vec;
}

/*
  Metodo responsavel pela leitura do vetor das quadras e lotes. 

  Neste metodo sao lidos dados para tres variaveis: 

  "nQuadras": Esta variavel armazena a quantidade de quadras presentes no 
              ambiente, incluindo a quadra "0" correspondente as ruas. 
  "sizeNLotes": Esta variavel armazena a quantidade de lotes que cada quadra
                contem. Por exemplo, "sizeNLotes[0]" contem a quantidade de 
                lotes da quadra "0", ou seja, a quantidade de ruas; 
                "sizeNLotes[10]" contem a quantidade de lotes da quadra "10". 
  "indQuadras": Esta variavel armazena os indices para as quadras. E bastante 
                utilizada para indexar as outras estruturas do ambiente. 
                Cada quadra conta com dois valores, que correspondem aos indices 
                iniciais e finais. Desta forma, o id numerico da quadra e 
                multiplicado por 2 quando do uso desta estrutura. Por exemplo, 
                "indQuadras[2 * 10]" armazena o indice inicial para os dados 
                correspondentes a quadra "10". "indQuadras[2 * 5 + 1]" 
                armazena o indice final para os dados correspondentes 
                a quadra "5". 
*/
void Ambiente::lerQuadrasLotes() {
  arquivo >> nQuadras;
  arquivo.get();

  sizeNLotes = nQuadras;
  nLotes = lerVetor(sizeNLotes);

  sizeIndQuadras = nQuadras * 2;
  indQuadras = lerVetor(sizeIndQuadras);
}

/*
  Metodo responsavel pela leitura do vetor das vizinhancas de Moore. 

  Neste metodo sao lidos dados para duas variaveis:

  "indViz": Esta variavel armazena os indices para as vizinhancas. Este indice 
            e utilizado para indexar a variavel "viz" empregando ids de 
            quadra e lote. Desta forma, e possivel obter as vizinhancas de 
            Moore de um particular lote de uma determinada quadra. Para indexar 
            esta variavel e utilizada a variavel "indQuadras". Por exemplo, 
            "indViz[indQuadras[2 * 10] + 5]" armazena o indice inicial 
            para os dados correspondentes as vizinhancas de Moore do lote "5" 
            da quadra "10". "indViz[indQuadras[2 * 7] + 3 + 1]" armazena o 
            indice final para os dados correspondentes as vizinhancas de Moore 
            do lote "3" da quadra "7". 
  "viz": Esta variavel armazena todas as vizinhancas de Moore presentes no 
         ambiente. E indexada pela variavel "indViz". Por exemplo, 
         "viz[indViz[indQuadras[2 * 10] + 5]]" armazena a primeira 
         vizinhanca do lote "5" da quadra "10". 
         "viz[indViz[indQuadras[2 * 10] + 5] + 1]" armazena a segunda 
         vizinhanca do lote "5" da quadra "10". 

*/
void Ambiente::lerVizinhancas() {
  sizeIndViz = indQuadras[nQuadras * 2 - 1] + 1;
  indViz = lerVetor(sizeIndViz);

  sizeViz = indViz[indQuadras[nQuadras * 2 - 1]];

  viz = new Vizinhanca[sizeViz];
  for (int i = 0; i < sizeViz; ++i) {
    arquivo >> viz[i].xOrigem; arquivo.get();
    arquivo >> viz[i].yOrigem; arquivo.get();
    arquivo >> viz[i].xDestino; arquivo.get();
    arquivo >> viz[i].yDestino; arquivo.get();
    arquivo >> viz[i].loteDestino; arquivo.get();
    arquivo >> viz[i].quadraDestino; arquivo.get();
  }
}

/*
  Metodo responsavel pela leitura do vetor de posicoes do ambiente. 

  Neste metodo sao lidos dados para tres variaveis:

  "indPos": Esta variavel armazena os indices para as posicoes. E utilizada para 
            indexar a variavel "pos" empregando ids de quadra e lote. Desta 
            forma e possivel obter todas as posicoes de um particular lote de 
            uma determinada quadra. Por exemplo, 
            "indPos[indQuadras[2 * 10] + 5]" armazena o indice da primeira 
            posicao do lote "5" da quadra "10". 
            "indPos[indQuadras[2 * 10] + 5] + 9" armazena o indice da decima 
            posicao do lote "5" da quadra "10". 
  "pos": Esta variavel armazena todas as posicoes presentes no ambiente. E 
         indexada pela variavel "indPos". Por exemplo, 
         "pos[indPos[indQuadras[2 * 10] + 5]]" armazena a primeira posicao 
         do lote "5" da quadra "10". 
         "pos[indPos[indQuadras[2 * 10] + 5] + 9]" armazena a decima posicao 
         do lote "5" da quadra "10". 
*/
void Ambiente::lerPosicoes() {
  sizeIndPos = indQuadras[nQuadras * 2 - 1] + 1;
  indPos = lerVetor(sizeIndPos);

  sizePos = indPos[indQuadras[nQuadras * 2 - 1]];

  pos = new Posicao[sizePos];
  for (int i = 0; i < sizePos; ++i) {
    arquivo >> pos[i].x; arquivo.get();
    arquivo >> pos[i].y; arquivo.get();
    arquivo >> pos[i].lote; arquivo.get();
    arquivo >> pos[i].quadra; arquivo.get();
  }
}

/*
  Metodo responsavel pela leitura de um vetor de dados relacionados ao controle. 
  
  O metodo retorna o tamanho e o vetor de dados lidos do arquivo. 
*/
std::tuple<int, int *> Ambiente::lerControle() {
  int size;
  arquivo.ignore(sMax, '\n');
  arquivo >> size;
  arquivo.get();
  int *vec = lerVetor(size);
  arquivo.ignore(sMax, '\n');
  return make_tuple(size, vec);
}

/*
  Metodo responsavel pela leitura do arquivo "Ambiente/2-CON.csv". 

  Cada linha do arquivo "Ambiente/2-CON.csv" corresponde a um vetor de dados 
  especifico, que e necessario a simulacao (desconsiderando linhas em branco ou 
  com comentarios). Os dados neste arquivo sao armazenados da seguinte maneira: 

  Linha 1: Vetor com informacoes sobre as faixas etarias vacinadas; 
  Linha 2: Vetor com informacoes sobre os ciclos de vacinacao; 
  Linha 3: Vetor com informacoes sobre complemento dos casos normalizados; 
  Linha 4: Vetor com informacoes sobre quarentena. 

  O metodo "lerControle" e responsavel pela leitura dos dados correspondentes, 
  na ordem que foram apresentadas anteriormente. Efetivamente, cada chamada 
  deste metodo realiza a leitura de uma linha de dados do arquivo. Por fim, 
  os vetores de complemento e quarentena sao lidos. 
*/
void Ambiente::lerVetoresControles() {
  string entrada = entradaMC;
  entrada += string("Ambiente");
  entrada += SEP;
  entrada += string("1-CON.csv");

  arquivo.open(entrada);
  if (not arquivo.is_open()) {
    cerr << "Arquivo: ";
    cerr << entrada;
    cerr << " nao foi aberto!" << endl;
    exit(1);
  }
  
  tie(sizeFEVac, fEVac) = lerControle();

  sizePerVac = 2;
  perVac = new int[sizePerVac]();
  perVac[0] = 30; perVac[1] = 0; 

  tie(sizeCicVac, cicVac) = lerControle();

  arquivo.ignore(sMax, '\n');

  arquivo >> sizeComp;
  arquivo.get();
  comp = new double[sizeComp];
  for (int i = 0; i < sizeComp; ++i) {
    arquivo >> comp[i];
    arquivo.get();
  }

  arquivo.ignore(sMax, '\n');
  arquivo.ignore(sMax, '\n');

  arquivo >> sizeQuaren;
  arquivo.get();
  quaren = new double[sizeQuaren];
  for (int i = 0; i < sizeQuaren; ++i) {
    arquivo >> quaren[i];
    arquivo.get();
  }

  arquivo.close();
}

/*
  Metodo responsavel pela leitura do arquivo "Ambiente/DistribuicaoHumanos.csv". 

  Primeiramente e lido a quantidade de registros presentes no arquivo. Em 
  seguida sao lidos os registros. Cada registro descreve um caso de infeccao de 
  humano que sera inserido na simulacao, sendo composto pelos atributos:

  "quadra": id da quadra da posicao inicial do humano; 
  "lote": id do lote da posicao inicial do humano; 
  "latitude": latitude inicial do humano; 
  "longitude": longitude inicial do humano; 
  "sexo": sexo do humano (M ou F); 
  "faixa etaria": faixa etaria do humano (C, J, A ou I); 
  "saude": saude do humano (S ou I); 
  "sorotipo atual": sorotipo do humano (1, 2, 3, 4 ou 0 se ausente); 
  "ciclo": ciclo de entrada do humano na simulacao. 

  Atualmente a posicao do humano que e lida do arquivo nao e utilizada. Ela e 
  substituida por uma posicao qualquer do ambiente que e escolhida 
  aleatoriamente. Com esta alteracao objetivou-se alcancar uma melhor 
  distribuicao espacial dos casos de Influenza inseridos, evitando a formacao de 
  clusters de infeccao. Para remover este comportamento basta comentar o trecho 
  de cidigo indicado abaixo. 
*/
void Ambiente::lerArquivoDistribuicaoHumanos() {
  string entrada = entradaMC;
  entrada += string("Ambiente");
  entrada += SEP;
  entrada += string("DistribuicaoHumanos.csv");

  ifstream arquivo;
  arquivo.open(entrada);
  if (not arquivo.is_open()) {
    cerr << "Arquivo: ";
    cerr << entrada;
    cerr << " nao foi aberto!" << endl;
    exit(1);
  }

  arquivo >> sizeDistHumanos;
  arquivo.get();
  arquivo.ignore(sMax, '\n');

  distHumanos = new Caso[sizeDistHumanos];

  int q, l, x, y, s, fe, sd, st, cic;
  char s1, fe1, sd1;

  for (int i = 0; i < sizeDistHumanos; ++i) {
    arquivo >> q; arquivo.get();
    arquivo >> l; arquivo.get();
    arquivo >> x; arquivo.get();
    arquivo >> y; arquivo.get();
    arquivo >> s1; arquivo.get();
    arquivo >> fe1; arquivo.get();
    arquivo >> sd1; arquivo.get();
    arquivo >> st; arquivo.get();
    arquivo >> cic; arquivo.get();

    switch (s1) {
      case 'M': s = MASCULINO; break;
      case 'F': s = FEMININO; break;
    }

    switch (fe1) {
      case 'B': fe = BEBE; break;
      case 'C': fe = CRIANCA; break;
      case 'D': fe = ADOLESCENTE; break;
      case 'J': fe = JOVEM; break;
      case 'A': fe = ADULTO; break;
      case 'I': fe = IDOSO; break;
    }

    switch (sd1) {
      case 'S': sd = SUSCETIVEL; break;
      case 'I': sd = INFECTANTE; break;
    }

    // Trecho de cidigo responsavel pela escolha aleatiria de uma posicao 
    // do ambiente.
    RandPerc rand;
    q = (int)(rand() * nQuadras);
    l = (int)(rand() * nLotes[q]);   
    int posicoesLote = (indPos[indQuadras[2 * q] + l + 1] - 
                        indPos[indQuadras[2 * q] + l]);
    int p = posicoesLote * rand();
    x = pos[indPos[indQuadras[2 * q] + l] + p].x;
    y = pos[indPos[indQuadras[2 * q] + l] + p].y;

    distHumanos[i].q = q;
    distHumanos[i].l = l;
    distHumanos[i].x = x;
    distHumanos[i].y = y;
    distHumanos[i].s = s;
    distHumanos[i].fe = fe;
    distHumanos[i].sd = sd;
    distHumanos[i].st = st;
    distHumanos[i].cic = cic;
  }

  arquivo.close();
}
