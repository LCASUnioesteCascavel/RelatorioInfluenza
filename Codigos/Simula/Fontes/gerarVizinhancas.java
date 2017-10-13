private void gerarVizinhancas() {
  ConsultasBanco con = ConsultasBanco.getInstance();
  ArrayList<Pair<Ponto, Ponto>> ret = con.getVizinhancasMoorePontos();
  ret.sort(new Pair.ComparatorPairPonto());

  vizinhancas = new HashMap<>();
  for (Pair<Ponto, Ponto> i : ret) {
    vizinhancas.putIfAbsent(i.first.quadra, new HashMap<>());
    vizinhancas.get(i.first.quadra).putIfAbsent(i.first.lote,
        new ArrayList<>());
    vizinhancas.get(i.first.quadra).get(i.first.lote).add(i);
  }
}
