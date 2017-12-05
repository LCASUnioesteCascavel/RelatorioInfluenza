package banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import estruturas.CasoInfeccao;
import estruturas.Local;
import estruturas.Ponto;
import estruturas.TipoTrajeto;
import estruturas.Vizinhanca;
import main.GeradorArquivosAmbientaisAcoplado;

public class ConsultasBanco {

	private static final String AREA_5 = "Area5";
	private static final String LOC_5 = "Localidade5";
	private static final String BOA_VISTA = "BoaVista";
	private static final String CASCAVEL = "Cascavel";

	private static final String HOST = "localhost";
	private static final String PORTA = "5432";
	private static final String BASE_BANCO = "BaseGeo";
	private static final String USUARIO_BANCO = "postgres";
	private static final String SENHA_BANCO = "postgres";

	static final String TABELA_CONFIG = "config";
	static final String TABELA_PONTOS = "pontos";
	static final String TABELA_PONTOS_ESQUINAS = "pontosesquinas";
	static final String TABELA_VIZINHANCAS_MOORE_PONTOS = "vizinhancasmoorepontos";
	static final String TABELA_POLIGONOS = "poligonos";
	static final String TABELA_ARESTAS = "arestas";
	static final String TABELA_CENTROIDES_ESQUINAS = "centroidesesquinas";
	static final String TABELA_CENTROIDES_LOTES = "centroideslotes";
	static final String TABELA_DISTRIBUICAO_MOSQUITOS = "distribuicaomosquitos";
	static final String TABELA_DISTRIBUICAO_HUMANOS = "distribuicaohumanos";
	static final String TABELA_QUADRAS_VACINACAO = "quadrasvacinacao";
	static final String TABELA_FAIXAS_ETARIAS_VACINACAO = "faixasetariasvacinacao";
	static final String TABELA_CICLOS_VACINACAO = "ciclosvacinacao";
	static final String TABELA_QUADRAS_RAIO = "quadrasraio";
	static final String TABELA_QUADRAS_BLOQUEIO = "quadrasbloqueio";
	static final String TABELA_QUADRAS_BLOQUEIO_BIOLOGICO = "quadrascontrolebiologico";
	static final String TABELA_QUADRAS_CONTROLE_AMBIENTAL = "quadrascontroleambiental";
	static final String TABELA_LOTES_PONTOS_ESTRATEGICOS = "lotespontosestrategicos";
	static final String TABELA_TIPOS_TRAJETOS = "tipos_trajetos";

	private String ambiente, ambienteDoenca;

	public ConsultasBanco(String ambiente, String doenca) {
		if (ambiente.equals(AREA_5)) {
			ambiente = new String("area5");
		}
		if (ambiente.equals(LOC_5)) {
			ambiente = new String("loc5");
		}
		if (ambiente.equals(BOA_VISTA)) {
			ambiente = new String("bvacompleto");
		}
		if (ambiente.equals(CASCAVEL)) {
			ambiente = new String("cascavel");
		}
		this.ambiente = ambiente + "_";
		this.ambienteDoenca = ambiente + "_" + doenca.toLowerCase() + "_";
	}

	private static Connection connect() {
		Connection c = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(
					"jdbc:postgresql://" + HOST + ":" + PORTA + "/" + BASE_BANCO,
					USUARIO_BANCO, SENHA_BANCO);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return c;
	}

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

	public HashMap<String, HashMap<String, ArrayList<Ponto>>> getPontos() {
		HashMap<String, HashMap<String, ArrayList<Ponto>>> retorno = new HashMap<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection
					.prepareStatement("SELECT * FROM " + ambiente + TABELA_PONTOS);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String quadra = rs.getString("quadra");
				String lote = rs.getString("lote");
				double x = rs.getDouble("x");
				double y = rs.getDouble("y");
				int ref = rs.getInt("ref");
				retorno.putIfAbsent(quadra, new HashMap<>());
				retorno.get(quadra).putIfAbsent(lote, new ArrayList<>());
				retorno.get(quadra).get(lote)
						.add(new Ponto(id, quadra, lote, x, y, ref));
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

	public HashMap<String, ArrayList<Ponto>> getPontosEsquinas() {
		HashMap<String, ArrayList<Ponto>> retorno = new HashMap<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM " + ambiente + TABELA_PONTOS_ESQUINAS);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String quadra = rs.getString("quadra");
				String l1 = rs.getString("l1");
				String l2 = rs.getString("l2");
				double x = rs.getDouble("x");
				double y = rs.getDouble("y");
				int ref = rs.getInt("ref");
				retorno.putIfAbsent(l1, new ArrayList<Ponto>());
				retorno.get(l1).add(new Ponto(id, quadra, l2, x, y, ref));
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

	public ArrayList<Vizinhanca> getVizinhancasMoorePontos() {
		ArrayList<Vizinhanca> retorno = new ArrayList<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM " + ambiente + TABELA_VIZINHANCAS_MOORE_PONTOS);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int id1 = rs.getInt("id1");
				String q1 = rs.getString("q1");
				String l1 = rs.getString("l1");
				double x1 = rs.getDouble("x1");
				double y1 = rs.getDouble("y1");
				int ref1 = rs.getInt("ref1");
				int id2 = rs.getInt("id2");
				String q2 = rs.getString("q2");
				String l2 = rs.getString("l2");
				double x2 = rs.getDouble("x2");
				double y2 = rs.getDouble("y2");
				int ref2 = rs.getInt("ref2");
				retorno.add(new Vizinhanca(new Ponto(id1, q1, l1, x1, y1, ref1),
						new Ponto(id2, q2, l2, x2, y2, ref2)));
				retorno.add(new Vizinhanca(new Ponto(id2, q2, l2, x2, y2, ref2),
						new Ponto(id1, q1, l1, x1, y1, ref1)));
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

	public ArrayList<Local> getPoligonos() {
		ArrayList<Local> retorno = new ArrayList<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection
					.prepareStatement("SELECT * FROM " + ambiente + TABELA_POLIGONOS);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Local temp = new Local(rs.getString("quadra"), rs.getString("lote"));
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

	public HashMap<String, HashMap<String, ArrayList<ArrayList<String>>>> getArestas() {
		HashMap<String, HashMap<String, ArrayList<ArrayList<String>>>> retorno 
		= new HashMap<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection
					.prepareStatement("SELECT * FROM " + ambiente + TABELA_ARESTAS);
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

	public HashMap<String, ArrayList<Ponto>> getCentroidesEsquinas() {
		HashMap<String, ArrayList<Ponto>> retorno = new HashMap<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM " + ambiente + TABELA_CENTROIDES_ESQUINAS);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String l1 = rs.getString("l1");
				String l2 = rs.getString("l2");
				double x = rs.getDouble("x");
				double y = rs.getDouble("y");
				int ref = rs.getInt("ref");
				retorno.putIfAbsent(l1, new ArrayList<Ponto>());
				retorno.get(l1).add(
						new Ponto(0, GeradorArquivosAmbientaisAcoplado.RUA, l2, x, y, ref));
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

	public HashMap<String, HashMap<String, Ponto>> getCentroidesLotes() {
		HashMap<String, HashMap<String, Ponto>> retorno = new HashMap<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM " + ambiente + TABELA_CENTROIDES_LOTES);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String q = rs.getString("q");
				String l = rs.getString("l");
				double x = rs.getDouble("x");
				double y = rs.getDouble("y");
				int ref = rs.getInt("ref");
				retorno.putIfAbsent(q, new HashMap<String, Ponto>());
				retorno.get(q).put(l, new Ponto(0, q, l, x, y, ref));
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

	public ArrayList<List<String>> getDistribuicaoMosquitos() {
		ArrayList<List<String>> retorno = new ArrayList<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM " + ambienteDoenca + TABELA_DISTRIBUICAO_MOSQUITOS);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String quadra = rs.getString("quadra");
				String quantidadeTotal = Integer
						.toString(rs.getInt("quantidade_total"));
				String sexo = rs.getString("sexo");
				String fase = rs.getString("fase");
				String percentualMinimoInfectados = Double
						.toString(rs.getDouble("percentual_minimo_infectados"));
				String percentualMaximoInfectados = Double
						.toString(rs.getDouble("percentual_maximo_infectados"));
				String sorotipo = Integer.toString(rs.getInt("sorotipo"));
				String ciclo = Integer.toString(rs.getInt("ciclo"));
				retorno.add(Arrays.asList(quadra, quantidadeTotal, sexo, fase,
						percentualMinimoInfectados, percentualMaximoInfectados, sorotipo,
						ciclo));
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

	public List<String> getQuadrasVacinacao() {
		ArrayList<String> retorno = new ArrayList<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM " + ambienteDoenca + TABELA_QUADRAS_VACINACAO);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				retorno.add(rs.getString("quadra"));
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

	public List<String> getQuadrasRaio() {
		ArrayList<String> retorno = new ArrayList<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM " + ambienteDoenca + TABELA_QUADRAS_RAIO);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				retorno.add(rs.getString("quadra"));
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

	public List<String> getQuadrasBloqueio() {
		ArrayList<String> retorno = new ArrayList<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM " + ambienteDoenca + TABELA_QUADRAS_BLOQUEIO);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				retorno.add(rs.getString("quadra"));
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

	public List<String> getQuadrasControleBiologico() {
		ArrayList<String> retorno = new ArrayList<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection.prepareStatement("SELECT * FROM " + ambienteDoenca
					+ TABELA_QUADRAS_BLOQUEIO_BIOLOGICO);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				retorno.add(rs.getString("quadra"));
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

	public List<String> getQuadrasControleAmbiental() {
		ArrayList<String> retorno = new ArrayList<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection.prepareStatement("SELECT * FROM " + ambienteDoenca
					+ TABELA_QUADRAS_CONTROLE_AMBIENTAL);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				retorno.add(rs.getString("quadra"));
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

	public List<List<String>> getLotesPontosEstrategicos() {
		ArrayList<List<String>> retorno = new ArrayList<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM " + ambienteDoenca + TABELA_LOTES_PONTOS_ESTRATEGICOS);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				retorno
						.add(Arrays.asList(rs.getString("quadra"), rs.getString("lote")));
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

	public Map<String, List<TipoTrajeto>> getTiposTrajetos() {
		ArrayList<TipoTrajeto> retorno = new ArrayList<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection
					.prepareStatement("SELECT * FROM " + TABELA_TIPOS_TRAJETOS);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String faixaEtaria = rs.getString("faixa_etaria");
				List<String> locais = Arrays.asList(rs.getString("locais").split(","));
				List<String> periodos = Arrays
						.asList(rs.getString("periodos").split(","));

				retorno.add(new TipoTrajeto(faixaEtaria, locais, periodos));
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					getClass().getName() + ": " + e.getMessage());
		}
		return retorno.stream()
				.collect(Collectors.groupingBy(TipoTrajeto::getFaixaEtaria));
	}

}
