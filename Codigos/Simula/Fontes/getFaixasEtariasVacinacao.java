public List<String> getFaixasEtariasVacinacao() {
  ArrayList<String> retorno = new ArrayList<>();
  PreparedStatement stmt = null;
  Connection connection = connect();
  try {
    stmt = connection.prepareStatement(
        "SELECT * FROM " + ambienteDoenca + TABELA_FAIXAS_ETARIAS_VACINACAO);
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      String fe = rs.getString("faixa_etaria");
      if (fe.equals("Crianca")) {
        fe = new String("0");
      }
      if (fe.equals("Jovem")) {
        fe = new String("1");
      }
      if (fe.equals("Adulto")) {
        fe = new String("2");
      }
      if (fe.equals("Idoso")) {
        fe = new String("4");
      }
      retorno.add(fe);
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
