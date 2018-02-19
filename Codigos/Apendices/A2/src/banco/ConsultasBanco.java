package banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import estruturas.CasoInfeccao;
import estruturas.Controle;
import estruturas.ControlePonto;
import estruturas.Local;
import estruturas.Ponto;
import estruturas.Raio;
import estruturas.TipoTrajeto;
import estruturas.Vacinado;
import estruturas.Vizinhanca;
import main.GeradorArquivosAmbientais;

public class ConsultasBanco {

	private static final String AREA_5 = "Area5";
	private static final String LOC_5 = "Localidade5";
	private static final String BOA_VISTA = "BoaVista";
	private static final String CASCAVEL = "Cascavel";

	private static final String HOST = "lcad-server";
	private static final String PORTA = "5432";
	private static final String BASE_BANCO = "BaseGeo";
	private static final String USUARIO_BANCO = "postgres";
	private static final String SENHA_BANCO = "postgres";

	private static final String TABELA_CONFIG = "config";
	private static final String TABELA_PONTOS = "pontos";
	private static final String TABELA_PONTOS_ESQUINAS = "pontosesquinas";
	private static final String TABELA_VIZINHANCAS_MOORE_PONTOS = "vizinhancasmoorepontos";
	private static final String TABELA_POLIGONOS = "poligonos";
	private static final String TABELA_ARESTAS = "arestas";
	private static final String TABELA_CENTROIDES_ESQUINAS = "centroidesesquinas";
	private static final String TABELA_CENTROIDES_LOTES = "centroideslotes";
	private static final String TABELA_DISTRIBUICAO_MOSQUITOS = "distribuicaomosquitos";
	private static final String TABELA_DISTRIBUICAO_HUMANOS = "distribuicaohumanos";
	private static final String TABELA_QUADRAS_VACINACAO = "quadrasvacinacao";
	private static final String TABELA_FAIXAS_ETARIAS_VACINACAO = "faixasetariasvacinacao";
	private static final String TABELA_CICLOS_VACINACAO = "ciclosvacinacao";
	private static final String TABELA_CONTROLES = "controles";
	private static final 
	String TABELA_QUADRAS_BLOQUEIO_BIOLOGICO = "quadrascontrolebiologico";
	private static final 
	String TABELA_QUADRAS_CONTROLE_AMBIENTAL = "quadrascontroleambiental";
	private static final String TABELA_LOTES_PONTOS_ESTRATEGICOS = "lotespontosestrategicos";
	private static final String TABELA_TIPOS_TRAJETOS = "tipos_trajetos";
	private static final String TABELA_CONTROLES_PONTOS = "controles_pontos";
	private static final String TABELA_PONTOS_RAIOS = "raios";
	private static final String TABELA_VACINADOS = "vacinados";
	private static final String TABELA_CLIMATICOS = "climaticos";

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

	public static Map<String, String> getConfig() {
		Map<String, String> retorno = new HashMap<>();
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

	public Map<String, Map<String, List<Ponto>>> getPontos() {
		Map<String, Map<String, List<Ponto>>> retorno = new HashMap<>();
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

	public Map<String, List<Ponto>> getPontosEsquinas() {
		Map<String, List<Ponto>> retorno = new HashMap<>();
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

	public List<Vizinhanca> getVizinhancasMoorePontos() {
		List<Vizinhanca> retorno = new ArrayList<>();
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

	public List<Local> getPoligonos() {
		List<Local> retorno = new ArrayList<>();
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

	public Map<String, Map<String, List<List<String>>>> getArestas() {
		Map<String, Map<String, List<List<String>>>> retorno = new HashMap<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection
					.prepareStatement("SELECT * FROM " + ambiente + TABELA_ARESTAS);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				List<String> temp = new ArrayList<>();
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

	public Map<String, List<Ponto>> getCentroidesEsquinas() {
		Map<String, List<Ponto>> retorno = new HashMap<>();
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
						new Ponto(0, GeradorArquivosAmbientais.RUA, l2, x, y, ref));
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

	public Map<String, Map<String, Ponto>> getCentroidesLotes() {
		Map<String, Map<String, Ponto>> retorno = new HashMap<>();
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

	public List<List<String>> getDistribuicaoMosquitos() {
		List<List<String>> retorno = new ArrayList<>();
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
				String probabilidadeMinimoInfectados = Double
						.toString(rs.getDouble("probabilidade_minimo_infectados"));
				String probabilidadeMaximoInfectados = Double
						.toString(rs.getDouble("probabilidade_maximo_infectados"));
				String sorotipo = Integer.toString(rs.getInt("sorotipo"));
				String ciclo = Integer.toString(rs.getInt("ciclo"));
				retorno.add(Arrays.asList(quadra, quantidadeTotal, sexo, fase,
						probabilidadeMinimoInfectados, probabilidadeMaximoInfectados,
						sorotipo, ciclo));
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

	public List<CasoInfeccao> getDistribuicaoHumanos() {
		List<CasoInfeccao> retorno = new ArrayList<>();
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
		List<String> retorno = new ArrayList<>();
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
		List<String> retorno = new ArrayList<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM " + ambienteDoenca + TABELA_FAIXAS_ETARIAS_VACINACAO);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String fe = rs.getString("faixa_etaria");
				if (fe.equals("Bebe")) {
					fe = new String("0");
				}
				if (fe.equals("Crianca")) {
					fe = new String("1");
				}
				if (fe.equals("Adolescente")) {
					fe = new String("2");
				}
				if (fe.equals("Jovem")) {
					fe = new String("3");
				}
				if (fe.equals("Adulto")) {
					fe = new String("4");
				}
				if (fe.equals("Idoso")) {
					fe = new String("5");
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
		List<String> retorno = new ArrayList<>();
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

	public List<Controle> getControles() {
		List<Controle> retorno = new ArrayList<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM " + ambienteDoenca + TABELA_CONTROLES);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String quadra = rs.getString("quadra");
				int cicloInicio = rs.getInt("ciclo_inicio");
				int cicloTermino = rs.getInt("ciclo_termino");
				String tipoControle = rs.getString("tipo_controle");
				double taxaMinMecanico = rs.getDouble("taxa_min_mecanico");
				double taxaMaxMecanico = rs.getDouble("taxa_max_mecanico");
				double taxaMinQuimico = rs.getDouble("taxa_min_quimico");
				double taxaMaxQuimico = rs.getDouble("taxa_max_quimico");
				retorno.add(new Controle(quadra, (cicloInicio + cicloTermino) / 2,
						tipoControle, taxaMinMecanico, taxaMaxMecanico, taxaMinQuimico,
						taxaMaxQuimico));
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					getClass().getName() + ": " + e.getMessage());
		}
		retorno.sort(Comparator.comparing(Controle::getCiclo));
		return retorno;
	}

	public List<String> getQuadrasControleBiologico() {
		List<String> retorno = new ArrayList<>();
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
		List<String> retorno = new ArrayList<>();
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
		List<List<String>> retorno = new ArrayList<>();
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
		List<TipoTrajeto> retorno = new ArrayList<>();
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

	public Map<Integer, List<ControlePonto>> getControlesPontos() {
		Map<Integer, List<ControlePonto>> retorno = new HashMap<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM " + ambienteDoenca + TABELA_CONTROLES_PONTOS);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int idControle = rs.getInt("id_controle");
				String quadra = rs.getString("quadra");
				String lote = rs.getString("lote");
				double x = rs.getDouble("x");
				double y = rs.getDouble("y");

				retorno.putIfAbsent(idControle, new ArrayList<>());
				retorno.get(idControle)
						.add(new ControlePonto(idControle, quadra, lote, x, y));
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

	public Map<Integer, List<Raio>> getPontosRaios() {
		Map<Integer, List<Raio>> retorno = new HashMap<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM " + ambienteDoenca + TABELA_PONTOS_RAIOS);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int idControle = rs.getInt("id_controle");
				String quadra = rs.getString("quadra");
				String lote = rs.getString("lote");
				double x = rs.getDouble("x");
				double y = rs.getDouble("y");

				retorno.putIfAbsent(idControle, new ArrayList<>());
				retorno.get(idControle).add(new Raio(idControle, quadra, lote, x, y));
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

	public List<Vacinado> getVacinados() {
		List<Vacinado> retorno = new ArrayList<>();
		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM " + ambienteDoenca + TABELA_VACINADOS);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int ciclo = rs.getInt("ciclo");
				String quadra = rs.getString("quadra");
				String lote = rs.getString("lote");
				int x = rs.getInt("x");
				int y = rs.getInt("y");
				String sexo = rs.getString("sexo");
				String faixaEtaria = rs.getString("faixa_etaria");
				int doses = rs.getInt("doses");

				retorno.add(
						new Vacinado(ciclo, quadra, lote, x, y, sexo, faixaEtaria, doses));
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

	public Map<String, List<Double>> getClimaticos() {
		Map<String, List<Double>> retorno = new HashMap<>();
		retorno.put("txMinNaoAlados", new ArrayList<>());
		retorno.put("txMaxNaoAlados", new ArrayList<>());
		retorno.put("txMinAlados", new ArrayList<>());
		retorno.put("txMaxAlados", new ArrayList<>());

		PreparedStatement stmt = null;
		Connection connection = connect();
		try {
			stmt = connection.prepareStatement(
					"SELECT * FROM " + ambienteDoenca + TABELA_CLIMATICOS);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				double txMinNaoAlados = rs.getDouble("tx_min_nao_alados");
				double txMaxNaoAlados = rs.getDouble("tx_max_nao_alados");
				double txMinAlados = rs.getDouble("tx_min_alados");
				double txMaxAlados = rs.getDouble("tx_max_alados");

				retorno.get("txMinNaoAlados").add(txMinNaoAlados);
				retorno.get("txMaxNaoAlados").add(txMaxNaoAlados);
				retorno.get("txMinAlados").add(txMinAlados);
				retorno.get("txMaxAlados").add(txMaxAlados);
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
}
