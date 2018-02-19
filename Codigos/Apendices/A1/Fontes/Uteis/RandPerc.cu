#include "RandPerc.h"

/*
  Construtor da classe RandPerc. 
*/
RandPerc::RandPerc() {
  seed = system_clock::now().time_since_epoch().count();
  gen = dre(seed);
  dis = urd<double>(0.0, 1.0);
}

/*
  Operador () da classe RandPerc, responsavel por retornar um numero 
  aleatirio no intervalo [0.0, 1.0). 
*/
double RandPerc::operator()() {
  return dis(gen);
}
