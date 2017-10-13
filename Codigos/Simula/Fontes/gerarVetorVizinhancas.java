private void gerarVetorVizinhancas() {
  vetorVizinhancas = new ArrayList<>();
  for (String quadra : indicesQuadras.keySet()) {
    for (String lote : indicesLotes.get(quadra).keySet()) {
      vizinhancas.get(quadra).get(lote).stream().forEach(k -> {
        vetorVizinhancas.add((int) k.first.x);
        vetorVizinhancas.add((int) k.first.y);
        vetorVizinhancas.add((int) k.second.x);
        vetorVizinhancas.add((int) k.second.y);
        vetorVizinhancas
            .add(indicesLotes.get(k.second.quadra).get(k.second.lote));
        vetorVizinhancas.add(indicesQuadras.get(k.second.quadra));
      });
    }
  }
}
