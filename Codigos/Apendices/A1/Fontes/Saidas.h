#ifndef __SAIDAS__
#define __SAIDAS__

#include <fstream>
#include <iostream>
#include <string>

#include <thrust/copy.h>
#include <thrust/device_vector.h>

class Ambiente;
class Parametros;

using std::cerr;
using std::endl;
using std::ofstream;
using std::string;
using std::to_string;

template <class T>
using DVector = thrust::device_vector<T>;

using thrust::copy;
using thrust::raw_pointer_cast;

class Saidas {

  public:

  string saidaMC; Ambiente *ambiente; Parametros *parametros;

  int *popTH, sizePopTH, *indPopQH, sizeIndPopQH, *popQH, sizePopQH;
  int *popNovoTH, sizePopNovoTH, *popNovoQH, sizePopNovoQH;

  int *espacialH, sizeEspacialH;
  int *espacialNovoH, sizeEspacialNovoH;

  DVector<int> *popTHDev, *indPopQHDev, *popQHDev, *popNovoTHDev, *popNovoQHDev;
  DVector<int> *espacialHDev, *espacialNovoHDev;

  int *PpopTHDev, *PindPopQHDev, *PpopQHDev, *PespacialHDev, *PpopNovoTHDev;
  int *PpopNovoQHDev;
  int *PespacialNovoHDev;

  Saidas(Ambiente *ambiente, Parametros *parametros, string saidaMC);
  ~Saidas();
  void salvarPopulacoes();
  void salvarEspaciais(string saidaSim);
  void toCPU();
  int getMemoriaGPU();

  private:

  void toGPU();
  void salvarSaidaEspacial(int *espacial, string saidaSim, string nomeArquivo);
  void salvarSaidaEspacial(int *espacialH, int *espacialMD, string saidaSim, 
                           string nomeArquivo);
  void salvarPopT(int *popT, int nCols, string prefNomeArquivo);
  void salvarPopQ(int *indPopQ, int *popQ, int nCols, string prefNomeArquivo);
  void calcIndPopQ(int *indPopQ, int nCols);

};

#endif