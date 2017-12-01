__host__ __device__
void MovimentacaoHumanos::operator()(int id) {
  dre seed = seeds[id];
  urd<double> dist(0.0, 1.0);

  int sd_h = GET_SD_H(id), fe_h = GET_FE_H(id);
  int p;

  if (sd_h == INFECTANTE and randPerc <= PERC_MIGRACAO) {
    p = randPerc * sizePos;
    SET_X_H(id, pos[p].x);
    SET_Y_H(id, pos[p].y);
    SET_L_H(id, pos[p].lote);
    SET_Q_H(id, pos[p].quadra);
  }

  if (sd_h != MORTO and randPerc <= TAXA_MOBILIDADE(fe_h)) {
    if (sd_h == QUARENTENA) {
      movimentacaoLocal(id, seed, dist);
    } else {
      movimentacaoAleatoria(id, seed, dist);
    }
  }
}