public static HashMap<String, String> getConfig() {
  HashMap<String, String> retorno = new HashMap<>();
  PreparedStatement stmt = null;
  Connection connection = connect();
  try {
    stmt = connection.prepareStatement("SELECT * FROM " + TABELA_CONFIG);
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      String key = rs.getString("key");
      String value = rs.getString("value");
      retorno.put(key, value);
    }
    rs.close();
    stmt.close();
    connection.close();
  } catch (Exception e) {
    JOptionPane.showMessageDialog(null, e.getMessage());
  }
  return retorno;
}
