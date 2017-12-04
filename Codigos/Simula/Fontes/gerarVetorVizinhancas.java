private void gerarVetorVizinhancas() {
  vetorVizinhancas = new ArrayList<>();
  for (String quadra : indicesQuadras.keySet()) {
    for (String lote : indicesLotes.get(quadra).keySet()) {
      vizinhancas.get(quadra).get(lote).stream().forEach(k -> {
        vetorVizinhancas.add((int) k.getFirst().getX());
        vetorVizinhancas.add((int) k.getFirst().getY());
        vetorVizinhancas.add((int) k.getSecond().getX());
        vetorVizinhancas.add((int) k.getSecond().getY());
        vetorVizinhancas.add(
            indicesLotes.get(k.getSecond().quadra).get(k.getSecond().lote));
        vetorVizinhancas.add(indicesQuadras.get(k.getSecond().quadra));
      });
    }
  }
}
