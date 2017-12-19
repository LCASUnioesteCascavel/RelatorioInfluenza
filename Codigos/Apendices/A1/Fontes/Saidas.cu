#include "Saidas.h"
#include "Fontes/Ambiente.h"
#include "Fontes/Parametros.h"
#include "Fontes/Macros/MacrosGerais.h"

Saidas::Saidas(Ambiente *ambiente, Parametros *parametros, string saidaMC) {
  this->ambiente = ambiente;
  this->parametros = parametros;
  this->saidaMC = saidaMC;

  sizeIndPopQH = ambiente->nQuadras + 1;
  indPopQH = new int[sizeIndPopQH]();
  calcIndPopQ(indPopQH, N_COLS_H);

  sizePopQH = indPopQH[ambiente->nQuadras];
  popQH = new int[sizePopQH]();

  sizePopTH = parametros->nCiclos * N_COLS_H;
  popTH = new int[sizePopTH]();

  sizePopNovoTH = parametros->nCiclos * N_COLS_H;
  popNovoTH = new int[sizePopNovoTH]();

  sizePopNovoQH = indPopQH[ambiente->nQuadras];
  popNovoQH = new int[sizePopNovoQH]();

  sizeEspacialH = ambiente->sizePos * parametros->nCiclos;
  espacialH = new int[sizeEspacialH]();

  sizeEspacialNovoH = ambiente->sizePos * parametros->nCiclos;
  espacialNovoH = new int[sizeEspacialNovoH]();
  
  toGPU();
}

Saidas::~Saidas() {
  delete[](popTH);delete[](indPopQH); delete[](popQH);
  delete[](espacialH); delete[](espacialNovoH);
  delete[](popNovoTH); delete[](popNovoQH);
  delete(popTHDev); delete(indPopQHDev); delete(popQHDev);
  delete(espacialHDev); delete(espacialNovoHDev);
  delete(popNovoTHDev); delete(popNovoQHDev);
}

void Saidas::salvarPopulacoes() {
  salvarPopQ(indPopQH, popQH, N_COLS_H, "Quantidades_Humanos_Quadra-");
  salvarPopT(popTH, N_COLS_H, "Quantidades_Humanos_Total");
  salvarPopT(popNovoTH, N_COLS_H, "Quantidades_Humanos_Novo_Total");
  salvarPopQ(indPopQH, popNovoQH, N_COLS_H, "Quantidades_Humanos_Novo_Quadra-");
}

void Saidas::salvarEspaciais(string saidaSim) {
  salvarSaidaEspacial(espacialH, saidaSim, "Espacial_Humanos");
  salvarSaidaEspacial(espacialNovoH, saidaSim, "Espacial_Novo_Humanos");
}

void Saidas::toCPU() {
  copy_n(popTHDev->begin(), sizePopTH, popTH);
  copy_n(popQHDev->begin(), sizePopQH, popQH);
  copy_n(espacialHDev->begin(), sizeEspacialH, espacialH);
  copy_n(espacialNovoHDev->begin(), sizeEspacialNovoH, espacialNovoH);
  copy_n(popNovoTHDev->begin(), sizePopNovoTH, popNovoTH);
  copy_n(popNovoQHDev->begin(), sizePopNovoQH, popNovoQH);
}

void Saidas::toGPU() {
  popTHDev = new DVector<int>(popTH, popTH + sizePopTH);
  indPopQHDev = new DVector<int>(indPopQH, indPopQH + sizeIndPopQH);
  popQHDev = new DVector<int>(popQH, popQH + sizePopQH);
  espacialHDev = new DVector<int>(espacialH, espacialH + sizeEspacialH);
  espacialNovoHDev = new DVector<int>(espacialNovoH, 
                                      espacialNovoH + sizeEspacialNovoH);
  popNovoTHDev = new DVector<int>(popNovoTH, popNovoTH + sizePopNovoTH);
  popNovoQHDev = new DVector<int>(popNovoQH, popNovoQH + sizePopNovoQH);

  PpopTHDev = raw_pointer_cast(popTHDev->data());
  PindPopQHDev = raw_pointer_cast(indPopQHDev->data());
  PpopQHDev = raw_pointer_cast(popQHDev->data());
  PespacialHDev = raw_pointer_cast(espacialHDev->data());
  PespacialNovoHDev = raw_pointer_cast(espacialNovoHDev->data());
  PpopNovoTHDev = raw_pointer_cast(popNovoTHDev->data());
  PpopNovoQHDev = raw_pointer_cast(popNovoQHDev->data());
}

int Saidas::getMemoriaGPU() {
  int totMem = 0;
  totMem += (sizePopTH * sizeof(int));
  totMem += (sizeIndPopQH * sizeof(int));
  totMem += (sizePopQH * sizeof(int));
  totMem += (sizeEspacialH * sizeof(int));
  totMem += (sizeEspacialNovoH * sizeof(int));
  totMem += (sizePopNovoTH * sizeof(int));
  totMem += (sizePopNovoQH * sizeof(int));
  return totMem;
}

void Saidas::salvarSaidaEspacial(int *espacial, string saidaSim, 
                                 string nomeArquivo) {
  string saida = saidaSim;
  saida += nomeArquivo;
  saida += string(".csv");

  ofstream arquivo(saida);
  if (not arquivo.is_open()) {
    cerr << "Arquivo: ";
    cerr << saida;
    cerr << " nao foi aberto!" << endl;
    exit(1);
  }

  for (int i = 0; i < ambiente->sizePos; ++i) {
    arquivo << ambiente->pos[i].x << ";";
    arquivo << ambiente->pos[i].y << ";";
    for (int j = 0; j < parametros->nCiclos; ++j) {
      arquivo << espacial[VEC(i, j, parametros->nCiclos)];
      arquivo << ";";
    }
    arquivo << endl;
  }
  arquivo.close();
}

void Saidas::salvarSaidaEspacial(int *espacialH, int *espacialMD, 
                                 string saidaSim, string nomeArquivo) {
  string saida = saidaSim;
  saida += nomeArquivo;
  saida += string(".csv");

  int d;
  ofstream arquivo(saida);
  if (not arquivo.is_open()) {
    cerr << "Arquivo: ";
    cerr << saida;
    cerr << " nao foi aberto!" << endl;
    exit(1);
  }

  for (int i = 0; i < ambiente->sizePos; ++i) {
    arquivo << ambiente->pos[i].x << ";";
    arquivo << ambiente->pos[i].y << ";";
    for (int j = 0; j < parametros->nCiclos; ++j) {
      d = VEC(i, j, parametros->nCiclos);
      arquivo << ((espacialH[d] % 10) * 10000 + espacialMD[d]);
      arquivo << ";";
    }
    arquivo << endl;
  }
  arquivo.close();
}

void Saidas::salvarPopT(int *popT, int nCols, string prefNomeArquivo) {
  string saida = saidaMC;
  saida += prefNomeArquivo;
  saida += string(".csv");

  int ind;
  ofstream arquivo(saida);
  if (not arquivo.is_open()) {
    cerr << "Arquivo: ";
    cerr << saida;
    cerr << " nao foi aberto!" << endl;
    exit(1);
  }

  for (int i = 0; i < parametros->nCiclos; ++i) {
    arquivo << i;
    for (int j = 0; j < nCols; ++j) {
      arquivo << ";";
      ind = VEC(i, j, nCols);
      arquivo << popT[ind] / parametros->nSims;
    }
    arquivo << endl;
  }
  arquivo.close();
}

void Saidas::salvarPopQ(int *indPopQ, int *popQ, int nCols, 
                        string prefNomeArquivo) {
  string saida;
  int ind, ind1, ind2;
  for (int idQuadra = 0; idQuadra < ambiente->nQuadras; ++idQuadra) {
    saida = saidaMC;
    saida += prefNomeArquivo;
    saida += to_string(idQuadra);
    saida += string(".csv");

    ofstream arquivo(saida);
    if (not arquivo.is_open()) {
      cerr << "Arquivo: ";
      cerr << saida;
      cerr << " nao foi aberto!" << endl;
      exit(1);
    }

    for (int i = 0; i < parametros->nCiclos; ++i) {
      arquivo << i;
      for (int j = 0; j < nCols; ++j) {
        arquivo << ";";
        ind1 = indPopQ[idQuadra];
        ind2 = VEC(i, j, nCols);
        ind = ind1 + ind2;
        arquivo << popQ[ind] / parametros->nSims;
      }
      arquivo << endl;
    }
    arquivo.close();
  }
}

void Saidas::calcIndPopQ(int *indPopQ, int nCols) {
  int i = 0, size = 0;
  for (int k = 0; k < ambiente->nQuadras; ++k) {
    indPopQ[i] = size;
    size += parametros->nCiclos * nCols;
    i += 1;
  }
  indPopQ[ambiente->nQuadras] = size;
}

void Saidas::limparEspaciais() {
  fill_n(espacialH, sizeEspacialH, 0);
  fill_n(espacialNovoH, sizeEspacialNovoH, 0);
  fill_n(espacialHDev->begin(), sizeEspacialH, 0);
  fill_n(espacialNovoHDev->begin(), sizeEspacialNovoH, 0);
}
