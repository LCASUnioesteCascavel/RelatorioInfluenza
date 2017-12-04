public HashMap<String, HashMap<String, ArrayList<Ponto>>> getPontos() {
  HashMap<String, HashMap<String, ArrayList<Ponto>>> retorno = new HashMap<>();
  PreparedStatement stmt = null;
  Connection connection = connect();
  try {
    stmt = connection
        .prepareStatement("SELECT * FROM " + ambiente + TABELA_PONTOS);
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      int id = rs.getInt("id");
      String quadra = rs.getString("quadra");
      String lote = rs.getString("lote");
      double x = rs.getDouble("x");
      double y = rs.getDouble("y");
      int ref = rs.getInt("ref");
      retorno.putIfAbsent(quadra, new HashMap<>());
      retorno.get(quadra).putIfAbsent(lote, new ArrayList<>());
      retorno.get(quadra).get(lote)
          .add(new Ponto(id, quadra, lote, x, y, ref));
    }
    rs.close();
    stmt.close();
    connection.close();
  } catch (Exception e) {
    JOptionPane.showMessageDialog(null,
        getClass().getName() + ": " + e.getMessage());
  }
  return retorno;
} 
