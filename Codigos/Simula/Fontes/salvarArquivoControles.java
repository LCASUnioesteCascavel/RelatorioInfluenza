private void salvarArquivoControles() {
  Map<Integer, List<CasoInfeccao>> casosPorDia = distribuicaoHumanos.stream()
      .collect(Collectors.groupingBy(CasoInfeccao::getDia));

  int duracaoAno = Integer.parseInt(params.get("DuracaoAno"));

  for (int i = 0; i < duracaoAno; ++i) {
    if (!casosPorDia.containsKey(i)) {
      casosPorDia.put(i, new ArrayList<>());
    }
  }

  Map<Integer, Integer> casosAcumulados = new HashMap<>();

  int soma = casosPorDia.get(0).size();
  casosAcumulados.put(0, soma);
  for (int i = 1; i < casosPorDia.entrySet().size(); ++i) {
    soma += casosPorDia.get(i).size();
    casosAcumulados.put(i, soma);
  }

  double max = casosAcumulados.values().stream().max(Integer::compareTo)
      .get();

  Map<Integer, Double> percentuais = new HashMap<>();
  for (Entry<Integer, Integer> i : casosAcumulados.entrySet()) {
    double v = (max - i.getValue()) / max;
    percentuais.put(i.getKey(), v);
  }

  try {
    BufferedWriter esc = new BufferedWriter(
        new FileWriter(nomeArquivoControles));

    esc.write("fEVac. Faixas etarias que receberao vacinacao\n");
    esc.write(fEVac.size() + "\n");
    esc.write(fEVac.stream().collect(Collectors.joining(";")) + "\n\n");

    esc.write("cicVac. Ciclos em que as vacinacoes serao executadas\n");
    esc.write(cicVac.size() + "\n");
    esc.write(cicVac.stream().collect(Collectors.joining(";")) + "\n\n");

    esc.write("Sazonalidade\n");
    esc.write(percentuais.size() + "\n");
    esc.write(percentuais.entrySet().stream()
        .map(i -> String.format("%.10f", i.getValue()).replace(",", "."))
        .collect(Collectors.joining(";")) + "\n\n");

    ArrayList<Double> percentuaisQuarentena = new ArrayList<>();
    for (int i = 0; i < Integer.parseInt(params.get("DuracaoAno")); ++i) {
      percentuaisQuarentena.add(Math.random());
    }

    esc.write("Percentuais de quarentena\n");
    esc.write(percentuaisQuarentena.size() + "\n");
    esc.write(percentuaisQuarentena.stream()
        .map(i -> String.format("%.10f", i).replace(",", "."))
        .collect(Collectors.joining(";")) + "\n");

    esc.close();

  } catch (Exception ex) {
    ex.printStackTrace();
  }
}
