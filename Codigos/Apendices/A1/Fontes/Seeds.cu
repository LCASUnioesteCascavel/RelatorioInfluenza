#include "Seeds.h"
#include "Fontes/Uteis/RandPerc.h"
#include "Fontes/Macros/MacrosParametros.h"

/*
  Operator () da classe InitSeeds.

  Este metodo e responsavel por inicializar as seeds para geracao de 
  numeros aleatorios em GPU. O primeiro elemento da tupla "t" e um id numerico 
  da seed e o segundo elemento e a estrutura utilizada para geracao dos 
  numeros aleatorios. 
*/
__host__ __device__ 
void InitSeeds::operator()(thrust::tuple<int, dre &> t) {
  int seed = get<0>(t);
  get<1>(t) = dre(seed);
}

/*
  Construtor da classe Seeds.

  A variavel "ind1" armazena uma instancia da classe "counting_iterator", que 
  mimetiza um contador infinito, comecando em "0". Esta variavel e utilizada a  
  geracao de ids a execucao dos metodos em GPU. Cada id indexa um dado que 
  pode ser processado paralelamente aos outros dados do conjunto. 

  O metodo realiza a geracao de numeros aleatorios em CPU e os utiliza a 
  geracao das seeds em GPU. 

  A quantidade de seeds geradas e obtida a partir do valor maximo encontrado 
  na lista "l". A lista "l" e inicializada com os valores das quantidades de 
  agentes humanos e posicoes do ambiente. 
*/
Seeds::Seeds(initializer_list<int> l) {
  // Inicializacao do contador em 0. 
  ind1 = make_counting_iterator(0);

  // Obtencao da quantidade de seeds necessarias. 
  nSeeds = max(l);

  RandPerc rand;

  // Geracao de numeros aleatorios em CPU.
  seedsDev = new DVector<dre>(nSeeds);
  int *rands = new int[nSeeds]();
  for (int i = 0; i < nSeeds; ++i) {
    rands[i] = (int)ENTRE_FAIXA(0, 100000, rand());
  }
  DVector<int> randsDev(rands, rands + nSeeds);
  delete[](rands);

  // Inicializacao das seeds em GPU. 
  for_each_n(
    make_zip_iterator(
      make_tuple(
        randsDev.begin(), seedsDev->begin()
      )
    ), nSeeds, InitSeeds()
  );
  PseedsDev = raw_pointer_cast(seedsDev->data());
}

/*
  Metodo responsavel pela obtencao do consumo de memiria da classe Seeds. 
*/
int Seeds::getMemoriaGPU() {
  return (nSeeds * sizeof(dre));
}

/*
  Destrutor da classe Seeds
*/
Seeds::~Seeds() {
  delete(seedsDev);
}
