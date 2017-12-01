__host__ __device__
void ContatoHumanos::operator()(int id) {
  dre seed = seeds[id];
  urd<double> dist(0.0, 1.0);

  int x = pos[id].x, y = pos[id].y;
  int l = pos[id].lote, q = pos[id].quadra;

  int l_h, x_h, y_h, sd_h, fe_h;

  double taxaInfeccao;

  int nSus = 0, nInf = 0;

  for (int idHumano = indHumanos[q]; 
        idHumano < indHumanos[q + 1]; ++idHumano) {
    l_h = GET_L_H(idHumano); x_h = GET_X_H(idHumano); 
    y_h = GET_Y_H(idHumano); sd_h = GET_SD_H(idHumano); 

    if (sd_h == MORTO or l_h != l or x_h != x or y_h != y) continue;

    switch (sd_h) {
      case SUSCETIVEL: nSus++; break;
      case INFECTANTE: nInf++; break;
    }
  }

  if (nSus > 0 and nInf > 0) {
    for (int idHumano = indHumanos[q]; 
          idHumano < indHumanos[q + 1]; ++idHumano) {
      l_h = GET_L_H(idHumano); x_h = GET_X_H(idHumano); 
      y_h = GET_Y_H(idHumano); sd_h = GET_SD_H(idHumano); 
      fe_h = GET_FE_H(idHumano);

      if (sd_h != SUSCETIVEL or l_h != l or x_h != x or y_h != y) continue;

      taxaInfeccao = TAXA_INFECCAO_HUMANO_SUSCETIVEL(fe_h);

      if (randPerc <= (taxaInfeccao * sazo[ciclo] * K_SAZO)) {
        SET_SD_H(idHumano, EXPOSTO);
      }
    }
  }
}