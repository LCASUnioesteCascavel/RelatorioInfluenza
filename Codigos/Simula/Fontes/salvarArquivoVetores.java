private void salvarArquivoVetores() {
  try {
    BufferedWriter esc = new BufferedWriter(
        new FileWriter(nomrArquivoAmbiental));

    esc.write("quantQuadras, quantLotes e indexQuadras\n");
    esc.write(pontos.keySet().size() + "\n");
    esc.write(pontos.entrySet().stream().sorted(Entry.comparingByKey())
        .map(i -> Integer.toString(i.getValue().size()))
        .collect(Collectors.joining(";")) + "\n");
    esc.write(indexQuadras.stream().map(i -> i.toString())
        .collect(Collectors.joining(";")) + "\n\n");

    esc.write("indexVizinhancas e vetorVizinhancas\n");
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
    for (int i = 0; i < vetorPosicoes.size(); ++i) {
      esc.write(Integer.toString(vetorPosicoes.get(i)));
      if (i < vetorPosicoes.size() - 1) {
        esc.write(";");
      }
    }
    esc.write("\n");

    esc.close();

  } catch (Exception ex) {
    ex.printStackTrace();
  }
}
