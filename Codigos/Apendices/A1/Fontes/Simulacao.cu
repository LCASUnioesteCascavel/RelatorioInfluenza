#include "Simulacao.h"

#include "Fontes/Seeds.h"
#include "Fontes/Parametros.h"
#include "Fontes/Ambiente.h"
#include "Fontes/Uteis/RandPerc.h"
#include "Fontes/Saidas.h"
#include "Fontes/Macros/MacrosSO.h"
#include "Fontes/Macros/MacrosGerais.h"

#include "Fontes/Humanos/Humanos.h"
#include "Fontes/Humanos/Movimentacao.h"
#include "Fontes/Humanos/Contato.h"
#include "Fontes/Humanos/Transicao.h"
#include "Fontes/Humanos/Insercao.h"
#include "Fontes/Humanos/Saidas.h"

/*
  Construtor da classe Simulacao. 

  A variavel "idSim" indica o id numerico da simulacao individual. 
  "saidaSim" indica o caminho para a pasta de saida da simulacao. 
  "saidas", "parametros" e "ambiente" armazenam as saidas, os parametros e o 
  ambiente de simulacao, respectivamente. 

  Este metodo e responsavel por criar a pasta de saida dos arquivos resultantes 
  da simulacao, inicializar a populacao de humanos, inicializar 
  as seeds utilizadas a geracao de numeros aleatorios, exibir em tela o 
  consumo de memiria total da simulacao, iniciar a execucao da simulacao 
  individual, copiar os resultados da simulacao da GPU para a memoria 
  principal e salvar as saidas espaciais da simulacao. Note que somente as 
  saidas espaciais sao salvas para a simulacao individual. As saidas 
  populacionais sao tipo Monte Carlo e sao salvas pela classe MonteCarlo. 
*/
Simulacao::Simulacao(int idSim, string saidaSim, Saidas *saidas, 
                     Parametros *parametros, Ambiente *ambiente) {
  this->idSim = idSim;
  this->saidaSim = saidaSim;
  this->saidas = saidas;
  this->parametros = parametros;
  this->ambiente = ambiente;

  ciclo = 0;

  // Criacao da pasta de saida da simulacao individual. 
  system((CRIAR_PASTA + saidaSim).c_str());

  // Criacao dos agentes humanos. 
  humanos = new Humanos(parametros, ambiente);

  // Inicializacao das seeds.
  seeds = new Seeds(
                {humanos->nHumanos, ambiente->sizePos}
              );

  // Exibicao em tela do consumo de memiria total da simulacao individual. 
  if (idSim == 0) exibirConsumoMemoria();

  // Inicializacao da execucao da simulacao individual. 
  iniciar();

  // Cipia das saidas da simulacao que estao em GPU para a CPU. 
  saidas->toCPU();
  // Escrita dos arquivos de saida espaciais da simulacao individual. 
  saidas->salvarEspaciais(saidaSim);
}

/*
  Destrutor da classe Simulacao. 
  
  Sao desalocados as classes que armazenam os agentes humanos e as 
  seeds utilizadas durante a simulacao. 
*/
Simulacao::~Simulacao() {
  delete(humanos); delete(seeds);
}

/*
  Metodo responsavel por executar o processo de simulacao. Sao executados os 
  operadores definidos a modelagem da Influenza na ordem especificada. O 
  primeiro for e responsavel por executar os ciclos de simulacao. Os operadores 
  sao executados uma vez a cada ciclo. 
*/
void Simulacao::iniciar() {
  // Obtencao do estado inicial das saidas da simulacao. 
  computarSaidas();

  // Execucao dos ciclos de simulacao. 
  for (ciclo = 1; ciclo < parametros->nCiclos; ++ciclo) {

    insercaoHumanos();
    movimentacaoHumanos();
    //vacinacao();
    contatoEntreHumanos();
    transicaoEstadosHumanos();
    //quarentena();

    computarSaidas();
  }
}

/*
  Metodo responsavel pela execucao da insercao de agentes humanos no ambiente 
  durante a simulacao.  
 
  Inicialmente e obtida a quantidade total de agentes humanos que serao 
  inseridos. Esta quantidade depende dos parametros definidos no arquivo 
  "DistribuicaoHumanos.csv". 

  Em seguida sao inseridos os agentes humanos. Os novos agentes sao inseridos, 
  se possivel, em posicoes do vetor de agentes humanos que contenham agentes 
  mortos, com o objetivo de otimizar o uso de memiria e evitar realocacoes 
  desnecessarias. O vetor de humanos somente e realocado se a quantidade de 
  agentes que serao inseridos e maior que a quantidade de agentes mortos. 
  Antes da insercao o vetor de agentes e particionado, movendo os agentes 
  mortos para o inicio do vetor, facilitando desta forma a insercao dos novos 
  agentes. For fim sao atualizados os indices para os humanos, pois as 
  quantidades de agentes nas quadras foram alterados. 

  O metodo "for_each_n" e responsavel pela aplicacao do operador 
  "InsercaoHumanos" a insercao dos novos agentes humanos. 
*/
void Simulacao::insercaoHumanos() {
  int n = transform_reduce(
            seeds->ind1, seeds->ind1 + 1,  
            PreInsercaoHumanos(parametros, ciclo, ambiente), 
            0, plus<int>()
          );
  if (n > 0) {
    int m = count_if(
              humanos->humanosDev->begin(), 
              humanos->humanosDev->end(), 
              EstaMortoHumano()
            );

    if (n > m) {
      humanos->nHumanos += (n - m);
      humanos->humanosDev->resize(humanos->nHumanos, Humano());
      humanos->PhumanosDev = raw_pointer_cast(humanos->humanosDev->data());
    }

    partition(
      humanos->humanosDev->begin(), 
      humanos->humanosDev->end(), 
      EstaMortoHumano()
    );

    for_each_n(
      seeds->ind1, 1, 
      InsercaoHumanos(humanos, ambiente, parametros, ciclo)
    );

    humanos->atualizacaoIndices();
  }
}

/*
  Metodo responsavel pela movimentacao dos agentes humanos. 

  O metodo "for_each_n" e responsavel pela aplicacao do operador 
  "MovimentacaoHumanos" sobre toda a populacao de agentes humanos. Como a 
  biblioteca Thrust e utilizada, a aplicacao desta operacao pode ocorrer 
  paralelamente sobre os dados, dependendo das flags utilizadas durante a
  compilacao realizada. 

  O metodo "humanos->atualizacaoIndices" e responsavel pela atualizacao dos 
  indices da estrutura que armazena os agentes humanos. Este indice agiliza 
  a obtencao dos humanos que estao em uma determinada quadra. Por exemplo, 
  "indHumanos[10]" armazena a primeira posicao da regiao de dados que contem os 
  agentes posicionados na quadra "10". A atualizacao dos indices e necessaria 
  pois a movimentacao pode alterar a quadra em que os humanos estao posicionados. 
*/
void Simulacao::movimentacaoHumanos() {
  for_each_n(
    seeds->ind1, humanos->nHumanos,
    MovimentacaoHumanos(humanos, ambiente, parametros, seeds)
  );
  humanos->atualizacaoIndices();
}

/*
  Metodo responsavel pelo contato entre agentes humanos, em que ocorrem a 
  transmissao da doenca de agentes infectados para agentes suscetiveis.  

  O metodo "for_each_n" e responsavel pela aplicacao do operador 
  "ContatoHumanos" sobre todo o ambiente de simulacao. Como a biblioteca 
  Thrust e utilizada, a aplicacao desta operacao pode ocorrer paralelamente 
  sobre os dados, dependendo das flags utilizadas durante a compilacao realizada. 
*/
void Simulacao::contatoEntreHumanos() {
  for_each_n(
    seeds->ind1, ambiente->sizePos,
    ContatoHumanos(humanos, ambiente, parametros, ciclo - 1, seeds)
  );
}

/*
  Metodo responsavel pela transicao de estados dos agentes humanos, em que 
  ocorre a evolucao da doenca dos agentes infectados. 

  O metodo "for_each_n" e responsavel pela aplicacao do operador 
  "TransicaoEstadosHumanos" sobre toda a populacao de agentes humanos. Como a 
  biblioteca Thrust e utilizada, a aplicacao desta operacao pode ocorrer 
  paralelamente sobre os dados, dependendo das flags utilizadas durante a 
  compilacao realizada. 
*/
void Simulacao::transicaoEstadosHumanos() {
  for_each_n(
    seeds->ind1, humanos->nHumanos,
    TransicaoEstadosHumanos(humanos, parametros, seeds)
  );
}

/*
  Metodo responsavel pela vacinacao dos agentes humanos. 

  A primeira chamada ao metodo "for_each_n" e responsavel pela aplicacao do 
  operador "Vacinacao" sobre todo o ambiente. 

  A segunda chamada ao metodo "for_each_n" e responsavel pela aplicacao do 
  operador "PosVacinacao", que realiza a atualizacao da campanha de vacinacao 
  ao longo do tempo. 

  Como a biblioteca Thrust e utilizada, a aplicacao destas operacoes podem 
  ocorrer paralelamente sobre os dados, dependendo das flags utilizadas durante 
  a compilacao realizada. 
*/
void Simulacao::vacinacao() {
  for_each_n(
    seeds->ind1, ambiente->nQuadras, 
    Vacinacao(
      humanos, ambiente, parametros, ciclo, 
      ambiente->sizeFEVac, 
      ambiente->sizePerVac, ambiente->sizeCicVac, seeds
    )
  );
  for_each_n(
    seeds->ind1, 1, 
    PosVacinacao(
      ambiente, ciclo, ambiente->sizePerVac, ambiente->sizeCicVac
    )
  );
}

/*
  Metodo responsavel pela aplicacao da quarentena sobre a populacao dos agentes 
  humanos. 

  O metodo "for_each_n" e responsavel pela aplicacao do operador 
  "Quarentena" sobre toda a populacao de agentes humanos. Como a biblioteca 
  Thrust e utilizada, a aplicacao desta operacao pode ocorrer paralelamente 
  sobre os dados, dependendo das flags utilizadas durante a compilacao realizada. 
*/
void Simulacao::quarentena() {
  for_each_n(
    seeds->ind1, humanos->nHumanos, 
    Quarentena(
      humanos, ambiente, ciclo, seeds
    )
  );
}

/*
  Metodo responsavel pelo processamento das saidas resultantes do ciclo de 
  simulacao. As saidas populacionais sao geradas paralelamente para cada 
  subpopulacao computada. Ja as saidas espaciais sao geradas paralelamente para 
  cada posicao do ambiente. As chamadas aos metodos "for_each_n" sao responsaveis 
  pela aplicacao dos operadores sobre os dados. 
*/
void Simulacao::computarSaidas() {
  for_each_n(
    seeds->ind1, N_COLS_H,
    ContPopTH(humanos, saidas, ciclo)
  );
  for_each_n(
    seeds->ind1, N_COLS_H,
    ContPopQH(humanos, saidas, ciclo)
  );
  for_each_n(
    seeds->ind1, ambiente->sizePos,
    ContEspacialH(
      humanos, saidas, ambiente, parametros->nCiclos, ciclo
    )
  );
  for_each_n(
    seeds->ind1, ambiente->sizePos, 
    ContEspacialNovoH(
      humanos, saidas, ambiente, parametros->nCiclos, ciclo
    )
  );
  for_each_n(
    seeds->ind1, N_COLS_H,
    ContPopNovoTH(humanos, saidas, ciclo)
  );
  for_each_n(
    seeds->ind1, N_COLS_H,
    ContPopNovoQH(humanos, saidas, ciclo)
  );
}

/*
  Metodo responsavel pela exibicao em tela do consumo de memiria total em GPU 
  para todas as estruturas de dados presentes na simulacao. Sao utilizados os 
  metodos "getMemoriaGPU" das distintas classes com dados relevantes a simulacao. 
  Como os metodos retornam a quantidade de memoria em bytes, este valor e 
  convertido para MB para facilitar a leitura. Sao considerados os dados das 
  classes "Seeds", "Humanos", "Saidas", "Parametros" e "Ambiente". 
*/
void Simulacao::exibirConsumoMemoria() {
  double totMem = 0;
  totMem += seeds->getMemoriaGPU();
  totMem += humanos->getMemoriaGPU();
  totMem += saidas->getMemoriaGPU();
  totMem += parametros->getMemoriaGPU();
  totMem += ambiente->getMemoriaGPU();
  cout << (totMem / (1 << 20)) << "MB" << endl;
}
