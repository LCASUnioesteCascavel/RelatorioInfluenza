private void gerarFronteiras() {
  ArrayList<Pair<Ponto, Ponto>> ret = (ArrayList<Pair<Ponto, Ponto>>) vizinhancas
      .entrySet().stream().filter(i -> !i.getKey().equals("0000"))
      .flatMap(i -> i.getValue().entrySet().stream())
      .flatMap(i -> i.getValue().stream()).filter(i -> i.second.isRua())
      .collect(Collectors.toList());

  fronteiras = new HashMap<>();
  for (Pair<Ponto, Ponto> i : ret) {
    fronteiras.putIfAbsent(i.first.quadra, new HashMap<>());
    fronteiras.get(i.first.quadra).putIfAbsent(i.first.lote,
        new ArrayList<>());
    fronteiras.get(i.first.quadra).get(i.first.lote).add(i);
  }
}
