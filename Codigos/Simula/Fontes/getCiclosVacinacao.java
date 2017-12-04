public List<String> getCiclosVacinacao() {
  ArrayList<String> retorno = new ArrayList<>();
  PreparedStatement stmt = null;
  Connection connection = connect();
  try {
    stmt = connection.prepareStatement(
        "SELECT * FROM " + ambienteDoenca + TABELA_CICLOS_VACINACAO);
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      retorno.add(Integer.toString(rs.getInt("ciclo")));
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
