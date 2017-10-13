private void escreverArquivoVetores() {
  try {
    BufferedWriter esc = new BufferedWriter(new FileWriter("0-AMB.csv"));

    esc.write("quantQuadras, quantLotes e indexQuadras\n");
    esc.write(pontos.keySet().size() + "\n");
    esc.write(pontos.entrySet().stream().sorted(Entry.comparingByKey())
        .map(i -> Integer.toString(i.getValue().size()))
        .collect(Collectors.joining(";")) + "\n");
    esc.write(indexQuadras.stream().map(i -> i.toString())
        .collect(Collectors.joining(";")) + "\n\n");

    esc.write("indexVizinhancas e vetorVizinhancas\n");
    // esc.write(indexVizinhancas.stream().map(i -> i.toString())
    // .collect(Collectors.joining(";")) + "\n");
    // esc.write(vetorVizinhancas.stream().map(i -> i.toString())
    // .collect(Collectors.joining(";")) + "\n\n");

    for (int i = 0; i < indexVizinhancas.size(); ++i) {
      esc.write(Integer.toString(indexVizinhancas.get(i)));
      if (i < indexVizinhancas.size() - 1) {
        esc.write(";");
      }
    }
    esc.write("\n");

    for (int i = 0; i < vetorVizinhancas.size(); ++i) {
      esc.write(Integer.toString(vetorVizinhancas.get(i)));
      if (i < vetorVizinhancas.size() - 1) {
        esc.write(";");
      }
    }
    esc.write("\n\n");

    esc.write("indexPosicoes, vetorPosicoes, indexPosicoesRegioes\n");
    esc.write(indexPosicoes.stream().map(i -> i.toString())
        .collect(Collectors.joining(";")) + "\n");
    // esc.write(vetorPosicoes.stream().map(i -> i.toString())
    // .collect(Collectors.joining(";")) + "\n");

    for (int i = 0; i < vetorPosicoes.size(); ++i) {
      esc.write(Integer.toString(vetorPosicoes.get(i)));
      if (i < vetorPosicoes.size() - 1) {
        esc.write(";");
      }
    }
    esc.write("\n");

    esc.write(indexPosicoesRegioes.stream().map(i -> i.toString())
        .collect(Collectors.joining(";")) + "\n\n");

    esc.write("indexFronteiras e vetorFronteiras\n");
    esc.write(indexFronteiras.stream().map(i -> i.toString())
        .collect(Collectors.joining(";")) + "\n");
    esc.write(vetorFronteiras.stream().map(i -> i.toString())
        .collect(Collectors.joining(";")) + "\n\n");

    esc.write("indexEsquinas e vetorEsquinas\n");
    esc.write(indexEsquinas.stream().map(i -> i.toString())
        .collect(Collectors.joining(";")) + "\n");
    esc.write(vetorEsquinas.stream().map(i -> i.toString())
        .collect(Collectors.joining(";")) + "\n\n");

    esc.write("indexCentrosEsquinas e vetorCentrosEsquinas\n");
    esc.write(indexCentrosEsquinas.stream().map(i -> i.toString())
        .collect(Collectors.joining(";")) + "\n");
    esc.write(vetorCentrosEsquinas.stream().map(i -> i.toString())
        .collect(Collectors.joining(";")) + "\n");

    esc.close();

    esc = new BufferedWriter(new FileWriter("1-MOV.csv"));

    esc.write("quantRotas, indexRotas e vetorRotas\n");
    esc.write(nRotas + "\n");
    // esc.write(indexRotas.stream().map(i -> i.toString())
    // .collect(Collectors.joining(";")) + "\n");
    // esc.write(vetorRotas.stream().map(i -> i.toString())
    // .collect(Collectors.joining(";")) + "\n\n");

    for (int i = 0; i < indexRotas.size(); ++i) {
      esc.write(Integer.toString(indexRotas.get(i)));
      if (i < indexRotas.size() - 1) {
        esc.write(";");
      }
    }
    esc.write("\n");

    for (int i = 0; i < vetorRotas.size(); ++i) {
      esc.write(Integer.toString(vetorRotas.get(i)));
      if (i < vetorRotas.size() - 1) {
        esc.write(";");
      }
    }
    esc.write("\n\n");

    esc.write("quantTrajetos, indexTrajetos e vetorTrajetos\n");
    esc.write(nTrajetos + "\n");
    esc.write(indexTrajetos.stream().map(i -> i.toString())
        .collect(Collectors.joining(";")) + "\n");
    esc.write(vetorTrajetos.stream().map(i -> i.toString())
        .collect(Collectors.joining(";")) + "\n\n");

    esc.write("indexPeriodos e vetorPeriodos\n");
    // esc.write(indexPeriodos.stream().map(i -> i.toString())
    // .collect(Collectors.joining(";")) + "\n");
    // esc.write(vetorPeriodos.stream().map(i -> i.toString())
    // .collect(Collectors.joining(";")) + "\n\n");

    for (int i = 0; i < indexPeriodos.size(); ++i) {
      esc.write(Integer.toString(indexPeriodos.get(i)));
      if (i < indexPeriodos.size() - 1) {
        esc.write(";");
      }
    }
    esc.write("\n");

    for (int i = 0; i < vetorPeriodos.size(); ++i) {
      esc.write(Integer.toString(vetorPeriodos.get(i)));
      if (i < vetorPeriodos.size() - 1) {
        esc.write(";");
      }
    }
    esc.write("\n\n");

    esc.write("indexTrajetosFaixaEtaria\n");
    esc.write(indexTrajetosFaixaEtaria.stream().map(i -> i.toString())
        .collect(Collectors.joining(";")) + "\n");

    esc.close();

  } catch (Exception ex) {
    ex.printStackTrace();
  }
}
