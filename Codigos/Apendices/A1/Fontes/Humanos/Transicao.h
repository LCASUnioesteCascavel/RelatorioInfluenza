#ifndef __TRANSICAO_HUMANOS__
#define __TRANSICAO_HUMANOS__

#include <thrust/random.h>

using dre = thrust::default_random_engine;
template <class T>
using urd = thrust::uniform_real_distribution<T>;

class Ambiente;
class Parametros;
class Seeds;
class Humanos;
class Humano;

/*
  Classe responsavel pela transicao de estados dos agentes humanos. 
*/
struct TransicaoEstadosHumanos {

  Humano *humanos; double *parametros; dre *seeds;

  TransicaoEstadosHumanos(Humanos *humanos, Parametros *parametros, 
                          Seeds *seeds);
  __host__ __device__
  void operator()(int id);

};

/*
  Classe responsavel pela vacinacao dos agentes humanos. As variaveis "fEVac", 
  "perVac" e "cicVac" armazenam as faixas etarias que serao vacinadas, os 
  percentuais de vacinacao e os ciclos em que ocorrerao as campanhas, 
  respectivamente. 
*/
struct Vacinacao {

  Humano *humanos; double *parametros;
  int ciclo;
  int *fEVac, sizeFEVac, *perVac, sizePerVac, *cicVac, sizeCicVac;
  int *indHumanos; dre *seeds;

  Vacinacao(Humanos *humanos, Ambiente *ambiente, Parametros *parametros, 
            int ciclo, int sizeFEVac, int sizePerVac, 
            int sizeCicVac, Seeds *seeds);
  __host__ __device__ 
  void operator()(int id);

  private:

  __host__ __device__
  bool periodoVacinacao();
  __host__ __device__
  bool faixaEtariaTeraVacinacao(int fe);

};

/*
  Classe responsavel pela atualizacao de variaveis utilizadas durante a rotina 
  de vacinacao. 
*/
struct PosVacinacao {
  
  int ciclo;
  int *perVac, sizePerVac, *cicVac, sizeCicVac;

  PosVacinacao(Ambiente *ambiente, int ciclo, int sizePerVac, int sizeCicVac);
  __host__ __device__ 
  void operator()(int id);

};

/*
  Classe responsavel pela transicao de agentes humanos infectantes ao estado 
  de quarentena. 
*/
struct Quarentena {

  Humano *humanos; int *indHumanos;
  int ciclo; double *quaren; dre *seeds;

  Quarentena(Humanos *humanos, Ambiente *ambiente, int ciclo, 
             Seeds *seeds);

  __host__ __device__
  void operator()(int id);

};

#endif
