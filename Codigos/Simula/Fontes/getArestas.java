public HashMap<String, HashMap<String, ArrayList<ArrayList<String>>>> getArestas() {
  HashMap<String, HashMap<String, ArrayList<ArrayList<String>>>> retorno = new HashMap<>();
  PreparedStatement stmt = null;
  Connection connection = connect();
  try {
    stmt = connection
        .prepareStatement("SELECT * FROM " + ambiente + "arestas");
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      ArrayList<String> temp = new ArrayList<>();
      temp.add(rs.getString("l1"));
      temp.add(rs.getString("q1"));
      temp.add(rs.getString("l2"));
      temp.add(rs.getString("q2"));
      retorno.putIfAbsent(temp.get(1), new HashMap<>());
      retorno.get(temp.get(1)).putIfAbsent(temp.get(0), new ArrayList<>());
      retorno.get(temp.get(1)).get(temp.get(0)).add(temp);
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
