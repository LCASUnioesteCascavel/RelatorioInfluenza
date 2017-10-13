void movimentacao(TIPO_AGENTE *agentes, int quantAgentes,
                  const int *indexQuadras, const int *indexVizinhancas,
                  const int *vizinhancas, const double *parametros,
                  const int *indexParametros, const int *indexPosicoes,
                  const int *posicoes, int quantQuadras) {
  for (int id = 0; id < quantAgentes; id++) {
    int q = GET_Q(id);
    int l = GET_L(id);
    int x = GET_X(id);
    int y = GET_Y(id);
    if (x != 0) {
      if (GET_E(id) == INFECTADO &&
          randomizarPercentual() <= PERC_MIGRACAO(randomizarPercentual())) {
        int pos = randomizarPercentual() *
                  (indexPosicoes[indexQuadras[quantQuadras * 2 - 1]] / 4);
        x = posicoes[pos * 4 + 0];
        y = posicoes[pos * 4 + 1];
        l = posicoes[pos * 4 + 2];
        q = posicoes[pos * 4 + 3];
        SET_X(id, x);
        SET_Y(id, y);
        SET_L(id, l);
        SET_Q(id, q);
      }
      double taxa;
      switch (GET_I(id)) {
      case CRIANCA:
        taxa = TAXA_MOBILIDADE_CRIANCA(randomizarPercentual());
        break;
      case JOVEM:
        taxa = TAXA_MOBILIDADE_JOVEM(randomizarPercentual());
        break;
      case ADULTO:
        taxa = TAXA_MOBILIDADE_ADULTO(randomizarPercentual());
        break;
      case IDOSO:
        taxa = TAXA_MOBILIDADE_IDOSO(randomizarPercentual());
        break;
      }
      if (randomizarPercentual() <= taxa) {
        int quantidade = 0;
        for (int i = indexVizinhancas[indexQuadras[2 * q] + l];
             i < indexVizinhancas[indexQuadras[2 * q] + l + 1]; i += 6) {
          if (vizinhancas[i + 0] == x && vizinhancas[i + 1] == y) {
            quantidade++;
          }
        }
        if (quantidade > 0) {
          int k = 0;
          int indice = (int)(randomizarPercentual() * quantidade);
          for (int i = indexVizinhancas[indexQuadras[2 * q] + l];
               i < indexVizinhancas[indexQuadras[2 * q] + l + 1]; i += 6) {
            if (vizinhancas[i + 0] == x && vizinhancas[i + 1] == y) {
              if (k == indice) {
                x = vizinhancas[i + 2];
                y = vizinhancas[i + 3];
                l = vizinhancas[i + 4];
                q = vizinhancas[i + 5];
                break;
              } else {
                k++;
              }
            }
          }
          SET_X(id, x);
          SET_Y(id, y);
          SET_L(id, l);
          SET_Q(id, q);
        }
      }
    }
  }
}