public static void main(String[] args) {
  this.params = params;

  this.pastaSaida = "Saidas" + File.separator + params.get("Ambiente") + "_"
      + params.get("Doenca") + File.separator;
  if (!new File(pastaSaida).exists()) {
    new File(pastaSaida).mkdir();
  }

  nomrArquivoAmbiental = pastaSaida + "0-AMB.csv";
  nomeArquivoControles = pastaSaida + "1-CON.csv";
  nomeArquivoDistribuicaoHumanos = pastaSaida + "DistribuicaoHumanos.csv";

  con = new ConsultasBanco(params.get("Ambiente"), params.get("Doenca"));
  pontos = con.getPontos();
  criarIndexQuadraseLotes();
  distribuicaoHumanos = con.getDistribuicaoHumanos();
  fEVac = con.getFaixasEtariasVacinacao();
  cicVac = con.getCiclosVacinacao();
  gerarIndexQuadras();

  System.out.println("Processando vizinhancas...");
  gerarVizinhancas();
  gerarIndexVizinhancas();
  gerarVetorVizinhancas();

  System.out.println("Processando posicoes...");
  gerarVetorPosicoes();
  gerarIndexPosicoes();

  System.out.println("Salvando arquivos...");
  salvarArquivoVetores();
  salvarArquivoDistribuicaoHumanos();
  salvarArquivoControles();
}
