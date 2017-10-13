private void gerarIndexEsquinas() {
  indexEsquinas = new ArrayList<>();
  int desl = 0;

  indexEsquinas.add(desl);
  for (String i : esquinas.keySet().stream().sorted()
      .collect(Collectors.toList())) {
    desl += esquinas.get(i).size() * 3;
    indexEsquinas.add(desl);
  }
}
