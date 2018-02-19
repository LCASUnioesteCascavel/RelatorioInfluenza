#include <iostream>
#include <string>

#include "Fontes/Macros/MacrosSO.h"
#include "Fontes/MonteCarlo.h"

using std::cout;
using std::endl;
using std::string;
using std::to_string;

/*
  Metodo responsavel por apresentar uma tela de ajuda com as opcoes 
  disponiveis por linha de comando. Estas posicoes sao: 

  "--help -h": mostra a tela de ajuda; 
  "--device -d": especifica a GPU para execucao da simulacao; 
  "--nmontecarlos -m": especifica a quantidade de simulacoes Monte Carlo.
*/
void help(string exe) {
  cout << "Uso: " << exe << " <opcoes>\n"
       << "Opcoes:\n"
       << "\t--help, -h\t\tMostra esta ajuda\n"
       << "\t--device, -d ID_DEVICE\tEspecifica a GPU para execucao\n"
       << "\t--nmontecarlos, -m QUANT_MCS\tEspecifica a quantidade de simulacoes MC\n";
}

/*
  Metodo inicial do programa. 

  Este metodo e responsavel por:
  - interpretar as opcoes passadas por linha de comando;
  - alterar a GPU utilizada para execucao;
  - excluir pasta de saida se ja existente;
  - iniciar a execucao das simulacoes Monte Carlo. 

  Valores padrao:
  - "idDevice": 0. Utilizara a primeira GPU encontrada. A id das GPUs pode 
                   ser vista com o utilitario "deviceQuery" presente no 
                   SDK do CUDA. 
  - "quantMCS": 1. Executara uma simulacao Monte Carlo com os arquivos de 
                   entrada presentes na pasta "Entradas/MonteCarlo_0". 
                   Se informado um numero maior que 1 serao executadas 
                   simulacoes utilizando os arquivos presentes nas pastas 
                   "Entradas/MonteCarlo_{1}", em que "{1}" designa o id da 
                   simulacao, iniciando em "0" ate "quantMCS - 1".
*/
int main(int argc, char **argv) {
  int idDevice = 0, quantMCs = 1;

  // Interpreta os argumentos passados por linha do comando, se existentes. 
  if (argc > 1) {
    for (int i = 1; i < argc; i += 2) {
      string textoOpcao(argv[i]);

      if (textoOpcao == "--help" or textoOpcao == "-h") {
        help(argv[0]);
        return 0;
      }
      if (textoOpcao == "--device" or textoOpcao == "-d") {
        idDevice = atoi(argv[i + 1]);
      } 
      if (textoOpcao == "--nmontecarlos" or textoOpcao == "-m") {
        quantMCs = atoi(argv[i + 1]);
      }
    }
  }

  // Altera a GPU que sera utilizada para execucao. 
  cudaSetDevice(idDevice);

  // Exclui a pasta de saida, se ja existente. 
  system((EXCLUIR_PASTA + string("Saidas")).c_str());

  string entrada, saida;
  for (int idMC = 0; idMC < quantMCs; idMC++) {
    // O caminho para a pasta de entrada sera "Entradas/MonteCarlo_{idMC}/"
    entrada = string("Entradas");
    entrada += SEP;
    entrada += string("MonteCarlo_");
    entrada += to_string(idMC);
    entrada += SEP;

    // O caminho para a pasta de saida sera "Saidas/MonteCarlo_{idMC}/"
    saida = string("Saidas");
    saida += SEP;
    saida += string("MonteCarlo_");
    saida += to_string(idMC);
    saida += SEP;

    // Inicia a execucao da simulacao tipo Monte Carlo. 
    MonteCarlo(entrada, saida);
  }

  return 0;
}
