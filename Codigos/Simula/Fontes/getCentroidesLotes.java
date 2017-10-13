public HashMap<String, HashMap<String, Ponto>> getCentroidesLotes() {
  HashMap<String, HashMap<String, Ponto>> retorno = new HashMap<>();
  PreparedStatement stmt = null;
  Connection connection = connect();
  try {
    stmt = connection
        .prepareStatement("SELECT * FROM " + ambiente + "centroideslotes");
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      String q = rs.getString("q");
      String l = rs.getString("l");
      double x = rs.getDouble("x");
      double y = rs.getDouble("y");
      int refinamento = rs.getInt("refinamento");
      retorno.putIfAbsent(q, new HashMap<String, Ponto>());
      retorno.get(q).put(l, new Ponto(0, q, l, x, y, refinamento));
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
