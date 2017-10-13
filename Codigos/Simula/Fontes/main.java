public static void main(String[] args) {
  ConsultasBanco.build(ConsultasBanco.CASCAVEL);
  ConsultasBanco con = ConsultasBanco.getInstance();

  int nCasa = 1642;
  int nTrabalho = 410;
  int nLazer = 28;
  int nEstudo = 16;
  GeradorVetores ger = new GeradorVetores(nCasa, nTrabalho, nLazer, nEstudo);

  ger.pontos = con.getPontosLotesERuas();
  ger.esquinas = con.getPontosEsquinas();
  ger.centroidesLotes = con.getCentroidesLotes();
  ger.arestas = con.getArestas();
  ger.centroidesEsquinas = con.getCentroidesEsquinas();
  ger.lotes = con.getLotes();
  ger.vertices = con.getVertices();

  ger.criarIndexQuadraseLotes();

  ger.gerarIndexQuadras();

  ger.gerarVizinhancas();
  ger.gerarIndexVizinhancas();
  ger.gerarVetorVizinhancas();

  ger.gerarVetorPosicoes();
  ger.gerarIndexPosicoes();
  ger.gerarIndexPosicoesRegioes();

  ger.escreverArquivoVetoresReduzido();

  ger.gerarFronteiras();
  ger.gerarIndexFronteiras();
  ger.gerarVetorFronteiras();

  ger.gerarIndexEsquinas();
  ger.gerarVetorEsquinas();

  ger.gerarIndexCentrosEsquinas();
  ger.gerarVetorCentrosEsquinas();

  ger.distribuirLocais();

  ger.gerarTrajetos();

  ger.gerarCaminhos();

  ger.gerarVetorIndexRotas();
  ger.gerarVetorIndexTrajetos();
  ger.gerarVetorIndexPeriodos();
  ger.gerarIndexTrajetosFaixaEtaria();

  ger.escreverArquivoVetores();
  ger.criarDistribuicaoMosquitos();
  ger.criarDistribuicaoHumanos("Sazonalidade_3.csv");
  ger.gerarArquivoRegioes();
}
