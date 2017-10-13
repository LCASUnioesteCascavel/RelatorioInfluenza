private void gerarIndexQuadras() {
  int desl = 0;
  indexQuadras = new ArrayList<>();

  for (String i : indicesQuadras.keySet()) {
    indexQuadras.add(desl);
    desl += indicesLotes.get(i).keySet().size();
    indexQuadras.add(desl);
    desl++;
  }
}
