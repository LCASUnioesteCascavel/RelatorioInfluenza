#include "RandPerc.h"

RandPerc::RandPerc() {
  seed = system_clock::now().time_since_epoch().count();
  gen = dre(seed);
  dis = urd<double>(0.0, 1.0);
}

double RandPerc::operator()() {
  return dis(gen);
}
