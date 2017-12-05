#!/usr/bin/python3

import numpy as np
from sys import argv


N_SIMS = 4
N_PONTOS = 141774
N_CICLOS = 214


def ler_arquivo(entrada, dados):
  with open(entrada, 'r') as arquivo:
    for i, linha in enumerate(arquivo):
      for j, d in enumerate(linha.split(';')[:-1]):
        if j in [0, 1]: 
          dados[i][j] = int(d)
        elif d.endswith('3'): 
          dados[i][j] += 1


def salvar_arquivo(saida, dados):
  for i, linha in enumerate(dados):
    for j, d in enumerate(linha):
      if j in [0, 1]: 
        continue
      if d == N_SIMS / 2: 
        dados[i][j] = 2034
      else: 
        dados[i][j] = int(d > N_SIMS / 2)
      if dados[i][j] == 1: 
        dados[i][j] = 2033
  
  with open(saida, 'w') as arquivo:
    for linha in dados:
      arquivo.write(';'.join(str(i) for i in linha))
      arquivo.write(';\n')


def main():
  if len(argv) == 2:
    entrada = argv[1]
  
    dados = np.zeros((N_PONTOS, N_CICLOS + 3), dtype=np.int32)
    for i in range(N_SIMS):
      arquivo = entrada + '/Simulacao_{}/Espacial_Geo_Infectantes_Acumulado.csv'.format(i)
      print('Lendo arquivo {}...'.format(arquivo))
      ler_arquivo(arquivo, dados)
    
    arquivo = entrada + '/Espacial_Geo_Media.csv'
    print('Salvando arquivo {}...'.format(arquivo))
    salvar_arquivo(arquivo, dados)
  else:
    msg = 'Uso:\n'
    msg += '\tpython {} pasta'.format(__file__)
    print(msg)


if __name__ == '__main__': main()
