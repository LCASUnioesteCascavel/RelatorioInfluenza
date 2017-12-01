__host__ __device__
void Quarentena::operator()(int id) {
  dre seed = seeds[id];
  urd<double> dist(0.0, 1.0);

  int sd_h = GET_SD_H(id);

  if (sd_h == INFECTANTE and randPerc <= quaren[ciclo]) {
    SET_SD_H(id, QUARENTENA);
  }
}  
