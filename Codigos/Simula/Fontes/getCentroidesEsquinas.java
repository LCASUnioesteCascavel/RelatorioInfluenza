public HashMap<String, ArrayList<Ponto>> getCentroidesEsquinas() {
  HashMap<String, ArrayList<Ponto>> retorno = new HashMap<>();
  PreparedStatement stmt = null;
  Connection connection = connect();
  try {
    stmt = connection
        .prepareStatement("SELECT * FROM " + ambiente + "centroidesesquinas");
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      String l1 = rs.getString("l1");
      String l2 = rs.getString("l2");
      double x = rs.getDouble("x");
      double y = rs.getDouble("y");
      int refinamento = rs.getInt("refinamento");
      retorno.putIfAbsent(l1, new ArrayList<Ponto>());
      retorno.get(l1).add(new Ponto(0, "0000", l2, x, y, refinamento));
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
