private ArrayList<Local> aEstrelaConstruirCaminho(
    HashMap<Local, Local> verticesAnteriores, Local verticeAtual) {
  ArrayList<Local> caminho = new ArrayList<>();
  caminho.add(verticeAtual);
  while (verticesAnteriores.keySet().contains(verticeAtual)) {
    verticeAtual = verticesAnteriores.get(verticeAtual);
    if (verticeAtual != null) {
      caminho.add(0, verticeAtual);
    }
  }
  return caminho;
}
