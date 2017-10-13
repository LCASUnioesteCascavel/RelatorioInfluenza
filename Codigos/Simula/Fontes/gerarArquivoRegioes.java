private void gerarArquivoRegioes() {
  List<String> vac = Arrays.asList("Q53A", "Q53B", "Q53C", "Q53D", "Q53E",
      "Q53F", "Q53G", "Q55", "Q56A", "Q56B", "Q56C");

  List<String> fEVac = Arrays.asList("1", "2");
  List<String> perVac = Arrays.asList("7", "0");
  List<String> cicVac = Arrays.asList("150", "300", "450");

  List<String> raio = Arrays.asList("Q100", "Q111", "Q95", "Q96", "Q97",
      "Q98", "Q99");
  List<String> bloq = Arrays.asList("Q45A", "Q48A", "Q48B", "Q75");
  List<String> conBio = Arrays.asList("Q35A", "Q35B", "Q41A", "Q41B", "Q57A",
      "Q57B");
  List<String> conAmb = Arrays.asList("Q15A", "Q15B", "Q16A", "Q16B");

  List<List<String>> ponEst = new ArrayList<>();
  ponEst.add(Arrays.asList("Q19", "08"));
  ponEst.add(Arrays.asList("Q21B", "15-17"));
  ponEst.add(Arrays.asList("Q25D", "10"));
  ponEst.add(Arrays.asList("Q26B", "53"));
  ponEst.add(Arrays.asList("Q31B", "08"));
  ponEst.add(Arrays.asList("Q32C", "02"));
  ponEst.add(Arrays.asList("Q46A", "28"));
  ponEst.add(Arrays.asList("Q47A", "48"));
  ponEst.add(Arrays.asList("Q48B", "23A"));
  ponEst.add(Arrays.asList("Q53A", "08"));
  ponEst.add(Arrays.asList("Q54D", "11-U-5"));
  ponEst.add(Arrays.asList("Q56C", "26"));
  ponEst.add(Arrays.asList("Q63", "09"));
  ponEst.add(Arrays.asList("Q99", "07"));

  try {
    BufferedWriter esc = new BufferedWriter(new FileWriter("2-CON.csv"));

    esc.write("quadVac. Quadras em que serao aplicadas a vacinacao\n");
    esc.write(vac.size() + "\n");
    esc.write(vac.stream().map(i -> Integer.toString(indicesQuadras.get(i)))
        .collect(Collectors.joining(";")) + "\n\n");

    esc.write("fEVac. Faixas etarias que receberao vacinacao\n");
    esc.write(fEVac.size() + "\n");
    esc.write(fEVac.stream().collect(Collectors.joining(";")) + "\n\n");

    esc.write(
        "perVac. Periodo da realizacao da vacinacao. Contador auxiliar\n");
    esc.write(perVac.size() + "\n");
    esc.write(perVac.stream().collect(Collectors.joining(";")) + "\n\n");

    esc.write("cicVac. Ciclos em que as vacinacoes serao executadas\n");
    esc.write(cicVac.size() + "\n");
    esc.write(cicVac.stream().collect(Collectors.joining(";")) + "\n\n");

    esc.write("raio. Quadras com raio\n");
    esc.write(raio.size() + "\n");
    esc.write(raio.stream().map(i -> Integer.toString(indicesQuadras.get(i)))
        .collect(Collectors.joining(";")) + "\n\n");

    esc.write("bloq. Quadras com bloqueio\n");
    esc.write(bloq.size() + "\n");
    esc.write(bloq.stream().map(i -> Integer.toString(indicesQuadras.get(i)))
        .collect(Collectors.joining(";")) + "\n\n");

    esc.write("conBio. Quadras com controle biologico\n");
    esc.write(conBio.size() + "\n");
    esc.write(
        conBio.stream().map(i -> Integer.toString(indicesQuadras.get(i)))
            .collect(Collectors.joining(";")) + "\n\n");

    esc.write("conAmb. Quadras com controle ambiental\n");
    esc.write(conAmb.size() + "\n");
    esc.write(
        conAmb.stream().map(i -> Integer.toString(indicesQuadras.get(i)))
            .collect(Collectors.joining(";")) + "\n\n");

    esc.write("pontEst. Lotes que sao pontos estrategicos\n");
    esc.write((ponEst.size() * 2) + "\n");
    esc.write(ponEst.stream()
        .flatMap(
            i -> Stream.of(Integer.toString(indicesQuadras.get(i.get(0))),
                Integer.toString(indicesLotes.get(i.get(0)).get(i.get(1)))))
        .collect(Collectors.joining(";")) + "\n");

    esc.close();

  } catch (Exception ex) {
    ex.printStackTrace();
  }
}
