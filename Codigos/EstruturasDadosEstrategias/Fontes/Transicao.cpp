void transicao(TIPO_AGENTE *agentes, int quantAgentes, const double *parametros,
               const int *indexParametros) {
  for (int i = 0; i < quantAgentes; i++) {
    int c = GET_C(i);
    if (GET_X(i) != 0) {
      switch (GET_E(i)) {
      case EXPOSTO: {
        double periodo;
        switch (GET_I(i)) {
        case CRIANCA:
          periodo = PERIODO_EXPOSTO_CRIANCA(randomizarPercentual());
          break;
        case JOVEM:
          periodo = PERIODO_EXPOSTO_JOVEM(randomizarPercentual());
          break;
        case ADULTO:
          periodo = PERIODO_EXPOSTO_ADULTO(randomizarPercentual());
          break;
        case IDOSO:
          periodo = PERIODO_EXPOSTO_IDOSO(randomizarPercentual());
          break;
        }
        if (c >= periodo) {
          SET_E(i, INFECTADO);
          SET_C(i, 0);
        } else {
          SET_C(i, c + 1);
        }
      } break;
      case INFECTADO: {
        double periodo;
        switch (GET_I(i)) {
        case CRIANCA:
          periodo = PERIODO_INFECTADO_CRIANCA(randomizarPercentual());
          break;
        case JOVEM:
          periodo = PERIODO_INFECTADO_JOVEM(randomizarPercentual());
          break;
        case ADULTO:
          periodo = PERIODO_INFECTADO_ADULTO(randomizarPercentual());
          break;
        case IDOSO:
          periodo = PERIODO_INFECTADO_IDOSO(randomizarPercentual());
          break;
        }
        if (c >= periodo) {
          SET_E(i, RECUPERADO);
          SET_C(i, 0);
        } else {
          SET_C(i, c + 1);
        }
      } break;
      case RECUPERADO: {
        double periodo;
        switch (GET_I(i)) {
        case CRIANCA:
          periodo = PERIODO_RECUPERADO_CRIANCA(randomizarPercentual());
          break;
        case JOVEM:
          periodo = PERIODO_RECUPERADO_JOVEM(randomizarPercentual());
          break;
        case ADULTO:
          periodo = PERIODO_RECUPERADO_ADULTO(randomizarPercentual());
          break;
        case IDOSO:
          periodo = PERIODO_RECUPERADO_IDOSO(randomizarPercentual());
          break;
        }
        if (c >= periodo) {
          SET_E(i, SUSCETIVEL);
          SET_C(i, 0);
        } else {
          SET_C(i, c + 1);
        }
      } break;
      }
    }
  }
}