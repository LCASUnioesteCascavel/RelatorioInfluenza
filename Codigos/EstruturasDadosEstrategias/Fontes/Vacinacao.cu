__host__ __device__ 
void Vacinacao::operator()(int id) {
  if (not periodoVacinacao()) return;

  dre seed = seeds[id];
  urd<double> dist(0.0, 1.0);

  int qVac = id;

  int fe_h, sd_h;
  int n[N_IDADES] = {0, 0, 0, 0};

  for (int idHumano = indHumanos[qVac]; 
       idHumano < indHumanos[qVac + 1]; ++idHumano) {
    fe_h = GET_FE_H(idHumano); sd_h = GET_SD_H(idHumano);

    if (sd_h == SUSCETIVEL) {
      n[fe_h]++;
    }
  }

  double percentualVacinacao = 1.0 / perVac[0];
  for (int fe = 0; fe < N_IDADES; ++fe) {
    if (faixaEtariaTeraVacinacao(fe)) {
      n[fe] = lround(n[fe] * percentualVacinacao);
    } else {
      n[fe] = 0;
    }
  }

  for (int idHumano = indHumanos[qVac]; 
       idHumano < indHumanos[qVac + 1]; ++idHumano) {
    fe_h = GET_FE_H(idHumano);
    sd_h = GET_SD_H(idHumano);

    if (sd_h == SUSCETIVEL and n[fe_h] > 0) {
      n[fe_h]--;

      if (randPerc <= TAXA_EFICACIA_VACINA) {
        SET_SD_H(idHumano, IMUNIZADO);
      }
    }
  }
}

__host__ __device__ 
void PosVacinacao::operator()(int id) {
  bool houveVacinacao = false;
  if (perVac[1] < perVac[0]) {
    for (int i = 0; i < sizeCicVac; ++i) {
      if (ciclo >= cicVac[i] and ciclo < (cicVac[i] + perVac[0])) {
        houveVacinacao = true;
        break;
      }
    }
  }
  if (houveVacinacao) perVac[1]++;
  else perVac[1] = 0;
}
