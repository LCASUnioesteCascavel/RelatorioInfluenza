__host__ __device__
void Quarentena::operator()(int id) {
  dre seed = seeds[id];
  urd<double> dist(0.0, 1.0);

  int sd_h = GET_SD_H(id);

  // Os agentes infectantes podem ser passados probabilisticamente ao estado 
  // de quarentena. As probabilidades de passagem ao estado de quarentena sao 
  // definidas pela variavel "quaren" e sao distintas para cada ciclo. 
  if (sd_h == INFECTANTE and randPerc <= quaren[ciclo]) {
    SET_SD_H(id, QUARENTENA);
  }
}
