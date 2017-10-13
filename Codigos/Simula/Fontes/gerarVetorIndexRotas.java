private void gerarVetorIndexRotas() {
  ArrayList<ArrayList<Integer>> rotasCrianca = new ArrayList<>();
  int k = 0;

  for (int i = 0; i < trajetosCrianca.size(); ++i) {

    for (int j = 0; j < trajetosCrianca.get(i).size() - 1; ++j) {

      ArrayList<Integer> rota = new ArrayList<>();

      Local localOrigem = trajetosCrianca.get(i).get(j);
      Local localDestino = trajetosCrianca.get(i).get(j + 1);

      int quadraOrigem = indicesQuadras.get(localOrigem.quadra);
      int loteOrigem = indicesLotes.get(localOrigem.quadra)
          .get(localOrigem.lote);

      int quadraDestino = indicesQuadras.get(localDestino.quadra);
      int loteDestino = indicesLotes.get(localDestino.quadra)
          .get(localDestino.lote);

      rota.add(loteOrigem);
      rota.add(quadraOrigem);
      for (Local rua : ruasCrianca.get(k++)) {
        rota.add(indicesLotes.get("0000").get(rua.lote));
      }
      rota.add(loteDestino);
      rota.add(quadraDestino);

      rotasCrianca.add(rota);
    }
  }

  k = 0;
  ArrayList<ArrayList<Integer>> rotasJovem = new ArrayList<>();
  for (int i = 0; i < trajetosJovem.size(); ++i) {

    for (int j = 0; j < trajetosJovem.get(i).size() - 1; ++j) {
      ArrayList<Integer> rota = new ArrayList<>();

      Local localOrigem = trajetosJovem.get(i).get(j);
      Local localDestino = trajetosJovem.get(i).get(j + 1);

      int quadraOrigem = indicesQuadras.get(localOrigem.quadra);
      int loteOrigem = indicesLotes.get(localOrigem.quadra)
          .get(localOrigem.lote);

      int quadraDestino = indicesQuadras.get(localDestino.quadra);
      int loteDestino = indicesLotes.get(localDestino.quadra)
          .get(localDestino.lote);

      rota.add(loteOrigem);
      rota.add(quadraOrigem);
      for (Local rua : ruasJovem.get(k++)) {
        rota.add(indicesLotes.get("0000").get(rua.lote));
      }
      rota.add(loteDestino);
      rota.add(quadraDestino);

      rotasJovem.add(rota);
    }
  }

  k = 0;
  ArrayList<ArrayList<Integer>> rotasAdulto = new ArrayList<>();
  for (int i = 0; i < trajetosAdulto.size(); ++i) {

    for (int j = 0; j < trajetosAdulto.get(i).size() - 1; ++j) {
      ArrayList<Integer> rota = new ArrayList<>();

      Local localOrigem = trajetosAdulto.get(i).get(j);
      Local localDestino = trajetosAdulto.get(i).get(j + 1);

      int quadraOrigem = indicesQuadras.get(localOrigem.quadra);
      int loteOrigem = indicesLotes.get(localOrigem.quadra)
          .get(localOrigem.lote);

      int quadraDestino = indicesQuadras.get(localDestino.quadra);
      int loteDestino = indicesLotes.get(localDestino.quadra)
          .get(localDestino.lote);

      rota.add(loteOrigem);
      rota.add(quadraOrigem);
      for (Local rua : ruasAdulto.get(k++)) {
        rota.add(indicesLotes.get("0000").get(rua.lote));
      }
      rota.add(loteDestino);
      rota.add(quadraDestino);

      rotasAdulto.add(rota);
    }
  }

  k = 0;
  ArrayList<ArrayList<Integer>> rotasIdoso = new ArrayList<>();
  for (int i = 0; i < trajetosIdoso.size(); ++i) {

    for (int j = 0; j < trajetosIdoso.get(i).size() - 1; ++j) {
      ArrayList<Integer> rota = new ArrayList<>();

      Local localOrigem = trajetosIdoso.get(i).get(j);
      Local localDestino = trajetosIdoso.get(i).get(j + 1);

      int quadraOrigem = indicesQuadras.get(localOrigem.quadra);
      int loteOrigem = indicesLotes.get(localOrigem.quadra)
          .get(localOrigem.lote);

      int quadraDestino = indicesQuadras.get(localDestino.quadra);
      int loteDestino = indicesLotes.get(localDestino.quadra)
          .get(localDestino.lote);

      rota.add(loteOrigem);
      rota.add(quadraOrigem);
      for (Local rua : ruasIdoso.get(k++)) {
        rota.add(indicesLotes.get("0000").get(rua.lote));
      }
      rota.add(loteDestino);
      rota.add(quadraDestino);

      rotasIdoso.add(rota);
    }
  }

  indexRotas = new ArrayList<>();
  int desl = 0;
  indexRotas.add(desl);
  for (ArrayList<Integer> i : rotasCrianca) {
    desl += i.size();
    indexRotas.add(desl);
  }
  for (ArrayList<Integer> i : rotasJovem) {
    desl += i.size();
    indexRotas.add(desl);
  }
  for (ArrayList<Integer> i : rotasAdulto) {
    desl += i.size();
    indexRotas.add(desl);
  }
  for (ArrayList<Integer> i : rotasIdoso) {
    desl += i.size();
    indexRotas.add(desl);
  }

  nRotas = rotasCrianca.size() + rotasJovem.size() + rotasAdulto.size()
      + rotasIdoso.size();

  vetorRotas = new ArrayList<>();
  vetorRotas.addAll(rotasCrianca.stream().flatMap(i -> i.stream())
      .collect(Collectors.toList()));
  vetorRotas.addAll(rotasJovem.stream().flatMap(i -> i.stream())
      .collect(Collectors.toList()));
  vetorRotas.addAll(rotasAdulto.stream().flatMap(i -> i.stream())
      .collect(Collectors.toList()));
  vetorRotas.addAll(rotasIdoso.stream().flatMap(i -> i.stream())
      .collect(Collectors.toList()));
}
