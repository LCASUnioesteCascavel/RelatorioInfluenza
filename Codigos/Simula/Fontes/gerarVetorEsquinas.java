private void gerarVetorEsquinas() {
  vetorEsquinas = (ArrayList<Integer>) esquinas.entrySet().stream()
      .sorted(Entry.comparingByKey()).flatMap(i -> i.getValue().stream())
      .flatMap(i -> Stream.of((int) i.x, (int) i.y,
          (int) indicesLotes.get(i.quadra).get(i.lote)))
      .collect(Collectors.toList());
}
