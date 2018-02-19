__host__ __device__ 
void Vacinacao::operator()(int id) {
  if (not periodoVacinacao()) return;

  dre seed = seeds[id];
  urd<double> dist(0.0, 1.0);

  // A vacinacao e aplicada em todas as quadras do ambiente. 
  int qVac = id;

  int fe_h, sd_h;
  int n[N_IDADES] = {0, 0, 0, 0};

  // Contagem, por faixas etarias, dos agentes presentes nesta quadra. 
  for (int idHumano = indHumanos[qVac]; 
       idHumano < indHumanos[qVac + 1]; ++idHumano) {
    fe_h = GET_FE_H(idHumano); sd_h = GET_SD_H(idHumano);

    if (sd_h == SUSCETIVEL) {
      n[fe_h]++;
    }
  }

  // Calcula as quantidades por faixas etarias de agentes que serao vacinados. 
  double percentualVacinacao = 1.0 / perVac[0];
  for (int fe = 0; fe < N_IDADES; ++fe) {
    if (faixaEtariaTeraVacinacao(fe)) {
      n[fe] = lround(n[fe] * percentualVacinacao);
    } else {
      n[fe] = 0;
    }
  }

  // Realiza a vacinacao para a quantidade de agentes calculada. 
  for (int idHumano = indHumanos[qVac]; 
       idHumano < indHumanos[qVac + 1]; ++idHumano) {
    fe_h = GET_FE_H(idHumano);
    sd_h = GET_SD_H(idHumano);

    if (sd_h == SUSCETIVEL and n[fe_h] > 0) {
      n[fe_h]--;

      // O agente pode ser passado probabilisticamente para o estado imunizado. 
      if (randPerc <= TAXA_EFICACIA_VACINA) {
        SET_SD_H(idHumano, IMUNIZADO);
      }
    }
  }
}

PosVacinacao::PosVacinacao(Ambiente *ambiente, int ciclo, 
                           int sizePerVac, int sizeCicVac) {
  this->ciclo = ciclo;
  this->sizePerVac = sizePerVac;
  this->sizeCicVac = sizeCicVac;
  this->perVac = ambiente->PperVacDev;
  this->cicVac = ambiente->PcicVacDev;
}
