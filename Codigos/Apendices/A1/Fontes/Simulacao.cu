#include "Simulacao.h"

#include "Fontes/Seeds.h"
#include "Fontes/Parametros.h"
#include "Fontes/Ambiente.h"
#include "Fontes/Uteis/RandPerc.h"
#include "Fontes/Saidas.h"
#include "Fontes/Macros/MacrosSO.h"
#include "Fontes/Macros/MacrosGerais.h"

#include "Fontes/Humanos/Humanos.h"
#include "Fontes/Humanos/Movimentacao.h"
#include "Fontes/Humanos/Contato.h"
#include "Fontes/Humanos/Transicao.h"
#include "Fontes/Humanos/Insercao.h"
#include "Fontes/Humanos/Saidas.h"

Simulacao::Simulacao(int idSim, string saidaSim, string entradaMC, 
                     Saidas *saidas, Parametros *parametros, 
                     Ambiente *ambiente) {
  this->idSim = idSim;
  this->saidaSim = saidaSim;
  this->entradaMC = entradaMC;
  this->saidas = saidas;
  this->parametros = parametros;
  this->ambiente = ambiente;

  ciclo = 0;

  system((CRIAR_PASTA + saidaSim).c_str());

  humanos = new Humanos(parametros, ambiente);

  seeds = new Seeds(
                {humanos->nHumanos, ambiente->sizePos}
              );

  if (idSim == 0) exibirConsumoMemoria();

  iniciar();

  saidas->toCPU();
  saidas->salvarEspaciais(saidaSim);
}

Simulacao::~Simulacao() {
  delete(humanos); delete(seeds);
}

void Simulacao::iniciar() {
  computarSaidas();

  for (ciclo = 1; ciclo < parametros->nCiclos; ++ciclo) {

    insercaoHumanos();
    movimentacaoHumanos();
    vacinacao();
    contatoEntreHumanos();
    transicaoEstadosHumanos();
    quarentena();

    computarSaidas();
  }
}

void Simulacao::insercaoHumanos() {
  int n = transform_reduce(
            seeds->ind1, seeds->ind1 + 1,  
            PreInsercaoHumanos(parametros, ciclo, ambiente), 
            0, plus<int>()
          );
  if (n > 0) {
    int m = count_if(
              humanos->humanosDev->begin(), 
              humanos->humanosDev->end(), 
              EstaMortoHumano()
            );

    if (n > m) {
      humanos->nHumanos += (n - m);
      humanos->humanosDev->resize(humanos->nHumanos, Humano());
      humanos->PhumanosDev = raw_pointer_cast(humanos->humanosDev->data());
    }

    partition(
      humanos->humanosDev->begin(), 
      humanos->humanosDev->end(), 
      EstaMortoHumano()
    );

    for_each_n(
      seeds->ind1, 1, 
      InsercaoHumanos(humanos, ambiente, parametros, ciclo)
    );

    humanos->atualizacaoIndices();
  }
}

void Simulacao::movimentacaoHumanos() {
  for_each_n(
    seeds->ind1, humanos->nHumanos,
    MovimentacaoHumanos(humanos, ambiente, parametros, seeds)
  );
  humanos->atualizacaoIndices();
}

void Simulacao::contatoEntreHumanos() {
  for_each_n(
    seeds->ind1, ambiente->sizePos,
    ContatoHumanos(humanos, ambiente, parametros, ciclo - 1, seeds)
  );
}

void Simulacao::transicaoEstadosHumanos() {
  for_each_n(
    seeds->ind1, humanos->nHumanos,
    TransicaoEstadosHumanos(humanos, parametros, seeds)
  );
}

void Simulacao::vacinacao() {
  for_each_n(
    seeds->ind1, ambiente->nQuadras, 
    Vacinacao(
      humanos, ambiente, parametros, ciclo, 
      ambiente->sizeFEVac, 
      ambiente->sizePerVac, ambiente->sizeCicVac, seeds
    )
  );
  for_each_n(
    seeds->ind1, 1, 
    PosVacinacao(
      ambiente, ciclo, ambiente->sizePerVac, ambiente->sizeCicVac
    )
  );
}

void Simulacao::quarentena() {
  for_each_n(
    seeds->ind1, humanos->nHumanos, 
    Quarentena(
      humanos, ambiente, ciclo, seeds
    )
  );
}

void Simulacao::computarSaidas() {
  for_each_n(
    seeds->ind1, N_COLS_H,
    ContPopTH(humanos, saidas, ciclo)
  );
  for_each_n(
    seeds->ind1, N_COLS_H,
    ContPopQH(humanos, saidas, ciclo)
  );
  for_each_n(
    seeds->ind1, ambiente->sizePos,
    ContEspacialH(
      humanos, saidas, ambiente, parametros->nCiclos, ciclo
    )
  );
  for_each_n(
    seeds->ind1, ambiente->sizePos, 
    ContEspacialNovoH(
      humanos, saidas, ambiente, parametros->nCiclos, ciclo
    )
  );
  for_each_n(
    seeds->ind1, N_COLS_H,
    ContPopNovoTH(humanos, saidas, ciclo)
  );
  for_each_n(
    seeds->ind1, N_COLS_H,
    ContPopNovoQH(humanos, saidas, ciclo)
  );
}

void Simulacao::exibirConsumoMemoria() {
  double totMem = 0;
  totMem += seeds->getMemoriaGPU();
  totMem += humanos->getMemoriaGPU();
  totMem += saidas->getMemoriaGPU();
  totMem += parametros->getMemoriaGPU();
  totMem += ambiente->getMemoriaGPU();
  cout << (totMem / (1 << 20)) << "MB" << endl;
}
