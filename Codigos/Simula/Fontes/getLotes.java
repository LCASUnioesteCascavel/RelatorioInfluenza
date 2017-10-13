public ArrayList<Lote> getLotes() {
  ArrayList<Lote> retorno = new ArrayList<>();
  PreparedStatement stmt = null;
  Connection connection = connect();
  try {
    stmt = connection.prepareStatement("SELECT * FROM " + ambiente + "lotes");
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      Lote temp = new Lote(rs.getString("quadra"), rs.getString("lote"));
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
