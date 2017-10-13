private void gerarCaminhos() {
  ruasCrianca = new ArrayList<>();
  ruasJovem = new ArrayList<>();
  ruasAdulto = new ArrayList<>();
  ruasIdoso = new ArrayList<>();

  for (ArrayList<Local> trajeto : trajetosCrianca) {
    for (int i = 0; i < trajeto.size() - 1; ++i) {
      ruasCrianca.add(aEstrela(trajeto.get(i), trajeto.get(i + 1)));
    }
  }
  for (ArrayList<Local> trajeto : trajetosJovem) {
    for (int i = 0; i < trajeto.size() - 1; ++i) {
      ruasJovem.add(aEstrela(trajeto.get(i), trajeto.get(i + 1)));
    }
  }
  for (ArrayList<Local> trajeto : trajetosAdulto) {
    for (int i = 0; i < trajeto.size() - 1; ++i) {
      ruasAdulto.add(aEstrela(trajeto.get(i), trajeto.get(i + 1)));
    }
  }
  for (ArrayList<Local> trajeto : trajetosIdoso) {
    for (int i = 0; i < trajeto.size() - 1; ++i) {
      ruasIdoso.add(aEstrela(trajeto.get(i), trajeto.get(i + 1)));
    }
  }
}
