private void gerarIndexPosicoesRegioes() {
  int regiao, q;
  indexPosicoesRegioes = new ArrayList<>();
  indexPosicoesRegioes.add(0);
  indexPosicoesRegioes.add(0);
  indexPosicoesRegioes.add(0);

  for (String quadra : indicesQuadras.keySet()) {
    regiao = getRegiao(quadra.charAt(0));
    q = indexPosicoesRegioes.get(regiao);
    for (String lote : indicesLotes.get(quadra).keySet()) {
      q += pontos.get(quadra).get(lote).size();
    }
    indexPosicoesRegioes.set(regiao, q);
  }

  indexPosicoesRegioes.add(0, 0);
  for (int i = 1; i < indexPosicoesRegioes.size(); ++i) {
    q = indexPosicoesRegioes.get(i) + indexPosicoesRegioes.get(i - 1);
    indexPosicoesRegioes.set(i, q);
  }
}
