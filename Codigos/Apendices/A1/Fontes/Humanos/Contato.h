#ifndef __CONTATO_HUMANOS__
#define __CONTATO_HUMANOS__

#include <thrust/random.h>

using dre = thrust::default_random_engine;
template <class T>
using urd = thrust::uniform_real_distribution<T>;

class Ambiente;
class Parametros;
class Seeds;
class Humanos;
class Humano;
class Posicao;

struct ContatoHumanos {

  Humano *humanos; double *parametros;
  int ciclo, *indHumanos; double *sazo; Posicao *pos;
  dre *seeds;

  ContatoHumanos(Humanos *humanos, Ambiente *ambiente, 
                 Parametros *parametros, int ciclo, Seeds *seeds);
  __host__ __device__
  void operator()(int id);

};

#endif
