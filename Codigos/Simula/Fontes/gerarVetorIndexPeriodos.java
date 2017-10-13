private void gerarVetorIndexPeriodos() {
  ArrayList<ArrayList<Integer>> periodosCrianca = new ArrayList<>();
  for (int i = 0; i < trajetosCrianca.size(); ++i) {
    ArrayList<Integer> periodo = new ArrayList<>();
    Local localOrigem = trajetosCrianca.get(i).get(0);
    periodo.add(localOrigem.periodo);

    for (int j = 1; j < trajetosCrianca.get(i).size(); ++j) {
      Local localDestino = trajetosCrianca.get(i).get(j);
      periodo.add(localDestino.periodo);
    }

    periodosCrianca.add(periodo);
  }

  ArrayList<ArrayList<Integer>> periodosJovem = new ArrayList<>();
  for (int i = 0; i < trajetosJovem.size(); ++i) {
    ArrayList<Integer> periodo = new ArrayList<>();
    Local localOrigem = trajetosJovem.get(i).get(0);
    periodo.add(localOrigem.periodo);

    for (int j = 1; j < trajetosJovem.get(i).size(); ++j) {
      Local localDestino = trajetosJovem.get(i).get(j);
      periodo.add(localDestino.periodo);
    }

    periodosJovem.add(periodo);
  }

  ArrayList<ArrayList<Integer>> periodosAdulto = new ArrayList<>();
  for (int i = 0; i < trajetosAdulto.size(); ++i) {
    ArrayList<Integer> periodo = new ArrayList<>();
    Local localOrigem = trajetosAdulto.get(i).get(0);
    periodo.add(localOrigem.periodo);

    for (int j = 1; j < trajetosAdulto.get(i).size(); ++j) {
      Local localDestino = trajetosAdulto.get(i).get(j);
      periodo.add(localDestino.periodo);
    }

    periodosAdulto.add(periodo);
  }

  ArrayList<ArrayList<Integer>> periodosIdoso = new ArrayList<>();
  for (int i = 0; i < trajetosIdoso.size(); ++i) {
    ArrayList<Integer> periodo = new ArrayList<>();
    Local localOrigem = trajetosIdoso.get(i).get(0);
    periodo.add(localOrigem.periodo);

    for (int j = 1; j < trajetosIdoso.get(i).size(); ++j) {
      Local localDestino = trajetosIdoso.get(i).get(j);
      periodo.add(localDestino.periodo);
    }

    periodosIdoso.add(periodo);
  }

  indexPeriodos = new ArrayList<>();
  int desl = 0;
  indexPeriodos.add(desl);
  for (ArrayList<Integer> i : periodosCrianca) {
    desl += i.size();
    indexPeriodos.add(desl);
  }
  for (ArrayList<Integer> i : periodosJovem) {
    desl += i.size();
    indexPeriodos.add(desl);
  }
  for (ArrayList<Integer> i : periodosAdulto) {
    desl += i.size();
    indexPeriodos.add(desl);
  }
  for (ArrayList<Integer> i : periodosIdoso) {
    desl += i.size();
    indexPeriodos.add(desl);
  }

  vetorPeriodos = new ArrayList<>();
  vetorPeriodos.addAll(periodosCrianca.stream().flatMap(i -> i.stream())
      .collect(Collectors.toList()));
  vetorPeriodos.addAll(periodosJovem.stream().flatMap(i -> i.stream())
      .collect(Collectors.toList()));
  vetorPeriodos.addAll(periodosAdulto.stream().flatMap(i -> i.stream())
      .collect(Collectors.toList()));
  vetorPeriodos.addAll(periodosIdoso.stream().flatMap(i -> i.stream())
      .collect(Collectors.toList()));
}
