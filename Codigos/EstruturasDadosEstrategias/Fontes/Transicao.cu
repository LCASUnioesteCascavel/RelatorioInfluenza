__host__ __device__
void TransicaoEstadosHumanos::operator()(int id) {
  int idHumano = id;
  dre seed = seeds[id];
  urd<double> dist(0.0, 1.0);

  int c  = GET_C_H(idHumano);  int sd = GET_SD_H(idHumano);
  int fe = GET_FE_H(idHumano); 

  if (sd == MORTO) return;

  switch (sd) {
    case EXPOSTO: {
      if (c >= PERIODO_EXPOSTO_HUMANO(fe)) {
        SET_SD_H(idHumano, INFECTANTE);
        SET_C_H(idHumano, 0);
      } else {
        SET_C_H(idHumano, c + 1);
      }
    } break;
    case INFECTANTE: {
      if (c >= PERIODO_INFECTADO_HUMANO(fe)) {
        SET_SD_H(idHumano, RECUPERADO);
        SET_C_H(idHumano, 0);
      } else {
        SET_C_H(idHumano, c + 1);
      }
    } break;
    case QUARENTENA: {
      if (c >= PERIODO_QUARENTENA_HUMANO(fe)) {
        SET_SD_H(idHumano, RECUPERADO);
        SET_C_H(idHumano, 0);
      } else {
        SET_C_H(idHumano, c + 1);
      }
    } break;
    case IMUNIZADO: {
      if (c >= PERIODO_IMUNIZADO_HUMANO(fe)) {
        SET_SD_H(idHumano, SUSCETIVEL);
        SET_C_H(idHumano, 0);
      } else {
        SET_C_H(idHumano, c + 1);
      }
    } break;
    case RECUPERADO: {
      if (c >= PERIODO_RECUPERADO_HUMANO(fe)) {
        SET_SD_H(idHumano, SUSCETIVEL);
        SET_C_H(idHumano, 0);
      } else {
        SET_C_H(idHumano, c + 1);
      }
    } break;
  }
}

