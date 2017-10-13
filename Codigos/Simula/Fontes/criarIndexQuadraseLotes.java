private void criarIndexQuadraseLotes() {
  indicesQuadras = new TreeMap<>();
  indicesLotes = new TreeMap<>();

  int idQuadra = 0;
  for (String quadra : pontos.keySet().stream().sorted()
      .collect(Collectors.toList())) {
    int idLote = 0;
    for (String lote : pontos.get(quadra).keySet().stream().sorted()
        .collect(Collectors.toList())) {
      indicesLotes.putIfAbsent(quadra, new TreeMap<>());
      indicesLotes.get(quadra).putIfAbsent(lote, idLote);
      idLote++;
    }
    indicesQuadras.putIfAbsent(quadra, idQuadra);
    idQuadra++;
  }
}
