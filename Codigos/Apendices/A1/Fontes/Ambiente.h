#ifndef __AMBIENTE__
#define __AMBIENTE__

#include <fstream>
#include <iostream>
#include <limits>
#include <string>

#include <thrust/device_vector.h>

using std::cerr;
using std::endl;
using std::fstream;
using std::ifstream;
using std::numeric_limits;
using std::string;
using std::streamsize;

template <class T>
using DVector = thrust::device_vector<T>;

using thrust::raw_pointer_cast;

struct Posicao {

  int x, y, lote, quadra;

};

struct Vizinhanca {

  int xOrigem, yOrigem, xDestino, yDestino, loteDestino, quadraDestino;

};

struct Caso {

  int q, l, x, y, s, fe, sd, st, cic;

};

class Ambiente {

  public:

  string entradaMC; streamsize sMax = numeric_limits<streamsize>::max();
  fstream arquivo;

  int nQuadras, *nLotes, sizeNLotes, *indQuadras, sizeIndQuadras;
  int *indViz, sizeIndViz, sizeViz; Vizinhanca *viz;
  int *indPos, sizeIndPos, sizePos; Posicao *pos;
  int sizeFEVac, *fEVac, sizePerVac, *perVac, sizeCicVac, *cicVac;
  int sizeDistHumanos; Caso *distHumanos;
  int sizeSazo; double *sazo; int sizeQuaren; double *quaren;

  DVector<int> *nLotesDev, *indQuadrasDev, *indVizDev; 
  DVector<Vizinhanca> *vizDev;
  DVector<int> *indPosDev; DVector<Posicao> *posDev;
  DVector<int> *fEVacDev, *perVacDev, *cicVacDev;
  DVector<Caso> *distHumanosDev; DVector<double> *sazoDev;
  DVector<double> *quarenDev;

  int *PnLotesDev, *PindQuadrasDev, *PindVizDev; Posicao *PposDev;
  int *PindPosDev, *PfEVacDev, *PperVacDev, *PcicVacDev; Vizinhanca *PvizDev;
  Caso *PdistHumanosDev; double *PsazoDev; double *PquarenDev;

  Ambiente(string entradaMC);
  int getMemoriaGPU();
  ~Ambiente();

  private:

  void toGPU();
  void lerVetoresAmbientais();  
  int *lerVetor(int n);
  void lerQuadrasLotes();
  void lerVizinhancas();
  void lerPosicoes();
  int *lerControle(int& size);
  void lerVetoresControles();
  void lerArquivoDistribuicaoHumanos();

};

#endif
