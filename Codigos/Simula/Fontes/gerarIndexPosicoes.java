private void gerarIndexPosicoes() {
  int desl = 0;
  indexPosicoes = new ArrayList<>();

  for (String quadra : indicesQuadras.keySet()) {
    indexPosicoes.add(desl);
    for (String lote : indicesLotes.get(quadra).keySet()) {
      desl += pontos.get(quadra).get(lote).size() * 5;
      indexPosicoes.add(desl);
    }
  }
}
