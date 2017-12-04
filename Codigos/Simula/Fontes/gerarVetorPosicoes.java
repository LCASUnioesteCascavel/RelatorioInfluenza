private void gerarVetorPosicoes() {
  vetorPosicoes = new ArrayList<>();

  int idQuadra = 0;
  for (String quadra : indicesQuadras.keySet()) {
    int idLote = 0;
    for (String lote : indicesLotes.get(quadra).keySet()) {
      for (Ponto ponto : pontos.get(quadra).get(lote)) {
        vetorPosicoes.add((int) ponto.getX());
        vetorPosicoes.add((int) ponto.getY());
        vetorPosicoes.add(idLote);
        vetorPosicoes.add(idQuadra);
      }
      idLote++;
    }
    idQuadra++;
  }
}
