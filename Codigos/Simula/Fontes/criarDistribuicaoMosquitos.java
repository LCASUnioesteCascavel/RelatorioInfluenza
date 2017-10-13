private void criarDistribuicaoMosquitos() {
  List<List<String>> pop = new ArrayList<>();

  pop.add(
      Arrays.asList("G9-F1", "0500", "M", "O", "0.02", "0.03", "2", "100"));
  pop.add(
      Arrays.asList("G9-F1", "1500", "M", "A", "0.02", "0.03", "2", "100"));
  pop.add(
      Arrays.asList("G9-F1", "0500", "F", "O", "0.02", "0.03", "2", "100"));
  pop.add(
      Arrays.asList("G9-F1", "1500", "F", "A", "0.02", "0.03", "2", "100"));

  pop.add(
      Arrays.asList("G9-F2", "0500", "M", "O", "0.02", "0.03", "2", "100"));
  pop.add(
      Arrays.asList("G9-F2", "1500", "M", "A", "0.02", "0.03", "2", "100"));
  pop.add(
      Arrays.asList("G9-F2", "0500", "F", "O", "0.02", "0.03", "2", "100"));
  pop.add(
      Arrays.asList("G9-F2", "1500", "F", "A", "0.02", "0.03", "2", "100"));

  pop.add(
      Arrays.asList("G8-F3", "0500", "M", "O", "0.02", "0.03", "2", "100"));
  pop.add(
      Arrays.asList("G8-F3", "1500", "M", "A", "0.02", "0.03", "2", "100"));
  pop.add(
      Arrays.asList("G8-F3", "0500", "F", "O", "0.02", "0.03", "2", "200"));
  pop.add(
      Arrays.asList("G8-F3", "1500", "F", "A", "0.02", "0.03", "2", "200"));

  pop.add(
      Arrays.asList("G8-F4", "0500", "M", "O", "0.02", "0.03", "2", "200"));
  pop.add(
      Arrays.asList("G8-F4", "1500", "M", "A", "0.02", "0.03", "2", "200"));
  pop.add(
      Arrays.asList("G8-F4", "0500", "F", "O", "0.02", "0.03", "2", "200"));
  pop.add(
      Arrays.asList("G8-F4", "1500", "F", "A", "0.02", "0.03", "2", "200"));

  pop.add(
      Arrays.asList("G8-F2", "0300", "M", "O", "0.01", "0.02", "2", "200"));
  pop.add(
      Arrays.asList("G8-F2", "1200", "M", "A", "0.01", "0.02", "2", "200"));
  pop.add(
      Arrays.asList("G8-F2", "0300", "F", "O", "0.01", "0.02", "2", "200"));
  pop.add(
      Arrays.asList("G8-F2", "1200", "F", "A", "0.01", "0.02", "2", "200"));

  pop.add(
      Arrays.asList("G9-F3", "0200", "M", "O", "0.00", "0.01", "2", "300"));
  pop.add(
      Arrays.asList("G9-F3", "0800", "M", "A", "0.00", "0.01", "2", "300"));
  pop.add(
      Arrays.asList("G9-F3", "0200", "F", "O", "0.00", "0.01", "2", "300"));
  pop.add(
      Arrays.asList("G9-F3", "0800", "F", "A", "0.00", "0.01", "2", "300"));

  pop.add(
      Arrays.asList("G9-F4", "0200", "M", "O", "0.00", "0.01", "2", "300"));
  pop.add(
      Arrays.asList("G9-F4", "0800", "M", "A", "0.00", "0.01", "2", "300"));
  pop.add(
      Arrays.asList("G9-F4", "0200", "F", "O", "0.00", "0.01", "2", "300"));
  pop.add(
      Arrays.asList("G9-F4", "0800", "F", "A", "0.00", "0.01", "2", "300"));

  pop.add(
      Arrays.asList("G9-F5", "0200", "M", "O", "0.00", "0.01", "2", "300"));
  pop.add(
      Arrays.asList("G9-F5", "0800", "M", "A", "0.00", "0.01", "2", "300"));
  pop.add(
      Arrays.asList("G9-F5", "0200", "F", "O", "0.00", "0.01", "2", "400"));
  pop.add(
      Arrays.asList("G9-F5", "0800", "F", "A", "0.00", "0.01", "2", "400"));

  pop.add(
      Arrays.asList("G9-F6", "0200", "M", "O", "0.00", "0.01", "2", "400"));
  pop.add(
      Arrays.asList("G9-F6", "0800", "M", "A", "0.00", "0.01", "2", "400"));
  pop.add(
      Arrays.asList("G9-F6", "0200", "F", "O", "0.00", "0.01", "2", "400"));
  pop.add(
      Arrays.asList("G9-F6", "0800", "F", "A", "0.00", "0.01", "2", "400"));

  pop.add(
      Arrays.asList("G8-F1", "0200", "M", "O", "0.00", "0.01", "2", "400"));
  pop.add(
      Arrays.asList("G8-F1", "0800", "M", "A", "0.00", "0.01", "2", "400"));
  pop.add(
      Arrays.asList("G8-F1", "0200", "F", "O", "0.00", "0.01", "2", "400"));
  pop.add(
      Arrays.asList("G8-F1", "0800", "F", "A", "0.00", "0.01", "2", "400"));

  try {
    BufferedWriter esc = new BufferedWriter(
        new FileWriter("DistribuicaoMosquitos.csv"));

    esc.write(pop.size() + "\n");
    esc.write("Regiao;QuantidadeTotal;Sexo;Fase;PercentualMinimoInfectados;");
    esc.write("PercentualMaximoInfectados;Sorotipo;Ciclo\n");
    for (List<String> i : pop) {
      esc.write(indicesQuadras.get(i.get(0)) + ";");
      esc.write(i.stream().skip(1).map(j -> j.toString())
          .collect(Collectors.joining(";")) + "\n");
    }

    esc.close();

  } catch (Exception ex) {
    ex.printStackTrace();
  }
}
