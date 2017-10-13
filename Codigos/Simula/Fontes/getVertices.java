public ArrayList<Local> getVertices() {
  ArrayList<Local> retorno = new ArrayList<>();
  PreparedStatement stmt = null;
  Connection connection = connect();
  try {
    stmt = connection
        .prepareStatement("SELECT * FROM " + ambiente + "vertices");
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      String q = rs.getString("quadra");
      String l = rs.getString("lote");
      retorno.add(new Local(q, l));
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
