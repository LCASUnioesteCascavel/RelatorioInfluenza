public ArrayList<CasoInfeccao> getDistribuicaoHumanos() {
  ArrayList<CasoInfeccao> retorno = new ArrayList<>();
  PreparedStatement stmt = null;
  Connection connection = connect();
  try {
    stmt = connection.prepareStatement(
        "SELECT * FROM " + ambienteDoenca + TABELA_DISTRIBUICAO_HUMANOS);
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      int ciclo = rs.getInt("ciclo");
      int x = rs.getInt("x");
      int y = rs.getInt("y");
      String faixaEtaria = rs.getString("faixa_etaria");
      retorno.add(new CasoInfeccao(ciclo, x, y, faixaEtaria));
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
