private void gerarVizinhancas() {
  ArrayList<Vizinhanca> ret = con.getVizinhancasMoorePontos();
  ret.sort(null);

  vizinhancas = new HashMap<>();
  for (Vizinhanca i : ret) {
    vizinhancas.putIfAbsent(i.getFirst().quadra, new HashMap<>());
    vizinhancas.get(i.getFirst().quadra).putIfAbsent(i.getFirst().lote,
        new ArrayList<>());
    vizinhancas.get(i.getFirst().quadra).get(i.getFirst().lote).add(i);
  }
}
