#include "Saidas.h"
#include "Fontes/Ambiente.h"
#include "Fontes/Saidas.h"
#include "Fontes/Humanos/Humanos.h"
#include "Fontes/Macros/MacrosHumanos.h"
#include "Fontes/Macros/MacrosGerais.h"

ContPopTH::ContPopTH(Humanos *humanos, Saidas *saidas, int ciclo) {
  this->humanos = humanos->PhumanosDev;
  this->nHumanos = humanos->nHumanos;
  this->popT = saidas->PpopTHDev;
  this->ciclo = ciclo;
}

__host__ __device__
void ContPopTH::operator()(int id) {
  for (int i = 0; i < nHumanos; ++i) {
    if (GET_SD_H(i) == MORTO) continue;

    int desl = (GET_S_H(i) * N_IDADES * N_ESTADOS_H);
    desl += (GET_FE_H(i) * N_ESTADOS_H);
    desl += (GET_SD_H(i) - 1);

    if(desl == id) popT[VEC(ciclo, desl, N_COLS_H)]++;
  }
}

ContPopQH::ContPopQH(Humanos *humanos, Saidas *saidas, int ciclo) {
  this->humanos = humanos->PhumanosDev;
  this->nHumanos = humanos->nHumanos;
  this->indPopQ = saidas->PindPopQHDev;
  this->popQ = saidas->PpopQHDev;
  this->ciclo = ciclo;
}

__host__ __device__
void ContPopQH::operator()(int id) {
  for (int i = 0; i < nHumanos; ++i) {
    if (GET_SD_H(i) == MORTO) continue;

    int desl = (GET_S_H(i) * N_IDADES * N_ESTADOS_H);
    desl += (GET_FE_H(i) * N_ESTADOS_H);
    desl += ((GET_SD_H(i) - 1));
    int q = GET_Q_H(i);

    if(desl == id) popQ[indPopQ[q] + VEC(ciclo, desl, N_COLS_H)]++;
  }
}

ContEspacialH::ContEspacialH(Humanos *humanos, Saidas *saidas, 
                             Ambiente *ambiente, int nCiclos, int ciclo) {
  this->humanos = humanos->PhumanosDev;
  this->indHumanos = humanos->PindHumanosDev;
  this->espacial = saidas->PespacialHDev;
  this->ciclo = ciclo;
  this->nCiclos = nCiclos;
  this->pos = ambiente->PposDev;
}

__host__ __device__
void ContEspacialH::operator()(int id) {
  int x = pos[id].x, y = pos[id].y;
  int l = pos[id].lote, q = pos[id].quadra;
  int d = VEC(id, ciclo, nCiclos), e;

  for (int i = indHumanos[q]; i < indHumanos[q + 1]; ++i) {
    if (GET_SD_H(i) == MORTO or GET_L_H(i) != l or
        GET_X_H(i) != x or GET_Y_H(i) != y) continue;

    e = 2000;
    e += (N_IDADES - GET_FE_H(i)) * 10;
    switch (GET_SD_H(i)) {
      case INFECTANTE: e += 3;
        break;
      case SUSCETIVEL: e += 1;
        break;
      case EXPOSTO: e += 2;
        break;
      case RECUPERADO: e += 4;
        break;
    }
    if (e % 10 > espacial[d] % 10) {
      espacial[d] = e;
    }
  }
}

ContPopNovoTH::ContPopNovoTH(Humanos *humanos, Saidas *saidas, int ciclo) {
  this->humanos = humanos->PhumanosDev;
  this->nHumanos = humanos->nHumanos;
  this->popNovoT = saidas->PpopNovoTHDev;
  this->ciclo = ciclo;
}

__host__ __device__
void ContPopNovoTH::operator()(int id) {
  for (int i = 0; i < nHumanos; ++i) {
    if (GET_SD_H(i) == MORTO) continue;
    if (GET_C_H(i) != 1) continue;

    int desl = (GET_S_H(i) * N_IDADES * N_ESTADOS_H);
    desl += (GET_FE_H(i) * N_ESTADOS_H);
    desl += ((GET_SD_H(i) - 1));

    if(desl == id) popNovoT[VEC(ciclo, desl, N_COLS_H)]++;
  }
}

ContPopNovoQH::ContPopNovoQH(Humanos *humanos, Saidas *saidas, int ciclo) {
  this->humanos = humanos->PhumanosDev;
  this->nHumanos = humanos->nHumanos;
  this->indPopQ = saidas->PindPopQHDev;
  this->popQ = saidas->PpopNovoQHDev;
  this->ciclo = ciclo;
}

__host__ __device__
void ContPopNovoQH::operator()(int id) {
  for (int i = 0; i < nHumanos; ++i) {
    if (GET_SD_H(i) == MORTO) continue;
    if (GET_C_H(i) != 1) continue;

    int desl = (GET_S_H(i) * N_IDADES * N_ESTADOS_H);
    desl += (GET_FE_H(i) * N_ESTADOS_H);
    desl += ((GET_SD_H(i) - 1));
    int q = GET_Q_H(i);

    if(desl == id) popQ[indPopQ[q] + VEC(ciclo, desl, N_COLS_H)]++;
  }
}

ContEspacialNovoH::ContEspacialNovoH(Humanos *humanos, Saidas *saidas, 
                                     Ambiente *ambiente, int nCiclos, 
                                     int ciclo) {
  this->humanos = humanos->PhumanosDev;
  this->indHumanos = humanos->PindHumanosDev;
  this->espacial = saidas->PespacialNovoHDev;
  this->ciclo = ciclo;
  this->nCiclos = nCiclos;
  this->pos = ambiente->PposDev;
}

__host__ __device__
void ContEspacialNovoH::operator()(int id) {
  int x = pos[id].x, y = pos[id].y;
  int l = pos[id].lote, q = pos[id].quadra;
  int d = VEC(id, ciclo, nCiclos), e;

  for (int i = indHumanos[q]; i < indHumanos[q + 1]; ++i) {
    if (GET_SD_H(i) == MORTO or GET_L_H(i) != l or
        GET_X_H(i) != x or GET_Y_H(i) != y or GET_C_H(i) != 1) continue;

    e = 2000;
    e += (N_IDADES - GET_FE_H(i)) * 10;
    switch (GET_SD_H(i)) {
      case INFECTANTE: e += 3;
        break;
      case SUSCETIVEL: e += 1;
        break;
      case EXPOSTO: e += 2;
        break;
      case RECUPERADO: e += 4;
        break;
    }
    if (e % 10 > espacial[d] % 10) {
      espacial[d] = e;
    }
  }
}
