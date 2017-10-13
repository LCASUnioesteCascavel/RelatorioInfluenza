private void gerarIndexTrajetosFaixaEtaria() {
  indexTrajetosFaixaEtaria = new ArrayList<>();
  int desl = 0;
  indexTrajetosFaixaEtaria.add(desl);
  desl += trajetosCrianca.size();
  indexTrajetosFaixaEtaria.add(desl);
  desl += trajetosJovem.size();
  indexTrajetosFaixaEtaria.add(desl);
  desl += trajetosAdulto.size();
  indexTrajetosFaixaEtaria.add(desl);
  desl += trajetosIdoso.size();
  indexTrajetosFaixaEtaria.add(desl);
}
