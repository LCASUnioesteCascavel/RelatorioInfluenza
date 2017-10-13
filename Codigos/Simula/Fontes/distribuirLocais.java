private void distribuirLocais() {
  ArrayList<Lote> locais = (ArrayList<Lote>) lotes.clone();

  Collections.shuffle(locais);
  locaisCasa = new ArrayList<>(locais.subList(0, nCasa));
  locais.removeAll(locaisCasa);

  Collections.shuffle(locais);
  locaisTrabalho = new ArrayList<>(locais.subList(0, nTrabalho));
  locais.removeAll(locaisTrabalho);

  Collections.shuffle(locais);
  locaisLazer = new ArrayList<>(locais.subList(0, nLazer));
  locais.removeAll(locaisLazer);

  Collections.shuffle(locais);
  locaisEstudo = new ArrayList<>(locais.subList(0, nEstudo));
  locais.removeAll(locaisEstudo);
}
