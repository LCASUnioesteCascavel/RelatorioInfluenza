#include "Timer.h"

/*
  Construtor da classe Timer. 
*/
Timer::Timer() {
  this->total = 0;
}

/*
  Metodo responsavel por armazenar o tempo inicial da ocorrencia de um evento. 
*/
void Timer::start() {
  cudaEventCreate(&begin);
  cudaEventCreate(&end);
  cudaEventRecord(begin);
}

/*
  Metodo responsavel por armazenar o tempo final da ocorrencia de um evento. 
  Com os tempos iniciais e finais e possivel calcular o tempo dispendido em 
  uma operacao. 
*/
void Timer::stop() {
  cudaEventRecord(end);
  cudaEventSynchronize(end);
  cudaEventElapsedTime(&parcial, begin, end);
  total += parcial;
}

/*
  Retorna o tempo calculado em segundos. 
*/
double Timer::getTime() {
  return total / 1000;
}
