private void gerarVetorIndexTrajetos() {
  indexTrajetos = new ArrayList<>();
  int desl = 0;
  indexTrajetos.add(desl);
  for (ArrayList<Local> i : trajetosCrianca) {
    desl += i.size() - 1;
    indexTrajetos.add(desl);
  }
  for (ArrayList<Local> i : trajetosJovem) {
    desl += i.size() - 1;
    indexTrajetos.add(desl);
  }
  for (ArrayList<Local> i : trajetosAdulto) {
    desl += i.size() - 1;
    indexTrajetos.add(desl);
  }
  for (ArrayList<Local> i : trajetosIdoso) {
    desl += i.size() - 1;
    indexTrajetos.add(desl);
  }

  nTrajetos = trajetosCrianca.size() + trajetosJovem.size()
      + trajetosAdulto.size() + trajetosIdoso.size();

  vetorTrajetos = (ArrayList<Integer>) IntStream
      .range(0, indexTrajetos.get(indexTrajetos.size() - 1)).boxed()
      .collect(Collectors.toList());
}
