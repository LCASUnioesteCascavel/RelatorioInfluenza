#include <iostream>
#include <string>

#include "Fontes/Macros/MacrosSO.h"
#include "Fontes/MonteCarlo.h"

using std::cout;
using std::endl;
using std::string;
using std::to_string;

void help(string exe) {
  cout << "Uso: " << exe << " <opcoes>\n"
       << "Opcoes:\n"
       << "\t-h\t\tMostra esta ajuda\n"
       << "\t-d ID_DEVICE\tEspecifica a GPU para execucao\n"
       << "\t-m QUANT_MCS\tEspecifica a quantidade de simulacoes MC\n";
}

int main(int argc, char **argv) {
  int idDevice = 0, quantMCs = 1;

  if (argc > 1) {
    for (int i = 1; i < argc; i += 2) {
      string textoOpcao(argv[i]);

      if (textoOpcao == "-h") {
        help(argv[0]);
        return 0;
      }
      if (textoOpcao == "-d") {
        idDevice = atoi(argv[i + 1]);
      } 
      if (textoOpcao == "-m") {
        quantMCs = atoi(argv[i + 1]);
      }
    }
  }

  cudaSetDevice(idDevice);

  system((EXCLUIR_PASTA + string("Saidas")).c_str());

  string entrada, saida;
  for (int idMC = 0; idMC < quantMCs; idMC++) {
    entrada = string("Entradas");
    entrada += SEP;
    entrada += string("MonteCarlo_");
    entrada += to_string(idMC);
    entrada += SEP;

    saida = string("Saidas");
    saida += SEP;
    saida += string("MonteCarlo_");
    saida += to_string(idMC);
    saida += SEP;

    MonteCarlo(entrada, saida);
  }

  return 0;
}
