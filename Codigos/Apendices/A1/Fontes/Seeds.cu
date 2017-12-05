#include "Seeds.h"
#include "Fontes/Uteis/RandPerc.h"
#include "Fontes/Macros/MacrosParametros.h"

__host__ __device__ 
void InitSeeds::operator()(tuple<int, dre &> t) {
  int seed = get<0>(t);
  get<1>(t) = dre(seed);
}

Seeds::Seeds(initializer_list<int> l) {
  ind1 = make_counting_iterator(0);

  nSeeds = max(l);

  RandPerc rand;

  seedsDev = new DVector<dre>(nSeeds);
  int *rands = new int[nSeeds]();
  for (int i = 0; i < nSeeds; ++i) {
    rands[i] = (int)ENTRE_FAIXA(0, 100000, rand());
  }
  DVector<int> randsDev(rands, rands + nSeeds);
  delete[](rands);

  for_each_n(
    make_zip_iterator(
      make_tuple(
        randsDev.begin(), seedsDev->begin()
      )
    ), nSeeds, InitSeeds()
  );
  PseedsDev = raw_pointer_cast(seedsDev->data());
}

int Seeds::getMemoriaGPU() {
  return (nSeeds * sizeof(dre));
}

Seeds::~Seeds() {
  delete(seedsDev);
}
