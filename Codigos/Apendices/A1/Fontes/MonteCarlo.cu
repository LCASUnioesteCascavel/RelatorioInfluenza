#include "MonteCarlo.h"
#include "Fontes/Macros/MacrosSO.h"
#include "Fontes/Parametros.h"
#include "Fontes/Ambiente.h"
#include "Fontes/Saidas.h"
#include "Fontes/Simulacao.h"

/*
  Construtor da classe MonteCarlo.

  Os argumentos do metodo, "entradaMC" e "saidaMC", armazenam o caminho para as 
  pasta de entrada e saida desta simulacao Monte Carlo. A pasta de entrada e 
  utilizada na leitura de parametros e dados de inicializacao da simulacao. A 
  pasta de saida e utilizada a escrita de arquivos de saida contendo os 
  resultados da simulacao. 

  Este metodo realiza a criacao da pasta de saida da simulacao Monte Carlo, 
  leitura dos parametros de simulacao e da estrutura do ambiente, alocacao 
  da classe responsavel pela geracao dos arquivos de saida da simulacao e 
  inicializacao da execucao da simulacao. Adicionalmente sao exibidos em tela 
  as datas de inicio e final da execucao da simulacao, assim como o tempo 
  dispendido. 
*/
MonteCarlo::MonteCarlo(string entradaMC, string saidaMC) {
  this->entradaMC = entradaMC;
  this->saidaMC = saidaMC;

  // Criacao da pasta de saida. 
  system((CRIAR_PASTA + saidaMC).c_str());

  // Leitura dos parametros e do ambiente de simulacao e alocacao das saidas.
  parametros = new Parametros(entradaMC);
  ambiente = new Ambiente(entradaMC);
  saidas = new Saidas(ambiente, parametros, saidaMC);

  // Mostra e coleta a data e hora do inicio da execucao da simulacao. 
  exibirData();
  auto t1 = high_resolution_clock::now();

  // Inicia a execucao da simulacao tipo Monte Carlo. 
  iniciar();

  // Mostra e coleta a data e hora do final da execucao da simulacao. 
  auto t2 = high_resolution_clock::now(); 
  exibirData();

  // Exibe em tela o tempo dispendido na execucao da simulacao tipo Monte Carlo. 
  cout << duration_cast<duration<double>>(t2 - t1).count() << "s" << endl;
}

/*
  Destrutor da classe MonteCarlo.

  Sao desalocadas as estruturas que armazenam os parametros, o ambiente e as 
  saidas da simulacao tipo Monte Carlo. 
*/
MonteCarlo::~MonteCarlo() {
  delete(parametros); delete(ambiente); delete(saidas);
}

/*
  Metodo responsavel pela inicializacao da execucao da simulacao Monte Carlo. 

  A variavel "saidaSim" armazena o caminho para a pasta de saida da simulacao 
  individual, que pertence a uma simulacao tipo Monte Carlo. 
  
  O parametro "parametros->nSims" define a quantidade de simulacoes individuais 
  que serao executadas e utilizadas para compor uma simulacao tipo Monte Carlo. 

  O metodo "saidas->limparEspaciais" e utilizado para limpar as saidas espaciais 
  entre a execucao de simulacoes individuais. Sem este metodo, as saidas 
  espaciais acumulam de uma execucao para outra, gerando resultados incorretos. 

  O metodo "saidas->salvarPopulacoes" e responsavel por salvar os resultados 
  da simulacao nos respectivos arquivos de saida. 
*/
void MonteCarlo::iniciar() {
  string saidaSim;
  for (int idSim = 0; idSim < parametros->nSims; ++idSim) {
    // A pasta da saida da simulacao individual sera 
    // "Entradas/MonteCarlo_{idMC}/Simulacao_{idSim}/". 
    saidaSim = saidaMC;
    saidaSim += string("Simulacao_");
    saidaSim += to_string(idSim);
    saidaSim += SEP;

    // Inicia a execucao da simulacao individual. 
    Simulacao(idSim, saidaSim, saidas, parametros, ambiente);

    // Limpa saidas espaciais. 
    saidas->limparEspaciais();
  }
  // Salva saidas populacionais da simulacao tipo Monte Carlo.
  saidas->salvarPopulacoes();
}

/*
  Metodo responsavel por obter e formatar a data atual a exibicao em tela. 
*/
void MonteCarlo::exibirData() {
  time_t data = system_clock::to_time_t(system_clock::now());
  cout << put_time(localtime(&data), "%d/%m/%Y %H:%M:%S") << endl;
}
