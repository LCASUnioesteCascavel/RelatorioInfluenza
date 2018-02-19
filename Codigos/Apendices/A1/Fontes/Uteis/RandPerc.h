#ifndef __RAND_PERC__
#define __RAND_PERC__

#include <chrono>

#include <thrust/random.h>

using dre = thrust::default_random_engine;
template <class T>
using urd = thrust::uniform_real_distribution<T>;

using namespace std::chrono;

/*
  Classe responsavel pela geracao de numeros aleatorios em CPU. Cada instancia 
  da classe armazena os seeds, geradores e distribuicao pripria. E utilizada 
  a distribuicao uniforme a geracao dos numeros aleatorios, assim como e feito 
  para os numeros aleatorios gerados em GPU. 
*/
class RandPerc {

  unsigned seed; dre gen;
  urd<double> dis;

  public:

  RandPerc();
  double operator()();

};

#endif
