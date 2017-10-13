private void gerarIndexFronteiras() {
  indexFronteiras = new ArrayList<>();
  int desl = 0;

  for (String quadra : indicesQuadras.keySet()) {
    indexFronteiras.add(desl);
    for (String lote : indicesLotes.get(quadra).keySet()) {
      if (!quadra.equals("0000")) {
        desl += fronteiras.get(quadra).get(lote).size() * 3;
      }
      indexFronteiras.add(desl);
    }
  }
}
