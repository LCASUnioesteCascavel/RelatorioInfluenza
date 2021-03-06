#ifndef __SIMULACAO__
#define __SIMULACAO__

class Seeds;
class Parametros;
class Ambiente;
class Saidas;
class Humanos;

#include <thrust/count.h>
#include <thrust/for_each.h>
#include <thrust/functional.h>
#include <thrust/partition.h>

using std::cerr;
using std::cout;
using std::endl;
using std::ofstream;
using std::string;

using thrust::count_if;
using thrust::for_each_n;
using thrust::partition;
using thrust::plus;
using thrust::raw_pointer_cast;

/*
  Classe responsavel por armazenar todos os dados associados a execucao de uma 
  simulacao individual. 
*/
class Simulacao {

  int idSim, ciclo; string saidaSim; Seeds *seeds;
  Parametros *parametros; Ambiente *ambiente; Saidas *saidas;
  Humanos *humanos; 
  
  public:

  Simulacao(int idSim, string saidaSim, Saidas *saidas, Parametros *parametros, 
            Ambiente *ambiente);
  ~Simulacao();

  private:
  
  void iniciar();
  void insercaoHumanos();
  void movimentacaoHumanos();
  void contatoEntreHumanos();
  void transicaoEstadosHumanos();
  void vacinacao();
  void quarentena();
  void computarSaidas();
  void exibirConsumoMemoria();

};

#endif
