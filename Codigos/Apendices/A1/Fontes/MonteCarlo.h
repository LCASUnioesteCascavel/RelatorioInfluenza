#ifndef __MONTE_CARLO__
#define __MONTE_CARLO__

#include <chrono>
#include <ctime>
#include <iomanip>
#include <iostream>
#include <string>

class Parametros;
class Ambiente;
class Saidas;

using std::cout;
using std::endl;
using std::put_time;
using std::localtime;
using std::time_t;
using std::string;
using std::to_string;

using namespace std::chrono;

/*
  Classe que armazena todos os dados relacionados a uma simulacao tipo 
  Monte Carlo. Simulacoes tipo Monte Carlo sao obtidas a partir do calculo da 
  media dos resultados obtidos por meio da execucao de simulacoes individuais. 
  As saidas populacionais sao geradas para as simulacoes tipo Monte Carlo 
  calculando-se a media, ciclo a ciclo, das quantidades de agentes pertencentes 
  a cada subpopulacao de interesse. Nao sao geradas saidas espaciais tipo 
  Monte Carlo. 
*/
class MonteCarlo {

  public:

  string entradaMC, saidaMC;
  Parametros *parametros; Ambiente *ambiente; Saidas *saidas;

  MonteCarlo(string entradaMC, string saidaMC);
  ~MonteCarlo();

  private:

  void iniciar();
  void exibirData();

};

#endif
