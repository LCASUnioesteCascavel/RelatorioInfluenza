public ArrayList<Pair<Ponto, Ponto>> getVizinhancasMoorePontos() {
  ArrayList<Pair<Ponto, Ponto>> retorno = new ArrayList<>();
  PreparedStatement stmt = null;
  Connection connection = connect();
  try {
    stmt = connection.prepareStatement(
        "SELECT * FROM " + ambiente + "vizinhancasmoorepontos");
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      int id1 = rs.getInt("id1");
      String q1 = rs.getString("q1");
      String l1 = rs.getString("l1");
      double x1 = rs.getDouble("x1");
      double y1 = rs.getDouble("y1");
      int refinamento1 = rs.getInt("refinamento1");
      int id2 = rs.getInt("id2");
      String q2 = rs.getString("q2");
      String l2 = rs.getString("l2");
      double x2 = rs.getDouble("x2");
      double y2 = rs.getDouble("y2");
      int refinamento2 = rs.getInt("refinamento2");
      retorno.add(
          new Pair<Ponto, Ponto>(new Ponto(id1, q1, l1, x1, y1, refinamento1),
              new Ponto(id2, q2, l2, x2, y2, refinamento2)));
      retorno.add(
          new Pair<Ponto, Ponto>(new Ponto(id2, q2, l2, x2, y2, refinamento2),
              new Ponto(id1, q1, l1, x1, y1, refinamento1)));
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
