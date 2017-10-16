private void criarDistribuicaoHumanos(String nomeArquivo) {
  final double percInsInfec = 0.03; // Porcentagem de casos utilizados
  Map<String, Double> percPop = new HashMap<>();
  percPop.put("C", 0.51); // Fracao Criancas Masculinos
  percPop.put("J", 0.50); // Fracao Jovens Masculinos
  percPop.put("A", 0.48); // Fracao Adultos Masculinos
  percPop.put("I", 0.45); // Fracao Idosos Masculinos
  percPop = Collections.unmodifiableMap(percPop);

  try {
    BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivo));
    Map<Integer, List<CasoInfeccao>> dados = leitor.lines()
        .map(i -> new CasoInfeccao(i.split(";")))
        .collect(Collectors.groupingBy(CasoInfeccao::getDia));
    leitor.close();

    for (Entry<Integer, List<CasoInfeccao>> entry : dados.entrySet()) {
      Integer key = entry.getKey();
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
        .flatMap(i -> i.entrySet().stream())
        .flatMap(i -> i.getValue().stream()).collect(Collectors.toList());
    for (CasoInfeccao caso : dados3) {
      Ponto ponto = pontos2.parallelStream()
          .collect(Collectors.minBy((i, j) -> Double.compare(
              Point.distance(i.x, i.y, Double.parseDouble(caso.getX()),
                  Double.parseDouble(caso.getY())),
              Point.distance(j.x, j.y, Double.parseDouble(caso.getX()),
                  Double.parseDouble(caso.getY())))))
          .get();
      caso.setX(Integer.toString((int) ponto.x));
      caso.setY(Integer.toString((int) ponto.y));
      caso.setQuadra(indicesQuadras.get(ponto.quadra));
      caso.setLote(indicesLotes.get(ponto.quadra).get(ponto.lote));
    }

    BufferedWriter escritor = new BufferedWriter(
        new FileWriter("DistribuicaoHumanos.csv"));
    escritor.write(dados3.size() + "\n");
    escritor
        .write("Q;L;X;Y;Sexo;FaixaEtaria;SaudeDengue;SorotipoAtual;Ciclo\n");
    for (CasoInfeccao caso : dados3) {
      escritor.write(caso.getQuadra() + ";");
      escritor.write(caso.getLote() + ";");
      escritor.write(caso.getX() + ";");
      escritor.write(caso.getY() + ";");
      escritor.write(caso.getSexo() + ";");
      escritor.write(caso.getFaixaEtaria() + ";");
      escritor.write("I;");
      escritor.write("2;");
      escritor.write(caso.getDia() + "\n");
    }
    escritor.close();
  } catch (Exception ex) {
    ex.printStackTrace();
  }
}
