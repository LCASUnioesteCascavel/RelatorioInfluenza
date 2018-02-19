#ifndef __TIMER__
#define __TIMER__

/*
  Classe responsavel pelo calculo do tempo de execucao entre dois trechos de 
  cidigo. Esta classe pode ser utilizada para mensurar o tempo gasto na 
  execucao de metodos em GPU. O metodo "start" inicia a contagem do tempo, 
  "stop" termina a contagem do tempo e "getTime" retorna o tempo dispendido 
  em segundos. 
*/
struct Timer {
  
  private:
  
  cudaEvent_t begin, end;
  float parcial, total;
  
  public:
  
  Timer();
  void start();
  void stop();
  double getTime();
  
};

#endif
