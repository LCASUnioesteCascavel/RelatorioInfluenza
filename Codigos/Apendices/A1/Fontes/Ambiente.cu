#include "Ambiente.h"
#include "Fontes/Macros/MacrosGerais.h"
#include "Fontes/Macros/MacrosSO.h"
#include "Fontes/Uteis/RandPerc.h"

using std::cerr;
using std::endl;
using std::fstream;
using std::ifstream;
using std::numeric_limits;
using std::string;
using std::streamsize;
using thrust::raw_pointer_cast;

Ambiente::Ambiente(string entradaMC) {
  this->entradaMC = entradaMC;
  lerVetoresAmbientais();
  lerVetoresControles();
  lerArquivoDistribuicaoHumanos();
  toGPU();
}

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
  totMem += (sizeSazo * sizeof(double));
  totMem += (sizeQuaren * sizeof(double));
  return totMem;
}

Ambiente::~Ambiente() {
  delete[](nLotes); delete[](indQuadras); delete[](indViz); delete[](viz);
  delete[](indPos); delete[](pos); 
  delete[](fEVac); delete[](perVac); delete[](cicVac);
  delete[](distHumanos); delete[](sazo); delete[](quaren);

  delete(nLotesDev); delete(indQuadrasDev);
  delete(indVizDev); delete(vizDev); delete(indPosDev);
  delete(posDev);
  delete(fEVacDev); delete(perVacDev); delete(cicVacDev);
  delete(distHumanosDev); delete(sazoDev); delete(quarenDev);
}

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
  sazoDev = new DVector<double>(sazo, sazo + sizeSazo);
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
  PsazoDev = raw_pointer_cast(sazoDev->data());
  PquarenDev = raw_pointer_cast(quarenDev->data());
}

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

int *Ambiente::lerVetor(int n) {
  int *vec = new int[n]();
  for (int i = 0; i < n; ++i) {
    arquivo >> vec[i];
    arquivo.get();
  }
  return vec;
}

void Ambiente::lerQuadrasLotes() {
  arquivo >> nQuadras;
  arquivo.get();

  sizeNLotes = nQuadras;
  nLotes = lerVetor(sizeNLotes);

  sizeIndQuadras = nQuadras * 2;
  indQuadras = lerVetor(sizeIndQuadras);
}

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

int *Ambiente::lerControle(int& size) {
  arquivo.ignore(sMax, '\n');
  arquivo >> size;
  arquivo.get();
  int *vec = lerVetor(size);
  arquivo.ignore(sMax, '\n');
  return vec;
}

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
  
  fEVac = lerControle(sizeFEVac);

  sizePerVac = 2;
  perVac = new int[sizePerVac]();
  perVac[0] = 30; perVac[1] = 0; 

  cicVac = lerControle(sizeCicVac);

  arquivo.ignore(sMax, '\n');

  arquivo >> sizeSazo;
  arquivo.get();
  sazo = new double[sizeSazo];
  for (int i = 0; i < sizeSazo; ++i) {
    arquivo >> sazo[i];
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
      case 'C': fe = CRIANCA; break;
      case 'J': fe = JOVEM; break;
      case 'A': fe = ADULTO; break;
      case 'I': fe = IDOSO; break;
    }

    switch (sd1) {
      case 'S': sd = SUSCETIVEL; break;
      case 'I': sd = INFECTANTE; break;
    }

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
