private void salvarArquivoDistribuicaoHumanos() {
  double percInsInfec = Double.parseDouble(params.get("PorcentagemCasos"));

  Map<String, Double> percPop = new HashMap<>();
  percPop.put("C",
      Double.parseDouble(params.get("FracaoCriancasMasculinas")));
  percPop.put("J", Double.parseDouble(params.get("FracaoJovensMasculinos")));
  percPop.put("A", Double.parseDouble(params.get("FracaoAdultosMasculinos")));
  percPop.put("I", Double.parseDouble(params.get("FracaoIdososMasculinos")));

  Map<Integer, List<CasoInfeccao>> dados = distribuicaoHumanos.stream()
      .collect(Collectors.groupingBy(CasoInfeccao::getDia));

  for (Entry<Integer, List<CasoInfeccao>> entry : dados.entrySet()) {
    List<CasoInfeccao> value = entry.getValue();

    Collections.shuffle(value);
    int ind = (int) Math.floor(value.size() * (1.0 - percInsInfec));
    value.subList(0, ind).clear();
  }

  Map<String, List<CasoInfeccao>> dados2 = dados.entrySet().stream()
      .flatMap(i -> i.getValue().stream())
      .collect(Collectors.groupingBy(CasoInfeccao::getFaixaEtaria));

  for (Entry<String, List<CasoInfeccao>> entry : dados2.entrySet()) {
    String key = entry.getKey();
    List<CasoInfeccao> value = entry.getValue();

    int nM = (int) (value.size() * percPop.get(key));
    Collections.shuffle(value);
    value.stream().forEach(i -> i.setSexo("F"));
    value.subList(0, nM).stream().forEach(i -> i.setSexo("M"));
  }

  List<CasoInfeccao> dados3 = dados2.entrySet().stream()
      .flatMap(i -> i.getValue().stream()).collect(Collectors.toList());
  dados3.sort(Comparator.comparing(CasoInfeccao::getDia));

  List<Ponto> pontos2 = pontos.entrySet().stream().map(i -> i.getValue())
      .flatMap(i -> i.entrySet().stream()).flatMap(i -> i.getValue().stream())
      .collect(Collectors.toList());
  for (CasoInfeccao caso : dados3) {
    Ponto ponto = pontos2.parallelStream()
        .collect(Collectors
            .minBy((i, j) -> Double.compare(i.distancia(caso.getPonto()),
                j.distancia(caso.getPonto()))))
        .get();
    caso.setX((int) ponto.getX());
    caso.setY((int) ponto.getY());
    caso.setQuadra(indicesQuadras.get(ponto.quadra));
    caso.setLote(indicesLotes.get(ponto.quadra).get(ponto.lote));
  }

  try {
    BufferedWriter esc = new BufferedWriter(
        new FileWriter(nomeArquivoDistribuicaoHumanos));
    esc.write(dados3.size() + "\n");
    esc.write("Q;L;X;Y;Sexo;FaixaEtaria;SaudeDengue;SorotipoAtual;Ciclo\n");
    for (CasoInfeccao caso : dados3) {
      esc.write(caso.getQuadra() + ";");
      esc.write(caso.getLote() + ";");
      esc.write(caso.getX() + ";");
      esc.write(caso.getY() + ";");
      esc.write(caso.getSexo() + ";");
      esc.write(caso.getFaixaEtaria() + ";");
      esc.write("I;");
      esc.write("2;");
      esc.write(caso.getDia() + "\n");
    }
    esc.close();
  } catch (Exception ex) {
    ex.printStackTrace();
  }
}
