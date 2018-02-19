#include "Parametros.h"
#include "Fontes/Macros/0_SIM.h"
#include "Fontes/Macros/MacrosSO.h"

/*
  Construtor da classe Parametros. 

  A variavel "entradaMC" armazena o caminho para a pasta de entrada contendo os 
  arquivos de configuracao da simulacao. 

  O metodo "lerParametros" e responsavel por realizar a leitura dos arquivos 
  de configuracao e armazenar os parametros em estruturas de dados. 

  O metodo "toGPU" realiza a cipia dos parametros lidos para a GPU. 
*/
Parametros::Parametros(string entradaMC) {
  this->entradaMC = entradaMC;

  lerParametros();

  nSims = QUANTIDADE_SIMULACOES;
  nCiclos = QUANTIDADE_CICLOS + 1;

  toGPU();
}

/*
  Metodo responsavel pela obtencao do consumo de memiria da classe Parametros. 
*/
int Parametros::getMemoriaGPU() {
  return (nParametros * sizeof(double));
}

/*
  Destrutor da classe Parametros. 

  Sao desalocados o vetor de parametros da memiria principal e da GPU. 
*/
Parametros::~Parametros() {
  delete[](parametros);
  delete(parametrosDev); 
}

/*
  Metodo responsavel pela cipia dos dados da classe Parametros para a GPU. 
*/
void Parametros::toGPU() {
  parametrosDev = new DVector<double>(parametros, parametros + nParametros);

  PparametrosDev = raw_pointer_cast(parametrosDev->data());
}

/*
  Metodo responsavel pela leitura de um arquivo de configuracao da simulacao. 
  Como todos os arquivos de entrada, contidos nas pastas 
  "Entradas/MonteCarlo_{1}/Humanos" e "Entradas/MonteCarlo_{1}/Simulacao" 
  tem a mesma estrutura, somente um metodo e necessario. 

  Ignorando a linha de cabecalho, cada linha do arquivo consiste em um 
  parametro de simulacao. Cada linha contem quatro atributos:

  "Codigo": Cidigo unico do parametro. Nao e lido nem armazenado pela classe.
  "Min": Valor minimo que o parametro pode assumir. 
  "Max": Valor maximo que o parametro pode assumir. 
  "Descricao": Descricao textual do parametro. Nao e lido nem armazenado pela 
  classe e possui a unica finalidade de auxiliar na alteracao de parametros 
  diretamente pelos arquivos de configuracao. 
*/
void Parametros::lerArquivo(string pasta, string nomeArquivo, 
                            int& i, int nPar) {
  string entrada = entradaMC;
  entrada += pasta;
  entrada += SEP;
  entrada += nomeArquivo;
  
  fstream arquivo(entrada);
  if (not arquivo.is_open()) {
    cerr << "Arquivo: ";
    cerr << entrada;
    cerr << " nao foi aberto!" << endl;
    exit(1);
  }

  arquivo.ignore(sMax, '\n');
  for (int j = 0; j < nPar; ++j) {
    arquivo.ignore(7, EOF);
    arquivo >> parametros[i];
    arquivo.get();
    i += 1;

    arquivo >> parametros[i];
    arquivo.get();
    i += 1;

    arquivo.ignore(sMax, '\n');
  }
  arquivo.close();
}

/*
  Metodo responsavel pela leitura de todos os arquivos de parametros. 

  Sao lidos os arquivos contidos nas pastas "Entradas/MonteCarlo_{1}/Humanos" 
  e "Entradas/MonteCarlo_{1}/Simulacao" por meio das chamadas ao metodo 
  "lerArquivo". Os arquivos lidos sao: 
  "Entradas/MonteCarlo_{1}/Simulacao/0-SIM.csv";
  "Entradas/MonteCarlo_{1}/Humanos/0-INI.csv";
  "Entradas/MonteCarlo_{1}/Humanos/1-MOV.csv";
  "Entradas/MonteCarlo_{1}/Humanos/2-CON.csv";
  "Entradas/MonteCarlo_{1}/Humanos/3-TRA.csv";;

  Todos os parametros sao armazenados em um unico vetor, facilitando a cipia 
  dos dados para a GPU e a manutencao de macros para acesso. 
*/
void Parametros::lerParametros() {
  nParametros = N_PAR;
  parametros = new double[nParametros]();

  int i = 0;
  lerArquivo("Simulacao", "0-SIM.csv", i, N_0_SIM);
  lerArquivo("Humanos", "0-INI.csv", i, N_0_INI_H);
  lerArquivo("Humanos", "1-MOV.csv", i, N_1_MOV_H);
  lerArquivo("Humanos", "2-CON.csv", i, N_2_CON_H);
  lerArquivo("Humanos", "3-TRA.csv", i, N_3_TRA_H);
}
