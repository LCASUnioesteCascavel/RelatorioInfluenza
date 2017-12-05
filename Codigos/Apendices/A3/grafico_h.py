#!/usr/bin/env python3

from pathlib import Path
import matplotlib
import matplotlib.pyplot as plt
from multiprocessing import Process
import numpy as np

class GraficosQuantidadesHumanosAcumulado:
  
  def __init__(self, arquivo):
    self.arquivo = Path(arquivo)
    self.nome_arquivo = str(self.arquivo.absolute())
    if not self.arquivo.is_file():
      raise Exception('Arquivo invalido!')
    matplotlib.rcParams.update({'font.size': 9})
    self.dados = []
    with self.arquivo.open() as f:
      for linha in f:
        self.dados.append([int(i) for i in linha.split(';')])
  
  def criar_graficos(self):
    x = [int(i[0]) for i in self.dados]
    i = [sum(i[3::6]) for i in self.dados]
    i = np.cumsum(i)
    
    plt.title('Humanos Infectados Acumulados')
    plt.plot(x, i, color="red", linewidth=2)
    params_leg = {'loc': 'lower center', 'fontsize': 9, 
                  'ncol': 1, 'fancybox': True, 'shadow': True}
    plt.legend(**params_leg)
    params_fig = {'bbox_inches': 'tight', 
                  'dpi': 400, 
                  'format': 'png'}
    plt.axis('tight')
    plt.savefig(self.nome_arquivo.replace('.csv', '.png'), **params_fig)
    plt.close()


class GraficosQuantidadesHumanos:

  def __init__(self, arquivo):
    self.arquivo = Path(arquivo)
    self.nome_arquivo = str(self.arquivo.absolute())
    if not self.arquivo.is_file():
      raise Exception('Arquivo invalido!')
    matplotlib.rcParams.update({'font.size': 9})
    self.dados = []
    with self.arquivo.open() as f:
      for linha in f:
        self.dados.append([int(i) for i in linha.split(';')])

  def conf_uni_graf(self, nome_imagem, cols):
    plt.axis('tight')    
    plot = plt.subplot()
    box = plot.get_position()
    plot.set_position([box.x0, box.y0 + box.height * 0.2,
                       box.width, box.height * 0.8])
    params_leg = {'loc': 'upper center', 
                  'bbox_to_anchor': (0.5, -0.1), 
                  'fancybox': True, 
                  'shadow': True, 
                  'fontsize': 9, 
                  'ncol': cols}
    plt.legend(**params_leg)
    nome_arquivo_saida = self.nome_arquivo.replace('.csv', nome_imagem)
    params_fig = {'bbox_inches': 'tight', 
                  'dpi': 400, 
                  'format': 'png'}
    plt.savefig(nome_arquivo_saida + '.png', **params_fig)
    plt.close()

  def conf_multi_graf(self, fig, handles, labels, nome_imagem, cols, dbot):    
    params_leg = {'handles': handles, 'labels': labels, 
                  'loc': 'lower center', 'fontsize': 9,  
                  'ncol': cols, 'fancybox': True, 'shadow': True}
    fig.legend(**params_leg)
    fig.subplots_adjust(bottom=dbot)
    fig.subplots_adjust(right=1.2)
    fig.subplots_adjust(left=-0.2)
    nome_arquivo_saida = self.nome_arquivo.replace('.csv', nome_imagem)
    params_fig = {'bbox_inches': 'tight', 
                  'dpi': 400, 
                  'format': 'png'}
    plt.savefig(nome_arquivo_saida + '.png', **params_fig)
    plt.close()

  def plot(self, ax, x, y, label, color, linestyle, marker):
    return ax.plot(x, y, label=label, 
                   color=color, linestyle=linestyle, marker=marker, 
                   linewidth=2, markevery=10)

  def criar_grafico_saude(self):
    x = [i[0] for i in self.dados]

    sus = [sum(i[1::6]) for i in self.dados]
    exp = [sum(i[2::6]) for i in self.dados]
    inf = [sum(i[3::6]) for i in self.dados]
    qua = [sum(i[4::6]) for i in self.dados]
    imu = [sum(i[5::6]) for i in self.dados]
    rec = [sum(i[6::6]) for i in self.dados]

    plt.title('Saude Humanos')
    self.plot(plt, x, sus, 'Suscetiveis', 'green', '-', '')
    self.plot(plt, x, exp, 'Expostos', 'brown', '-', '')
    self.plot(plt, x, inf, 'Infectados', 'red', '-', '')
    self.plot(plt, x, qua, 'Quarentena', 'LightCoral', '-', '')
    self.plot(plt, x, imu, 'Imunizados', 'magenta', '-', '')
    self.plot(plt, x, rec, 'Recuperados', 'blue', '-', '')

    self.conf_uni_graf('_Saude', 6)
  
  def criar_grafico_faixa_etaria(self):
    x = [i[0] for i in self.dados]

    cr = [sum(sum(i[j:j+6]) for j in range(1, 49, 24)) for i in self.dados]
    jo = [sum(sum(i[j:j+6]) for j in range(7, 49, 24)) for i in self.dados]
    ad = [sum(sum(i[j:j+6]) for j in range(13, 49, 24)) for i in self.dados]
    id = [sum(sum(i[j:j+6]) for j in range(19, 49, 24)) for i in self.dados]
    
    plt.title('Faixas Etarias Humanos')
    self.plot(plt, x, cr, 'Criancas', 'green', '-', 'o')
    self.plot(plt, x, jo, 'Jovens', 'brown', '-', '*')
    self.plot(plt, x, ad, 'Adultos', 'red', '-', 'x')
    self.plot(plt, x, id, 'Idosos', 'magenta', '-', 'v')

    self.conf_uni_graf('_FaixaEtaria', 4)

  def criar_grafico_sexo(self):
    x = [i[0] for i in self.dados]

    m = [sum(i[1:25]) for i in self.dados]
    f = [sum(i[25:]) for i in self.dados]

    plt.title('Sexos Humanos')
    self.plot(plt, x, m, 'Masculinos', 'black', '-', '')
    self.plot(plt, x, f, 'Femininos', 'black', '--', '')

    self.conf_uni_graf('_Sexo', 2)

  def criar_grafico_saude_faixa_etaria(self):
    x = [i[0] for i in self.dados]

    cr_sus = [sum(i[1::24]) for i in self.dados]
    cr_exp = [sum(i[2::24]) for i in self.dados]
    cr_inf = [sum(i[3::24]) for i in self.dados]
    cr_qua = [sum(i[4::24]) for i in self.dados]
    cr_imu = [sum(i[5::24]) for i in self.dados]
    cr_rec = [sum(i[6::24]) for i in self.dados]

    jo_sus = [sum(i[7::24]) for i in self.dados]
    jo_exp = [sum(i[8::24]) for i in self.dados]
    jo_inf = [sum(i[9::24]) for i in self.dados]
    jo_qua = [sum(i[10::24]) for i in self.dados]
    jo_imu = [sum(i[11::24]) for i in self.dados]
    jo_rec = [sum(i[12::24]) for i in self.dados]

    ad_sus = [sum(i[13::24]) for i in self.dados]
    ad_exp = [sum(i[14::24]) for i in self.dados]
    ad_inf = [sum(i[15::24]) for i in self.dados]
    ad_qua = [sum(i[16::24]) for i in self.dados]
    ad_imu = [sum(i[17::24]) for i in self.dados]
    ad_rec = [sum(i[18::24]) for i in self.dados]
    
    id_sus = [sum(i[19::24]) for i in self.dados]
    id_exp = [sum(i[20::24]) for i in self.dados]
    id_inf = [sum(i[21::24]) for i in self.dados]
    id_qua = [sum(i[22::24]) for i in self.dados]
    id_imu = [sum(i[23::24]) for i in self.dados]
    id_rec = [sum(i[24::24]) for i in self.dados]

    plt.title('Saude Humanos Criancas')
    self.plot(plt, x, cr_sus, 'Criancas Suscetiveis', 'green', '-', 'o')
    self.plot(plt, x, cr_exp, 'Criancas Expostos', 'brown', '-', 'o')
    self.plot(plt, x, cr_inf, 'Criancas Infectados', 'red', '-', 'o')
    self.plot(plt, x, cr_qua, 'Criancas Quarentena', 'LightCoral', '-', 'o')
    self.plot(plt, x, cr_imu, 'Criancas Imunizados', 'magenta', '-', 'o')
    self.plot(plt, x, cr_rec, 'Criancas Recuperados', 'blue', '-', 'o')
    self.conf_uni_graf('_Saude_x_FaixaEtaria_Criancas', 3)

    plt.title('Saude Humanos Jovens')
    self.plot(plt, x, jo_sus, 'Jovens Suscetiveis', 'green', '-', '*')
    self.plot(plt, x, jo_exp, 'Jovens Expostos', 'brown', '-', '*')
    self.plot(plt, x, jo_inf, 'Jovens Infectados', 'red', '-', '*')
    self.plot(plt, x, jo_qua, 'Jovens Quarentena', 'LightCoral', '-', '*')
    self.plot(plt, x, jo_imu, 'Jovens Imunizados', 'magenta', '-', '*')
    self.plot(plt, x, jo_rec, 'Jovens Recuperados', 'blue', '-', '*')
    self.conf_uni_graf('_Saude_x_FaixaEtaria_Jovens', 3)

    plt.title('Saude Humanos Adultos')
    self.plot(plt, x, ad_sus, 'Adultos Suscetiveis', 'green', '-', 'x')
    self.plot(plt, x, ad_exp, 'Adultos Expostos', 'brown', '-', 'x')
    self.plot(plt, x, ad_inf, 'Adultos Infectados', 'red', '-', 'x')
    self.plot(plt, x, ad_qua, 'Adultos Quarentena', 'LightCoral', '-', 'x')
    self.plot(plt, x, ad_imu, 'Adultos Imunizados', 'magenta', '-', 'x')
    self.plot(plt, x, ad_rec, 'Adultos Recuperados', 'blue', '-', 'x')
    self.conf_uni_graf('_Saude_x_FaixaEtaria_Adultos', 3)

    plt.title('Saude Humanos Idosos')
    self.plot(plt, x, id_sus, 'Idosos Suscetiveis', 'green', '-', 'v')
    self.plot(plt, x, id_exp, 'Idosos Expostos', 'brown', '-', 'v')
    self.plot(plt, x, id_inf, 'Idosos Infectados', 'red', '-', 'v')
    self.plot(plt, x, id_qua, 'Idosos Quarentena', 'LightCoral', '-', 'v')
    self.plot(plt, x, id_imu, 'Idosos Imunizados', 'magenta', '-', 'v')
    self.plot(plt, x, id_rec, 'Idosos Recuperados', 'blue', '-', 'v')
    self.conf_uni_graf('_Saude_x_FaixaEtaria_Idosos', 3)

    fig, axes = plt.subplots(2, 2)

    axes[0, 0].set_title('Saude Humanos Criancas')
    axes[0, 1].set_title('Saude Humanos Jovens')
    axes[1, 0].set_title('Saude Humanos Adultos')
    axes[1, 1].set_title('Saude Humanos Idosos')
    
    x1, = self.plot(axes[0, 0], x, cr_sus, '', 'green', '-', 'o')
    y1, = self.plot(axes[0, 1], x, jo_sus, '', 'green', '-', '*')
    z1, = self.plot(axes[1, 0], x, ad_sus, '', 'green', '-', 'x')
    w1, = self.plot(axes[1, 1], x, id_sus, '', 'green', '-', 'v')
    
    x2, = self.plot(axes[0, 0], x, cr_exp, '', 'brown', '-', 'o')
    y2, = self.plot(axes[0, 1], x, jo_exp, '', 'brown', '-', '*')
    z2, = self.plot(axes[1, 0], x, ad_exp, '', 'brown', '-', 'x')
    w2, = self.plot(axes[1, 1], x, id_exp, '', 'brown', '-', 'v')
    
    x3, = self.plot(axes[0, 0], x, cr_inf, '', 'red', '-', 'o')
    y3, = self.plot(axes[0, 1], x, jo_inf, '', 'red', '-', '*')
    z3, = self.plot(axes[1, 0], x, ad_inf, '', 'red', '-', 'x')
    w3, = self.plot(axes[1, 1], x, id_inf, '', 'red', '-', 'v')
    
    x4, = self.plot(axes[0, 0], x, cr_qua, '', 'LightCoral', '-', 'o')
    y4, = self.plot(axes[0, 1], x, jo_qua, '', 'LightCoral', '-', '*')
    z4, = self.plot(axes[1, 0], x, ad_qua, '', 'LightCoral', '-', 'x')
    w4, = self.plot(axes[1, 1], x, id_qua, '', 'LightCoral', '-', 'v')
    
    x5, = self.plot(axes[0, 0], x, cr_imu, '', 'magenta', '-', 'o')
    y5, = self.plot(axes[0, 1], x, jo_imu, '', 'magenta', '-', '*')
    z5, = self.plot(axes[1, 0], x, ad_imu, '', 'magenta', '-', 'x')
    w5, = self.plot(axes[1, 1], x, id_imu, '', 'magenta', '-', 'v')
    
    x6, = self.plot(axes[0, 0], x, cr_rec, '', 'blue', '-', 'o')
    y6, = self.plot(axes[0, 1], x, jo_rec, '', 'blue', '-', '*')
    z6, = self.plot(axes[1, 0], x, ad_rec, '', 'blue', '-', 'x')
    w6, = self.plot(axes[1, 1], x, id_rec, '', 'blue', '-', 'v')
    
    axes[0, 0].axis('tight')
    axes[0, 1].axis('tight')
    axes[1, 0].axis('tight')
    axes[1, 1].axis('tight')
   
    handles = (x1, y1, z1, w1, 
               x2, y2, z2, w2, 
               x3, y3, z3, w3,
               x4, y4, z4, w4, 
               x5, y5, z5, w5, 
               x6, y6, z6, w6)
    labels = ('Criancas Suscetiveis', 'Jovens Suscetiveis', 
              'Adultos Suscetiveis', 'Idosos Suscetiveis', 
              'Criancas Expostos', 'Jovens Expostos', 
               'Adultos Expostos', 'Idosos Expostos', 
              'Criancas Infectados', 'Jovens Infectados', 
              'Adultos Infectados',  'Idosos Infectados', 
              'Criancas Quarentena', 'Jovens Quarentena', 
              'Adultos Quarentena', 'Idosos Quarentena', 
              'Criancas Imunizados', 'Jovens Imunizados', 
              'Adultos Imunizados', 'Idosos Imunizados', 
              'Criancas Recuperados', 'Jovens Recuperados', 
              'Adultos Recuperados', 'Idosos Recuperados')
    self.conf_multi_graf(fig, handles, labels, '_Saude_x_FaixaEtaria', 6, 0.25)
  
  def criar_grafico_saude_sexo(self):
    x = [i[0] for i in self.dados]

    m_sus = [sum(i[1:25:6]) for i in self.dados]
    m_exp = [sum(i[2:25:6]) for i in self.dados]
    m_inf = [sum(i[3:25:6]) for i in self.dados]
    m_qua = [sum(i[4:25:6]) for i in self.dados]
    m_imu = [sum(i[5:25:6]) for i in self.dados]
    m_rec = [sum(i[6:25:6]) for i in self.dados]

    f_sus = [sum(i[25::6]) for i in self.dados]
    f_exp = [sum(i[26::6]) for i in self.dados]
    f_inf = [sum(i[27::6]) for i in self.dados]
    f_qua = [sum(i[28::6]) for i in self.dados]
    f_imu = [sum(i[29::6]) for i in self.dados]
    f_rec = [sum(i[30::6]) for i in self.dados]

    plt.title('Sexos e Saude Humanos')
    self.plot(plt, x, m_sus, 'Masculinos Saudaveis', 'green', '-', '')
    self.plot(plt, x, f_sus, 'Femininos Saudaveis', 'green', '--', '')
    
    self.plot(plt, x, m_exp, 'Masculinos Expostos', 'brown', '-', '')
    self.plot(plt, x, f_exp, 'Femininos Expostos', 'brown', '--', '')
    
    self.plot(plt, x, m_inf, 'Masculinos Infectados', 'red', '-', '')
    self.plot(plt, x, f_inf, 'Femininos Infectados', 'red', '--', '')
    
    self.plot(plt, x, m_qua, 'Masculinos Quarentena', 'LightCoral', '-', '')
    self.plot(plt, x, f_qua, 'Femininos Quarentena', 'LightCoral', '--', '')
    
    self.plot(plt, x, m_imu, 'Masculinos Imunizados', 'magenta', '-', '')
    self.plot(plt, x, f_imu, 'Femininos Imunizados', 'magenta', '--', '')
    
    self.plot(plt, x, m_rec, 'Masculinos Recuperados', 'blue', '-', '')
    self.plot(plt, x, f_rec, 'Femininos Recuperados', 'blue', '--', '')

    self.conf_uni_graf('_Saude_x_Sexo', 6)
    
  def criar_grafico_faixa_etaria_sexo(self):
    x = [i[0] for i in self.dados]

    cr_m = [sum(i[1:7]) for i in self.dados]
    jo_m = [sum(i[7:13]) for i in self.dados]
    ad_m = [sum(i[13:19]) for i in self.dados]
    id_m = [sum(i[19:25]) for i in self.dados]

    cr_f = [sum(i[25:31]) for i in self.dados]
    jo_f = [sum(i[31:37]) for i in self.dados]
    ad_f = [sum(i[37:43]) for i in self.dados]
    id_f = [sum(i[43:49]) for i in self.dados]

    plt.title('Sexos e Faixas Etarias Humanos')
    self.plot(plt, x, cr_m, 'Masculinos Criancas', 'blue', '-', 'o')
    self.plot(plt, x, cr_f, 'Femininos Criancas', 'blue', '--', 'o')

    self.plot(plt, x, jo_m, 'Masculinos Jovens', 'brown', '-', '*')
    self.plot(plt, x, jo_f, 'Femininos Jovens', 'brown', '--', '*')

    self.plot(plt, x, ad_m, 'Masculinos Adultos', 'magenta', '-', 'x')
    self.plot(plt, x, ad_f, 'Femininos Adultos', 'magenta', '--', 'x')

    self.plot(plt, x, id_m, 'Masculinos Idosos', 'green', '-', 'v')
    self.plot(plt, x, id_f, 'Femininos Idosos', 'green', '--', 'v')

    self.conf_uni_graf('_FaixaEtaria_x_Sexo', 4)
  
  def criar_graficos(self):
    proc = []
    
    proc.append(Process(target=self.criar_grafico_saude))
    proc.append(Process(target=self.criar_grafico_faixa_etaria))
    proc.append(Process(target=self.criar_grafico_sexo))
    
    proc.append(Process(target=self.criar_grafico_saude_faixa_etaria))
    proc.append(Process(target=self.criar_grafico_saude_sexo))
    proc.append(Process(target=self.criar_grafico_faixa_etaria_sexo))
    
    for p in proc: p.start()
    for p in proc: p.join()
