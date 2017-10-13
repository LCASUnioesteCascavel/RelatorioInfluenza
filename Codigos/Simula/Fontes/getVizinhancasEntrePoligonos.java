public ArrayList<ArrayList<String>> getVizinhancasEntrePoligonos() {
  ArrayList<ArrayList<String>> retorno = new ArrayList<>();
  PreparedStatement stmt = null;
  Connection connection = connect();
  try {
    stmt = connection.prepareStatement(
        "SELECT * FROM " + ambiente + "vizinhancasentrepoligonos");
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      ArrayList<String> temp = new ArrayList<>();
      temp.add(rs.getString("l1"));
      temp.add(rs.getString("q1"));
      temp.add(rs.getString("l2"));
      temp.add(rs.getString("q2"));
      retorno.add(temp);
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
