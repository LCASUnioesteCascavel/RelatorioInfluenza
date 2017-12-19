#include <iostream>
#include <fstream>
#include <string>
#include <cstdlib>
#include <algorithm>
#include <tuple>
#include <sstream>
#include <vector>
#include <functional>
#include <omp.h>

using namespace std;

vector<vector<int>> lerCSV(string nomeArquivo) {
  ifstream arquivo;
  arquivo.open(nomeArquivo);
  if (not arquivo.is_open()) {
    cout << "Arquivo: ";
    cout << nomeArquivo;
    cout << " nao foi aberto!" << endl;
    exit(1);
  }
  
  vector<vector<int>> dados;
  
  string linha;
  int i = 0, j, cols, dado;
  while (getline(arquivo, linha)) {
    cols = count(linha.begin(), linha.end(), ';');
    
    stringstream linha2(linha);
    
    dados.push_back(vector<int>());
    
    for (j = 0; j < cols; ++j) {
      linha2 >> dado;
      linha2.get();
    
      dados[i].push_back(dado);
    }
    
    i++;
  }
  
  arquivo.close();
  
  return dados;
}

void escreverCSV(string nomeArquivo, vector<vector<int>> dados) {
  ofstream arquivo;
  arquivo.open(nomeArquivo);
  if (not arquivo.is_open()) {
    cout << "Arquivo: ";
    cout << nomeArquivo;
    cout << " nao foi aberto!" << endl;
    exit(1);
  }
  
  for (auto&& v: dados) {
    for (int& d : v) {
      arquivo << d;
      arquivo << ";";
    }
    arquivo << endl;
  }
  
  arquivo.close();
}

int main(int argc, char **argv) {  
  const int N_SIM = 10;
  const int N_MC = 1;
  int mcs[N_MC] = {0};
  
  #pragma omp parallel for
  for (int i = 0; i < N_MC; ++i) {
    int mc = mcs[i];

    cout << "MC: " << mc << endl;
    
    vector<vector<int>> media;
    
    for (int j = 0; j < N_SIM; ++j) {      
      string arquivoEntrada = string("Saidas/");
      arquivoEntrada += string("MonteCarlo_") + to_string(mc) + string("/");
      arquivoEntrada += string("Simulacao_") + to_string(j) + string("/");
      arquivoEntrada += string("Espacial_Novo_Humanos_Infectantes.csv");
      
      vector<vector<int>> dados = lerCSV(arquivoEntrada);
      for (auto&& v : dados) {
        auto fn = [](int& d){return d % 10 == 4;};
        replace_if(v.begin() + 2, v.end(), fn, 1);
      }
      
      if (media.empty()) {
        media = dados;
      } else {
        for (int pos = 0; pos < dados.size(); ++pos) {
          transform(media[pos].begin() + 2, media[pos].end(), 
                    dados[pos].begin() + 2, 
                    media[pos].begin() + 2, 
                    plus<int>());
        }
      }
    }
    
    const int N_POS = media.size(); 
    const int N_CIC = media[0].size() - 2;
    
    for (int cic = 2; cic < N_CIC + 2; ++cic) {
      vector<tuple<int, int>> posicoes;
      
      int soma = 0;
      for (int pos = 0; pos < N_POS; ++pos) {
        posicoes.push_back(make_tuple(pos, media[pos][cic]));
        soma += media[pos][cic];
      }
      int n = soma / N_SIM;
      
      auto fn = [](tuple<int, int>& t1, tuple<int, int>& t2){
        return get<1>(t1) > get<1>(t2);
      };
      sort(posicoes.begin(), posicoes.end(), fn);
      
      n = ((n > posicoes.size()) ? posicoes.size() : n);
      
      posicoes.erase(posicoes.begin() + n, posicoes.end());
      
      for (int pos = 0; pos < N_POS; ++pos) {
        media[pos][cic] = 0;
      }
      
      for (int i = 0; i < n; ++i) {
        int ind = get<0>(posicoes[i]);
        media[ind][cic] = 1;
      }
    }
    
    for (auto&& v : media) {
      partial_sum(v.begin() + 2, v.end(), v.begin() + 2);
    }
    
    int sim = 1;
    for (int cic = 2; cic < N_CIC + 2; ++cic) {
      vector<tuple<int, int>> posicoes;
      
      int soma = 0;
      for (int pos = 0; pos < N_POS; ++pos) {
        posicoes.push_back(make_tuple(pos, media[pos][cic]));
        soma += media[pos][cic];
      }
      int n = soma / sim;      
      sim++;
      
      auto fn = [](tuple<int, int>& t1, tuple<int, int>& t2){
        return get<1>(t1) > get<1>(t2);
      };
      sort(posicoes.begin(), posicoes.end(), fn);
      
      n = ((n > posicoes.size()) ? posicoes.size() : n);
      
      posicoes.erase(posicoes.begin() + n, posicoes.end());
      
      for (int pos = 0; pos < N_POS; ++pos) {
        media[pos][cic] = 0;
      }
      
      for (int i = 0; i < n; ++i) {
        int ind = get<0>(posicoes[i]);
        media[ind][cic] = 1;
      }
    }
    
    for (auto&& v : media) {
      partial_sum(v.begin() + 2, v.end(), v.begin() + 2);
    }
    
    for (auto&& v : media) {
      auto fn = [](int& d){return d != 0;};
      replace_if(v.begin() + 2, v.end(), fn, 3034);
    }
    
    string arquivoSaida = string("Saidas/");
    arquivoSaida += string("MonteCarlo_") + to_string(mc) + string("/");
    arquivoSaida += "Espacial_Geo_Media.csv";
    escreverCSV(arquivoSaida, media); 
  }
  return 0;
}
