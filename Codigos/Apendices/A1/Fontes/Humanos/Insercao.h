#ifndef __INSERCAO_HUMANOS__
#define __INSERCAO_HUMANOS__

class Ambiente;
class Parametros;
class Humanos;
class Humano;
class Caso;

/*
  Classe responsavel pela contagem dos agentes humanos que serao inseridos 
  no ambiente durante a simulacao. 
*/
struct PreInsercaoHumanos {

  double *parametros; int ciclo;
  int sizeDistHumanos; Caso *distHumanos;

  PreInsercaoHumanos(Parametros *parametros, int ciclo, Ambiente *ambiente);
  __host__ __device__
  int operator()(int id);

};

/*
  Classe responsavel pela insercao de agentes humanos no ambiente durante a 
  simulacao. 
*/
struct InsercaoHumanos {

  double *parametros; Humano *humanos;
  int ciclo; int *indQuadras;
  int sizeDistHumanos; Caso *distHumanos;

  InsercaoHumanos(Humanos *humanos, Ambiente *ambiente, 
                  Parametros *parametros, int ciclo);
  __host__ __device__
  void operator()(int id);

  private:

  __host__ __device__
  void inicializarHumano(int id, int sd, int x, int y, int l, 
                         int q, int s, int fe);

};

#endif
