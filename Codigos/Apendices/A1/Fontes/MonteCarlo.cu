#include "MonteCarlo.h"
#include "Fontes/Macros/MacrosSO.h"
#include "Fontes/Parametros.h"
#include "Fontes/Ambiente.h"
#include "Fontes/Saidas.h"
#include "Fontes/Simulacao.h"

MonteCarlo::MonteCarlo(string entradaMC, string saidaMC) {
  this->entradaMC = entradaMC;
  this->saidaMC = saidaMC;

  system((CRIAR_PASTA + saidaMC).c_str());

  parametros = new Parametros(entradaMC);
  ambiente = new Ambiente(entradaMC);
  saidas = new Saidas(ambiente, parametros, saidaMC);

  exibirData();
  auto t1 = high_resolution_clock::now();

  iniciar();

  auto t2 = high_resolution_clock::now(); 
  exibirData();

  cout << duration_cast<duration<double>>(t2 - t1).count() << "s" << endl;
}

MonteCarlo::~MonteCarlo() {
  delete(parametros); delete(ambiente); delete(saidas);
}

void MonteCarlo::iniciar() {
  string saidaSim;
  for (int idSim = 0; idSim < parametros->nSims; ++idSim) {
    saidaSim = saidaMC;
    saidaSim += string("Simulacao_");
    saidaSim += to_string(idSim);
    saidaSim += SEP;

    Simulacao(idSim, saidaSim, entradaMC, saidas, parametros, ambiente);

    saidas->limparEspaciais();
  }
  saidas->salvarPopulacoes();
}

void MonteCarlo::exibirData() {
  time_t data = system_clock::to_time_t(system_clock::now());
  cout << put_time(localtime(&data), "%d/%m/%Y %H:%M:%S") << endl;
}
