private void gerarIndexVizinhancas() {
  indexVizinhancas = new ArrayList<>();
  int desl = 0;

  for (String quadra : indicesQuadras.keySet()) {
    indexVizinhancas.add(desl);
    for (String lote : indicesLotes.get(quadra).keySet()) {
      desl += vizinhancas.get(quadra).get(lote).size() * 6;
      indexVizinhancas.add(desl);
    }
  }
}
