#include "Timer.h"

Timer::Timer() {
  this->total = 0;
}

void Timer::start() {
  cudaEventCreate(&begin);
  cudaEventCreate(&end);
  cudaEventRecord(begin);
}

void Timer::stop() {
  cudaEventRecord(end);
  cudaEventSynchronize(end);
  cudaEventElapsedTime(&parcial, begin, end);
  total += parcial;
}

double Timer::getTime() {
  return total / 1000;
}
