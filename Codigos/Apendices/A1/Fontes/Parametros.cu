#include "Parametros.h"
#include "Fontes/Macros/0_SIM.h"
#include "Fontes/Macros/MacrosSO.h"

Parametros::Parametros(string entradaMC) {
  this->entradaMC = entradaMC;

  lerParametros();

  nSims = QUANTIDADE_SIMULACOES;
  nCiclos = QUANTIDADE_CICLOS + 1;

  toGPU();
}

int Parametros::getMemoriaGPU() {
  return (nParametros * sizeof(double));
}

Parametros::~Parametros() {
  delete[](parametros);
  delete(parametrosDev); 
}

void Parametros::toGPU() {
  parametrosDev = new DVector<double>(parametros, parametros + nParametros);

  PparametrosDev = raw_pointer_cast(parametrosDev->data());
}

void Parametros::lerArquivo(string pasta, string nomeArquivo, 
                            int& i, int nPar) {
  string entrada = entradaMC;
  entrada += pasta;
  entrada += SEP;
  entrada += nomeArquivo;
  
  fstream arquivo(entrada);
  if (not arquivo.is_open()) {
    cerr << "Arquivo: ";
    cerr << entrada;
    cerr << " nao foi aberto!" << endl;
    exit(1);
  }

  arquivo.ignore(sMax, '\n');
  for (int j = 0; j < nPar; ++j) {
    arquivo.ignore(7, EOF);
    arquivo >> parametros[i];
    arquivo.get();
    i += 1;

    arquivo >> parametros[i];
    arquivo.get();
    i += 1;

    arquivo.ignore(sMax, '\n');
  }
  arquivo.close();
}

void Parametros::lerParametros() {
  nParametros = N_PAR;
  parametros = new double[nParametros]();

  int i = 0;
  lerArquivo("Simulacao", "0-SIM.csv", i, N_0_SIM);
  lerArquivo("Humanos", "0-INI.csv", i, N_0_INI_H);
  lerArquivo("Humanos", "1-MOV.csv", i, N_1_MOV_H);
  lerArquivo("Humanos", "2-CON.csv", i, N_2_CON_H);
  lerArquivo("Humanos", "3-TRA.csv", i, N_3_TRA_H);
}
