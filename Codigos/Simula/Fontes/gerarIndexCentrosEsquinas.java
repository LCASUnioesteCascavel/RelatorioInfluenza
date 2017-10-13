private void gerarIndexCentrosEsquinas() {
  indexCentrosEsquinas = new ArrayList<>();
  int desl = 0;

  indexCentrosEsquinas.add(desl);
  for (String i : indicesLotes.get("0000").keySet()) {
    if (centroidesEsquinas.containsKey(i)) {
      desl += centroidesEsquinas.get(i).size() * 3;
    }
    indexCentrosEsquinas.add(desl);
  }
}
