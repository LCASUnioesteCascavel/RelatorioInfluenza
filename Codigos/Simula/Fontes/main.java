public static void main(String[] args) {
  ConsultasBanco.build(ConsultasBanco.CASCAVEL);
  ConsultasBanco con = ConsultasBanco.getInstance();

  int nCasa = 1642;
  int nTrabalho = 410;
  int nLazer = 28;
  int nEstudo = 16;
  GeradorVetores ger = new GeradorVetores(nCasa, nTrabalho, nLazer, nEstudo);

  ger.pontos = con.getPontosLotesERuas();

  ger.criarIndexQuadraseLotes();

  ger.gerarIndexQuadras();

  ger.gerarVizinhancas();
  ger.gerarIndexVizinhancas();
  ger.gerarVetorVizinhancas();

  ger.gerarVetorPosicoes();
  ger.gerarIndexPosicoes();
  ger.gerarIndexPosicoesRegioes();

  ger.escreverArquivoVetores();

  ger.criarDistribuicaoAgentes();
}
