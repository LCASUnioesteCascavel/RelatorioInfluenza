private void gerarVetorFronteiras() {
  vetorFronteiras = new ArrayList<>();
  for (String quadra : indicesQuadras.keySet()) {
    for (String lote : indicesLotes.get(quadra).keySet()) {
      if (!quadra.equals("0000")) {
        for (Pair<Ponto, Ponto> i : fronteiras.get(quadra).get(lote)) {
          vetorFronteiras.add((int) i.second.x);
          vetorFronteiras.add((int) i.second.y);
          vetorFronteiras.add(
              (int) indicesLotes.get(i.second.quadra).get(i.second.lote));
        }
      }
    }
  }
}
