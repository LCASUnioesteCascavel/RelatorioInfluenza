__host__ __device__
void MovimentacaoHumanos::operator()(int id) {
  dre seed = seeds[id];
  urd<double> dist(0.0, 1.0);

  int sd_h = GET_SD_H(id), fe_h = GET_FE_H(id);
  int p;

  // Humanos infectantes podem realizar uma movimentacao para uma posicao 
  // aleatoria do ambiente, mimetizando Redes de Mundo Pequeno. 
  if (sd_h == INFECTANTE and randPerc <= PERC_MIGRACAO) {
    p = randPerc * sizePos;
    SET_X_H(id, pos[p].x);
    SET_Y_H(id, pos[p].y);
    SET_L_H(id, pos[p].lote);
    SET_Q_H(id, pos[p].quadra);
  }

  // A movimentacao do agente e condicionada a taxa de mobilidade para sua 
  // faixa etaria. 
  if (sd_h != MORTO and randPerc <= TAXA_MOBILIDADE(fe_h)) {
    if (sd_h == QUARENTENA) { 
      // Humanos em quarentena realizam movimentacao local. 
      movimentacaoLocal(id, seed, dist);
    } else {
      // Demais agentes realizam movimentacao aleatoria. 
      movimentacaoAleatoria(id, seed, dist);
    }
  }
}
