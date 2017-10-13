private ArrayList<Local> aEstrela(Local verticeOrigem, Local verticeDestino) {

  HashSet<Local> verticesJaVisitados = new HashSet<>();

  HashSet<Local> verticesNaoVisitados = new HashSet<>();

  ArrayList<String> primeiraRuaAux = arestas.get(verticeOrigem.quadra)
      .get(verticeOrigem.lote).stream().filter(i -> i.get(3).equals("0000"))
      .findFirst().get();
  Local primeiraRua = new Local(primeiraRuaAux.get(3), primeiraRuaAux.get(2));
  verticesNaoVisitados.add(primeiraRua);

  HashMap<Local, Local> verticesAnteriores = new HashMap<>();
  for (Local vertice : vertices) {
    verticesAnteriores.put(vertice, null);
  }

  HashMap<Local, Double> distanciaOrigemAoAtual = new HashMap<>();
  for (Local vertice : vertices) {
    distanciaOrigemAoAtual.put(vertice, Double.MAX_VALUE);
  }
  distanciaOrigemAoAtual.put(primeiraRua, 0.0);

  HashMap<Local, Double> distanciaAtualAoDestino = new HashMap<>();
  for (Local vertice : vertices) {
    distanciaAtualAoDestino.put(vertice, Double.MAX_VALUE);
  }
  Ponto pontoOrigem = centroidesLotes.get(verticeOrigem.quadra)
      .get(verticeOrigem.lote);
  Ponto pontoDestino = centroidesLotes.get(verticeDestino.quadra)
      .get(verticeDestino.lote);
  distanciaAtualAoDestino.put(primeiraRua, Point.distance(pontoOrigem.x,
      pontoOrigem.y, pontoDestino.x, pontoDestino.y));

  while (!verticesNaoVisitados.isEmpty()) {

    Local atual = verticesNaoVisitados.stream()
        .collect(Collectors
            .minBy((i, j) -> Double.compare(distanciaAtualAoDestino.get(i),
                distanciaAtualAoDestino.get(j))))
        .get();

    if (arestas.get(atual.quadra).get(atual.lote).stream()
        .filter(i -> i.get(2).equals(verticeDestino.lote)
            && i.get(3).equals(verticeDestino.quadra))
        .count() > 0) {
      ArrayList<Local> caminho = aEstrelaConstruirCaminho(verticesAnteriores,
          atual);
      return caminho;
    }

    verticesNaoVisitados.remove(atual);
    verticesJaVisitados.add(atual);

    for (Ponto vizinhoAux : centroidesEsquinas.get(atual.lote).stream()
        .collect(Collectors.toList())) {
      Local vizinho = new Local(vizinhoAux.quadra, vizinhoAux.lote);

      if (verticesJaVisitados.contains(vizinho)) {
        continue;
      }
      Ponto pontoDestinoAux = centroidesLotes.get(verticeDestino.quadra)
          .get(verticeDestino.lote);
      pontoOrigem = centroidesEsquinas.get(atual.lote).stream()
          .collect(Collectors.minBy((i, j) -> Double.compare(
              Point.distance(i.x, i.y, pontoDestinoAux.x, pontoDestinoAux.y),
              Point.distance(j.x, j.y, pontoDestinoAux.x,
                  pontoDestinoAux.y))))
          .get();
      pontoDestino = vizinhoAux;
      double novaDistancia = distanciaOrigemAoAtual.get(atual)
          + Point.distance(pontoOrigem.x, pontoOrigem.y, pontoDestino.x,
              pontoDestino.y);
      if (!verticesNaoVisitados.contains(vizinho)) {
        verticesNaoVisitados.add(vizinho);
      } else {
        if (novaDistancia >= distanciaOrigemAoAtual.get(vizinho)) {
          continue;
        }
      }
      verticesAnteriores.put(vizinho, atual);
      distanciaOrigemAoAtual.put(vizinho, novaDistancia);
      pontoOrigem = vizinhoAux;
      pontoDestino = centroidesLotes.get(verticeDestino.quadra)
          .get(verticeDestino.lote);
      distanciaAtualAoDestino.put(primeiraRua, Point.distance(pontoOrigem.x,
          pontoOrigem.y, pontoDestino.x, pontoDestino.y));
      distanciaAtualAoDestino.put(vizinho,
          distanciaOrigemAoAtual.get(vizinho) + Point.distance(pontoOrigem.x,
              pontoOrigem.y, pontoDestino.x, pontoDestino.y));
    }
  }
  System.err.println(
      verticeOrigem + " -> " + verticeDestino + " : Retornando NULL");
  System.exit(1);
  return null;
}
