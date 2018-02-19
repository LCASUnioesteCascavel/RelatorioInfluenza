package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import banco.ConsultasBanco;
import estruturas.CasoInfeccao;
import estruturas.Controle;
import estruturas.ControlePonto;
import estruturas.Local;
import estruturas.Ponto;
import estruturas.Raio;
import estruturas.Rota;
import estruturas.TipoTrajeto;
import estruturas.TipoTrajeto.LocalPeriodo;
import estruturas.Trajeto;
import estruturas.Vacinado;
import estruturas.Vizinhanca;

public class Dengue {

	public String pastaSaida;

	public String nomeArquivoAmbiental;
	public String nomeArquivoMovimentacao;
	public String nomeArquivoControles;
	public String nomeArquivoClimaticos;
	public String nomeArquivoDistribuicaoMosquitos;
	public String nomeArquivoDistribuicaoHumanos;

	private Map<String, String> params;
	private ConsultasBanco con;
	private Map<String, Map<String, List<Ponto>>> pontos;
	private Map<String, Map<String, List<Vizinhanca>>> vizinhancas;
	private Map<String, List<Ponto>> pontosEsquinas;
	private Map<String, Integer> indicesQuadras;
	private Map<String, Map<String, Integer>> indicesLotes;
	private Map<String, Map<String, List<Vizinhanca>>> fronteiras;
	private List<Integer> indexQuadras, indexVizinhancas, vetorVizinhancas,
			indexPosicoes, vetorPosicoes, indexFronteiras, vetorFronteiras,
			indexEsquinas, vetorEsquinas, indexCentrosEsquinas, vetorCentrosEsquinas;
	private Map<String, List<Local>> locais;
	private List<Trajeto> trajetos;
	private Map<String, Map<String, List<List<String>>>> arestas;
	private Map<String, List<Ponto>> centroidesEsquinas;
	private Map<String, Map<String, Ponto>> centroidesLotes;
	private List<Integer> indexRotas, vetorRotas;
	private List<Integer> indexTrajetos;
	private List<Integer> indexPeriodos, vetorPeriodos;
	int nRotas, nTrajetos, trajetosBebes, trajetosCriancas, trajetosAdolescentes,
			trajetosJovens, trajetosAdultos, trajetosIdosos;
	private List<Integer> indexTrajetosFaixaEtaria;
	private List<Integer> indexPosicoesRegioes;
	private List<Local> poligonos;
	private List<Local> vertices;
	private List<List<String>> distribuicaoMosquitos;
	private List<CasoInfeccao> distribuicaoHumanos;
	private List<String> vac, fEVac, cicVac, conBio, conAmb;
	private List<Controle> controles;
	private List<List<String>> ponEst;
	private Map<String, List<TipoTrajeto>> tiposTrajetos;
	private Map<Integer, List<ControlePonto>> controlesPontos;
	private Map<Integer, List<Raio>> pontosRaios;
	private List<Integer> indexRaios, vetorRaios;
	private List<Integer> indexControlesPontos, vetorControlesPontos;
	private List<Vacinado> vacs;
	private Map<String, List<Double>> clim;

	public Dengue(Map<String, String> params) {
		this.params = params;

		this.pastaSaida = "./Saidas" + File.separator + params.get("Ambiente") + "_"
				+ params.get("Doenca") + File.separator;
		if (!new File(pastaSaida).exists()) {
			new File(pastaSaida).mkdirs();
		}

		nomeArquivoAmbiental = pastaSaida + "0-AMB.csv";
		nomeArquivoMovimentacao = pastaSaida + "1-MOV.csv";
		nomeArquivoControles = pastaSaida + "2-CON.csv";
		nomeArquivoClimaticos = pastaSaida + "3-CLI.csv";
		nomeArquivoDistribuicaoMosquitos = pastaSaida + "DistribuicaoMosquitos.csv";
		nomeArquivoDistribuicaoHumanos = pastaSaida + "DistribuicaoHumanos.csv";

		con = new ConsultasBanco(params.get("Ambiente"), params.get("Doenca"));
		pontos = con.getPontos();
		criarIndexQuadraseLotes();
		poligonos = con.getPoligonos();
		vertices = poligonos.stream()
				.filter(
						i -> i.getQuadra().equals(GeradorArquivosAmbientais.RUA))
				.map(i -> new Local(i.getLote(), i.getQuadra()))
				.collect(Collectors.toList());
		pontosEsquinas = con.getPontosEsquinas();
		centroidesLotes = con.getCentroidesLotes();
		arestas = con.getArestas();
		centroidesEsquinas = con.getCentroidesEsquinas();
		distribuicaoMosquitos = con.getDistribuicaoMosquitos();
		distribuicaoHumanos = con.getDistribuicaoHumanos();
		vac = con.getQuadrasVacinacao();
		fEVac = con.getFaixasEtariasVacinacao();
		cicVac = con.getCiclosVacinacao();
		controles = con.getControles();
		conBio = con.getQuadrasControleBiologico();
		conAmb = con.getQuadrasControleAmbiental();
		ponEst = con.getLotesPontosEstrategicos();
		tiposTrajetos = con.getTiposTrajetos();
		controlesPontos = con.getControlesPontos();
		pontosRaios = con.getPontosRaios();
		vacs = con.getVacinados();
		clim = con.getClimaticos();
		gerarIndexQuadras();
	}

	private void criarIndexQuadraseLotes() {
		indicesQuadras = new TreeMap<>();
		indicesLotes = new TreeMap<>();

		int idQuadra = 0;
		for (String quadra : pontos.keySet().stream().sorted()
				.collect(Collectors.toList())) {
			int idLote = 0;
			for (String lote : pontos.get(quadra).keySet().stream().sorted()
					.collect(Collectors.toList())) {
				indicesLotes.putIfAbsent(quadra, new TreeMap<>());
				indicesLotes.get(quadra).putIfAbsent(lote, idLote);
				idLote++;
			}
			indicesQuadras.putIfAbsent(quadra, idQuadra);
			idQuadra++;
		}
	}

	private void gerarIndexQuadras() {
		int desl = 0;
		indexQuadras = new ArrayList<>();

		for (String i : indicesQuadras.keySet()) {
			indexQuadras.add(desl);
			desl += indicesLotes.get(i).keySet().size();
			indexQuadras.add(desl);
			desl++;
		}
	}

	private void gerarVizinhancas() {
		List<Vizinhanca> ret = con.getVizinhancasMoorePontos();
		ret.sort(null);

		vizinhancas = new HashMap<>();
		for (Vizinhanca i : ret) {
			vizinhancas.putIfAbsent(i.getFirst().quadra, new HashMap<>());
			vizinhancas.get(i.getFirst().quadra).putIfAbsent(i.getFirst().lote,
					new ArrayList<>());
			vizinhancas.get(i.getFirst().quadra).get(i.getFirst().lote).add(i);
		}
	}

	private void gerarIndexVizinhancas() {
		indexVizinhancas = new ArrayList<>();
		int desl = 0;

		for (String quadra : indicesQuadras.keySet()) {
			indexVizinhancas.add(desl);
			for (String lote : indicesLotes.get(quadra).keySet()) {
				desl += vizinhancas.get(quadra).get(lote).size();
				indexVizinhancas.add(desl);
			}
		}
	}

	private void gerarVetorVizinhancas() {
		vetorVizinhancas = new ArrayList<>();
		for (String quadra : indicesQuadras.keySet()) {
			for (String lote : indicesLotes.get(quadra).keySet()) {
				vizinhancas.get(quadra).get(lote).stream().forEach(k -> {
					vetorVizinhancas.add((int) k.getFirst().getX());
					vetorVizinhancas.add((int) k.getFirst().getY());
					vetorVizinhancas.add((int) k.getSecond().getX());
					vetorVizinhancas.add((int) k.getSecond().getY());
					vetorVizinhancas.add(
							indicesLotes.get(k.getSecond().quadra).get(k.getSecond().lote));
					vetorVizinhancas.add(indicesQuadras.get(k.getSecond().quadra));
				});
			}
		}
	}

	private int getRegiao(char c) {
		int regiao = -1;
		String c1 = Character.toString(c);
		if (c1.equals(GeradorArquivosAmbientais.REGIAO_RUA)) {
			regiao = 0;
		}
		if (c1.equals(GeradorArquivosAmbientais.REGIAO_RURAL)) {
			regiao = 1;
		}
		if (c1.equals(GeradorArquivosAmbientais.REGIAO_QUADRA)) {
			regiao = 2;
		}
		return regiao;
	}

	private void gerarVetorPosicoes() {
		vetorPosicoes = new ArrayList<>();

		int idQuadra = 0;
		for (String quadra : indicesQuadras.keySet()) {
			int idLote = 0;
			for (String lote : indicesLotes.get(quadra).keySet()) {
				for (Ponto ponto : pontos.get(quadra).get(lote)) {
					vetorPosicoes.add((int) ponto.getX());
					vetorPosicoes.add((int) ponto.getY());
					vetorPosicoes.add(idLote);
					vetorPosicoes.add(idQuadra);
				}
				idLote++;
			}
			idQuadra++;
		}
	}

	private void gerarIndexPosicoes() {
		int desl = 0;
		indexPosicoes = new ArrayList<>();

		for (String quadra : indicesQuadras.keySet()) {
			indexPosicoes.add(desl);
			for (String lote : indicesLotes.get(quadra).keySet()) {
				desl += pontos.get(quadra).get(lote).size();
				indexPosicoes.add(desl);
			}
		}
	}

	private void gerarIndexPosicoesRegioes() {
		int regiao, q;
		indexPosicoesRegioes = new ArrayList<>();
		indexPosicoesRegioes.add(0);
		indexPosicoesRegioes.add(0);
		indexPosicoesRegioes.add(0);

		for (String quadra : indicesQuadras.keySet()) {
			regiao = getRegiao(quadra.charAt(0));
			q = indexPosicoesRegioes.get(regiao);
			for (String lote : indicesLotes.get(quadra).keySet()) {
				q += pontos.get(quadra).get(lote).size();
			}
			indexPosicoesRegioes.set(regiao, q);
		}

		indexPosicoesRegioes.add(0, 0);
		for (int i = 1; i < indexPosicoesRegioes.size(); ++i) {
			q = indexPosicoesRegioes.get(i) + indexPosicoesRegioes.get(i - 1);
			indexPosicoesRegioes.set(i, q);
		}
	}

	private void gerarFronteiras() {
		List<Vizinhanca> ret = vizinhancas.entrySet().stream()
				.filter(i -> !i.getKey().equals(GeradorArquivosAmbientais.RUA))
				.flatMap(i -> i.getValue().entrySet().stream())
				.flatMap(i -> i.getValue().stream()).filter(i -> i.getSecond().isRua())
				.collect(Collectors.toList());

		fronteiras = new HashMap<>();
		for (Vizinhanca i : ret) {
			fronteiras.putIfAbsent(i.getFirst().quadra, new HashMap<>());
			fronteiras.get(i.getFirst().quadra).putIfAbsent(i.getFirst().lote,
					new ArrayList<>());
			fronteiras.get(i.getFirst().quadra).get(i.getFirst().lote).add(i);
		}
	}

	private void gerarIndexFronteiras() {
		indexFronteiras = new ArrayList<>();
		int desl = 0;

		for (String quadra : indicesQuadras.keySet()) {
			indexFronteiras.add(desl);
			for (String lote : indicesLotes.get(quadra).keySet()) {
				if (!quadra.equals(GeradorArquivosAmbientais.RUA) \&\& !quadra
						.startsWith(GeradorArquivosAmbientais.REGIAO_RURAL)) {
					desl += fronteiras.get(quadra).get(lote).size();
				}
				indexFronteiras.add(desl);
			}
		}
	}

	private void gerarVetorFronteiras() {
		vetorFronteiras = new ArrayList<>();
		for (String quadra : indicesQuadras.keySet()) {
			for (String lote : indicesLotes.get(quadra).keySet()) {
				if (!quadra.equals(GeradorArquivosAmbientais.RUA) \&\& !quadra
						.startsWith(GeradorArquivosAmbientais.REGIAO_RURAL)) {
					for (Vizinhanca i : fronteiras.get(quadra).get(lote)) {
						vetorFronteiras.add((int) i.getSecond().getX());
						vetorFronteiras.add((int) i.getSecond().getY());
						vetorFronteiras.add((int) indicesLotes.get(i.getSecond().quadra)
								.get(i.getSecond().lote));
					}
				}
			}
		}
	}

	private void gerarIndexEsquinas() {
		indexEsquinas = new ArrayList<>();
		int desl = 0;

		indexEsquinas.add(desl);
		for (String i : pontosEsquinas.keySet().stream().sorted()
				.collect(Collectors.toList())) {
			desl += pontosEsquinas.get(i).size();
			indexEsquinas.add(desl);
		}
	}

	private void gerarVetorEsquinas() {
		vetorEsquinas = pontosEsquinas.entrySet().stream()
				.sorted(Entry.comparingByKey()).flatMap(i -> i.getValue().stream())
				.flatMap(i -> Stream.of((int) i.getX(), (int) i.getY(),
						(int) indicesLotes.get(i.quadra).get(i.lote)))
				.collect(Collectors.toList());
	}

	private void gerarIndexCentrosEsquinas() {
		indexCentrosEsquinas = new ArrayList<>();
		int desl = 0;

		indexCentrosEsquinas.add(desl);
		for (String i : indicesLotes.get(GeradorArquivosAmbientais.RUA)
				.keySet()) {
			if (centroidesEsquinas.containsKey(i)) {
				desl += centroidesEsquinas.get(i).size();
			}
			indexCentrosEsquinas.add(desl);
		}
	}

	private void gerarVetorCentrosEsquinas() {
		vetorCentrosEsquinas = centroidesEsquinas.entrySet().stream()
				.sorted(Entry.comparingByKey()).flatMap(i -> i.getValue().stream())
				.flatMap(i -> Stream.of((int) i.getX(), (int) i.getY(),
						(int) indicesLotes.get(i.quadra).get(i.lote)))
				.collect(Collectors.toList());
	}

	private void distribuirLocais() {
		List<Local> lotes = poligonos.stream()
				.filter(i -> i.getQuadra()
						.startsWith(GeradorArquivosAmbientais.REGIAO_QUADRA))
				.collect(Collectors.toList());

		Collections.shuffle(lotes);
		List<Local> locaisCasa = new ArrayList<>(
				lotes.subList(0, Integer.parseInt(params.get("LotesCasa"))));
		lotes.removeAll(locaisCasa);

		Collections.shuffle(lotes);
		List<Local> locaisTrabalho = new ArrayList<>(
				lotes.subList(0, Integer.parseInt(params.get("LotesTrabalho"))));
		lotes.removeAll(locaisTrabalho);

		Collections.shuffle(lotes);
		List<Local> locaisLazer = new ArrayList<>(
				lotes.subList(0, Integer.parseInt(params.get("LotesLazer"))));
		lotes.removeAll(locaisLazer);

		Collections.shuffle(lotes);
		List<Local> locaisEstudo = new ArrayList<>(
				lotes.subList(0, Integer.parseInt(params.get("LotesEstudo"))));
		lotes.removeAll(locaisEstudo);

		locais = new HashMap<>();
		locais.put("Casa", locaisCasa);
		locais.put("Trabalho", locaisTrabalho);
		locais.put("Lazer", locaisLazer);
		locais.put("Estudo", locaisEstudo);
	}

	private Trajeto gerarTrajeto(int faixaEtariaTrajeto, TipoTrajeto tipoTrajeto,
			Local casa) {
		Trajeto trajeto = new Trajeto(faixaEtariaTrajeto);
		int duracaoDia = Integer.parseInt(params.get("DuracaoDia"));
		List<LocalPeriodo> locaisPeriodos = tipoTrajeto.getLocaisPeriodos();

		LocalPeriodo lp1, lp2;
		String local1, local2;
		int periodo1, periodo2;
		Local l1, l2;

		for (int i = 0; i < locaisPeriodos.size() - 1; ++i) {
			lp1 = locaisPeriodos.get(i);
			lp2 = locaisPeriodos.get(i + 1);

			local1 = lp1.getLocal();
			periodo1 = (int) (lp1.getPeriodo() * duracaoDia);
			local2 = lp2.getLocal();
			periodo2 = (int) (lp2.getPeriodo() * duracaoDia);

			if (local1.equals("Casa")) {
				l1 = new Local(casa, periodo1);
			} else {
				l1 = new Local(locais.get(local1)
						.get((int) (Math.random() * locais.get(local1).size())), periodo1);
			}

			if (local2.equals("Casa")) {
				l2 = new Local(casa, periodo2);
			} else {
				l2 = new Local(locais.get(local2)
						.get((int) (Math.random() * locais.get(local2).size())), periodo2);
			}

			trajeto.add(new Rota(l1, l2));
		}

		return trajeto;
	}

	private void gerarTrajetos() {
		trajetos = new ArrayList<>();

		int index;
		TipoTrajeto tipoTrajeto;

		for (Local casa : locais.get("Casa")) {
			index = (int) (Math.random() * tiposTrajetos.get("Bebe").size());
			tipoTrajeto = tiposTrajetos.get("Bebe").get(index);
			trajetos.add(gerarTrajeto(Trajeto.BEBE, tipoTrajeto, casa));

			index = (int) (Math.random() * tiposTrajetos.get("Crianca").size());
			tipoTrajeto = tiposTrajetos.get("Crianca").get(index);
			trajetos.add(gerarTrajeto(Trajeto.CRIANCA, tipoTrajeto, casa));

			index = (int) (Math.random() * tiposTrajetos.get("Adolescente").size());
			tipoTrajeto = tiposTrajetos.get("Adolescente").get(index);
			trajetos.add(gerarTrajeto(Trajeto.ADOLESCENTE, tipoTrajeto, casa));

			index = (int) (Math.random() * tiposTrajetos.get("Jovem").size());
			tipoTrajeto = tiposTrajetos.get("Jovem").get(index);
			trajetos.add(gerarTrajeto(Trajeto.JOVEM, tipoTrajeto, casa));

			index = (int) (Math.random() * tiposTrajetos.get("Adulto").size());
			tipoTrajeto = tiposTrajetos.get("Adulto").get(index);
			trajetos.add(gerarTrajeto(Trajeto.ADULTO, tipoTrajeto, casa));

			index = (int) (Math.random() * tiposTrajetos.get("Idoso").size());
			tipoTrajeto = tiposTrajetos.get("Idoso").get(index);
			trajetos.add(gerarTrajeto(Trajeto.IDOSO, tipoTrajeto, casa));
		}
	}

	private List<Local> aEstrelaConstruirCaminho(
			Map<Local, Local> verticesAnteriores, Local verticeAtual) {
		List<Local> caminho = new ArrayList<>();
		caminho.add(verticeAtual);
		while (verticesAnteriores.keySet().contains(verticeAtual)) {
			verticeAtual = verticesAnteriores.get(verticeAtual);
			if (verticeAtual != null) {
				caminho.add(0, verticeAtual);
			}
		}
		return caminho;
	}

	private void aEstrela(Rota rota) {
		Local verticeOrigem = rota.getLocalInicial();
		Local verticeDestino = rota.getLocalFinal();

		Set<Local> verticesJaVisitados = new HashSet<>();

		Set<Local> verticesNaoVisitados = new HashSet<>();

		List<String> primeiraRuaAux = arestas.get(verticeOrigem.getQuadra())
				.get(verticeOrigem.getLote()).stream()
				.filter(i -> i.get(3).equals(GeradorArquivosAmbientais.RUA))
				.findFirst().get();
		Local primeiraRua = new Local(primeiraRuaAux.get(3), primeiraRuaAux.get(2));
		verticesNaoVisitados.add(primeiraRua);

		Map<Local, Local> verticesAnteriores = new HashMap<>();
		for (Local vertice : vertices) {
			verticesAnteriores.put(vertice, null);
		}

		Map<Local, Double> distanciaOrigemAoAtual = new HashMap<>();
		for (Local vertice : vertices) {
			distanciaOrigemAoAtual.put(vertice, Double.MAX_VALUE);
		}
		distanciaOrigemAoAtual.put(primeiraRua, 0.0);

		Map<Local, Double> distanciaAtualAoDestino = new HashMap<>();
		for (Local vertice : vertices) {
			distanciaAtualAoDestino.put(vertice, Double.MAX_VALUE);
		}
		Ponto pontoOrigem = centroidesLotes.get(verticeOrigem.getQuadra())
				.get(verticeOrigem.getLote());
		Ponto pontoDestino = centroidesLotes.get(verticeDestino.getQuadra())
				.get(verticeDestino.getLote());
		distanciaAtualAoDestino.put(primeiraRua,
				pontoOrigem.distancia(pontoDestino));

		while (!verticesNaoVisitados.isEmpty()) {

			Local atual = verticesNaoVisitados.stream()
					.collect(Collectors
							.minBy((i, j) -> Double.compare(distanciaAtualAoDestino.get(i),
									distanciaAtualAoDestino.get(j))))
					.get();

			if (arestas.get(atual.getQuadra()).get(atual.getLote()).stream()
					.filter(i -> i.get(2).equals(verticeDestino.getLote())
							\&\& i.get(3).equals(verticeDestino.getQuadra()))
					.count() > 0) {
				rota.setRuas(aEstrelaConstruirCaminho(verticesAnteriores, atual));
				return;
			}

			verticesNaoVisitados.remove(atual);
			verticesJaVisitados.add(atual);

			for (Ponto vizinhoAux : centroidesEsquinas.get(atual.getLote()).stream()
					.collect(Collectors.toList())) {
				Local vizinho = new Local(vizinhoAux.quadra, vizinhoAux.lote);

				if (verticesJaVisitados.contains(vizinho)) {
					continue;
				}
				Ponto pontoDestinoAux = centroidesLotes.get(verticeDestino.getQuadra())
						.get(verticeDestino.getLote());
				pontoOrigem = centroidesEsquinas.get(atual.getLote()).stream()
						.collect(Collectors
								.minBy((i, j) -> Double.compare(i.distancia(pontoDestinoAux),
										j.distancia(pontoDestinoAux))))
						.get();
				pontoDestino = vizinhoAux;
				double novaDistancia = distanciaOrigemAoAtual.get(atual)
						+ pontoOrigem.distancia(pontoDestino);
				if (!verticesNaoVisitados.contains(vizinho)) {
					verticesNaoVisitados.add(vizinho);
				} else {
					if (novaDistancia >= distanciaOrigemAoAtual.get(vizinho)) {
						continue;
					}
				}
				verticesAnteriores.put(vizinho, atual);
				distanciaOrigemAoAtual.put(vizinho, novaDistancia);
				pontoOrigem = vizinhoAux;
				pontoDestino = centroidesLotes.get(verticeDestino.getQuadra())
						.get(verticeDestino.getLote());
				distanciaAtualAoDestino.put(primeiraRua,
						pontoOrigem.distancia(pontoDestino));
				distanciaAtualAoDestino.put(vizinho, distanciaOrigemAoAtual.get(vizinho)
						+ pontoOrigem.distancia(pontoDestino));
			}
		}
		System.err.println(
				verticeOrigem + " -> " + verticeDestino + " : Retornando NULL");
		System.exit(1);
	}

	private void gerarCaminhos() {
		trajetos.stream().flatMap(i -> i.streamTrajeto()).parallel()
				.forEach(this::aEstrela);
	}

	private void gerarVetorIndexRotas() {
		Map<Integer, List<List<Integer>>> rotas = new HashMap<>();
		rotas.put(Trajeto.BEBE, new ArrayList<>());
		rotas.put(Trajeto.CRIANCA, new ArrayList<>());
		rotas.put(Trajeto.ADOLESCENTE, new ArrayList<>());
		rotas.put(Trajeto.JOVEM, new ArrayList<>());
		rotas.put(Trajeto.ADULTO, new ArrayList<>());
		rotas.put(Trajeto.IDOSO, new ArrayList<>());

		for (Trajeto trajeto : trajetos) {

			for (Rota rota : trajeto.getTrajeto()) {

				Local localOrigem = rota.getLocalInicial();
				Local localDestino = rota.getLocalFinal();

				int quadraOrigem = indicesQuadras.get(localOrigem.getQuadra());
				int loteOrigem = indicesLotes.get(localOrigem.getQuadra())
						.get(localOrigem.getLote());

				int quadraDestino = indicesQuadras.get(localDestino.getQuadra());
				int loteDestino = indicesLotes.get(localDestino.getQuadra())
						.get(localDestino.getLote());

				List<Integer> rota2 = new ArrayList<>();

				rota2.add(loteOrigem);
				rota2.add(quadraOrigem);
				rota2.add(loteDestino);
				rota2.add(quadraDestino);
				for (Local rua : rota.getRuas()) {
					rota2.add(indicesLotes.get(GeradorArquivosAmbientais.RUA)
							.get(rua.getLote()));
				}

				rotas.get(trajeto.getTipoTrajeto()).add(rota2);
			}
		}

		indexRotas = new ArrayList<>();
		int desl = 0;
		indexRotas.add(desl);
		for (List<Integer> i : rotas.get(Trajeto.BEBE)) {
			desl += i.size();
			indexRotas.add(desl);
		}
		for (List<Integer> i : rotas.get(Trajeto.CRIANCA)) {
			desl += i.size();
			indexRotas.add(desl);
		}
		for (List<Integer> i : rotas.get(Trajeto.ADOLESCENTE)) {
			desl += i.size();
			indexRotas.add(desl);
		}
		for (List<Integer> i : rotas.get(Trajeto.JOVEM)) {
			desl += i.size();
			indexRotas.add(desl);
		}
		for (List<Integer> i : rotas.get(Trajeto.ADULTO)) {
			desl += i.size();
			indexRotas.add(desl);
		}
		for (List<Integer> i : rotas.get(Trajeto.IDOSO)) {
			desl += i.size();
			indexRotas.add(desl);
		}

		nRotas = rotas.get(Trajeto.BEBE).size() + rotas.get(Trajeto.CRIANCA).size()
				+ rotas.get(Trajeto.ADOLESCENTE).size()
				+ rotas.get(Trajeto.JOVEM).size() + rotas.get(Trajeto.ADULTO).size()
				+ rotas.get(Trajeto.IDOSO).size();

		vetorRotas = new ArrayList<>();
		vetorRotas.addAll(rotas.get(Trajeto.BEBE).stream().flatMap(i -> i.stream())
				.collect(Collectors.toList()));
		vetorRotas.addAll(rotas.get(Trajeto.CRIANCA).stream()
				.flatMap(i -> i.stream()).collect(Collectors.toList()));
		vetorRotas.addAll(rotas.get(Trajeto.ADOLESCENTE).stream()
				.flatMap(i -> i.stream()).collect(Collectors.toList()));
		vetorRotas.addAll(rotas.get(Trajeto.JOVEM).stream().flatMap(i -> i.stream())
				.collect(Collectors.toList()));
		vetorRotas.addAll(rotas.get(Trajeto.ADULTO).stream()
				.flatMap(i -> i.stream()).collect(Collectors.toList()));
		vetorRotas.addAll(rotas.get(Trajeto.IDOSO).stream().flatMap(i -> i.stream())
				.collect(Collectors.toList()));
	}

	private void gerarIndexTrajetos() {
		indexTrajetos = new ArrayList<>();
		int desl = 0;
		indexTrajetos.add(desl);
		for (Trajeto i : trajetos) {
			desl += i.getTrajeto().size();
			indexTrajetos.add(desl);
		}

		trajetosBebes = (int) trajetos.stream().filter(i -> i.isTipoBebe()).count();
		trajetosCriancas = (int) trajetos.stream().filter(i -> i.isTipoCrianca())
				.count();
		trajetosAdolescentes = (int) trajetos.stream()
				.filter(i -> i.isTipoAdolescente()).count();
		trajetosJovens = (int) trajetos.stream().filter(i -> i.isTipoJovem())
				.count();
		trajetosAdultos = (int) trajetos.stream().filter(i -> i.isTipoAdulto())
				.count();
		trajetosIdosos = (int) trajetos.stream().filter(i -> i.isTipoIdoso())
				.count();

		nTrajetos = trajetosBebes + trajetosCriancas + trajetosAdolescentes
				+ trajetosJovens + trajetosAdultos + trajetosIdosos;
	}

	private void gerarVetorIndexPeriodos() {
		for (Trajeto trajeto : trajetos) {
			trajeto.add(trajeto.getTrajeto().get(0).getLocalInicial().getPeriodo());

			for (Rota rota : trajeto.getTrajeto()) {
				trajeto.add(rota.getLocalFinal().getPeriodo());
			}
		}

		indexPeriodos = new ArrayList<>();
		int desl = 0;
		indexPeriodos.add(desl);
		for (Trajeto trajeto : trajetos) {
			desl += trajeto.getPeriodos().size();
			indexPeriodos.add(desl);
		}

		vetorPeriodos = trajetos.stream().flatMap(i -> i.streamPeriodos())
				.collect(Collectors.toList());
	}

	private void gerarIndexTrajetosFaixaEtaria() {
		indexTrajetosFaixaEtaria = new ArrayList<>();
		int desl = 0;
		indexTrajetosFaixaEtaria.add(desl);
		desl += trajetosBebes;
		indexTrajetosFaixaEtaria.add(desl);
		desl += trajetosCriancas;
		indexTrajetosFaixaEtaria.add(desl);
		desl += trajetosAdolescentes;
		indexTrajetosFaixaEtaria.add(desl);
		desl += trajetosJovens;
		indexTrajetosFaixaEtaria.add(desl);
		desl += trajetosAdultos;
		indexTrajetosFaixaEtaria.add(desl);
		desl += trajetosIdosos;
		indexTrajetosFaixaEtaria.add(desl);
	}

	private void gerarIndexControlesPontos() {
		int desl = 0;
		indexControlesPontos = new ArrayList<>();

		indexControlesPontos.add(desl);
		for (Integer idControle : controlesPontos.keySet()) {
			desl += controlesPontos.get(idControle).size();
			indexControlesPontos.add(desl);
		}
	}

	private void gerarVetorControlesPontos() {
		vetorControlesPontos = new ArrayList<>();

		for (Integer idControle : controlesPontos.keySet()) {
			for (ControlePonto ponto : controlesPontos.get(idControle)) {
				vetorControlesPontos.add((int) ponto.getX());
				vetorControlesPontos.add((int) ponto.getY());
				vetorControlesPontos
						.add(indicesLotes.get(ponto.getQuadra()).get(ponto.getLote()));
				vetorControlesPontos.add(indicesQuadras.get(ponto.getQuadra()));
			}
		}
	}

	private void gerarIndexRaios() {
		indexRaios = new ArrayList<>();
		int desl = 0;

		indexRaios.add(desl);
		for (Integer idControle : controlesPontos.keySet()) {
			if (pontosRaios.containsKey(idControle)) {
				desl += pontosRaios.get(idControle).size();
			}
			indexRaios.add(desl);
		}
	}

	private void gerarVetorRaios() {
		vetorRaios = new ArrayList<>();
		for (Integer idControle : pontosRaios.keySet()) {
			pontosRaios.get(idControle).stream().forEach(k -> {
				vetorRaios.add((int) k.getX());
				vetorRaios.add((int) k.getY());
				vetorRaios.add(indicesLotes.get(k.getQuadra()).get(k.getLote()));
				vetorRaios.add(indicesQuadras.get(k.getQuadra()));
			});
		}
	}

	private void salvarArquivoVetores() {
		try {
			BufferedWriter esc = new BufferedWriter(
					new FileWriter(nomeArquivoAmbiental));

			esc.write("quantQuadras, quantLotes e indexQuadras\\n");
			esc.write(pontos.keySet().size() + "\\n");
			esc.write(pontos.entrySet().stream().sorted(Entry.comparingByKey())
					.map(i -> Integer.toString(i.getValue().size()))
					.collect(Collectors.joining(";")) + "\\n");
			esc.write(indexQuadras.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\\n\\n");

			esc.write("indexVizinhancas e vetorVizinhancas\\n");

			for (int i = 0; i < indexVizinhancas.size(); ++i) {
				esc.write(Integer.toString(indexVizinhancas.get(i)));
				if (i < indexVizinhancas.size() - 1) {
					esc.write(";");
				}
			}
			esc.write("\\n");

			for (int i = 0; i < vetorVizinhancas.size(); ++i) {
				esc.write(Integer.toString(vetorVizinhancas.get(i)));
				if (i < vetorVizinhancas.size() - 1) {
					esc.write(";");
				}
			}
			esc.write("\\n\\n");

			esc.write("indexPosicoes, vetorPosicoes, indexPosicoesRegioes\\n");
			esc.write(indexPosicoes.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\\n");

			for (int i = 0; i < vetorPosicoes.size(); ++i) {
				esc.write(Integer.toString(vetorPosicoes.get(i)));
				if (i < vetorPosicoes.size() - 1) {
					esc.write(";");
				}
			}
			esc.write("\\n");

			esc.write(indexPosicoesRegioes.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\\n\\n");

			esc.write("indexFronteiras e vetorFronteiras\\n");
			esc.write(indexFronteiras.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\\n");
			esc.write(vetorFronteiras.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\\n\\n");

			esc.write("indexEsquinas e vetorEsquinas\\n");
			esc.write(indexEsquinas.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\\n");
			esc.write(vetorEsquinas.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\\n\\n");

			esc.write("indexCentrosEsquinas e vetorCentrosEsquinas\\n");
			esc.write(indexCentrosEsquinas.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\\n");
			esc.write(vetorCentrosEsquinas.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\\n");

			esc.close();

			esc = new BufferedWriter(new FileWriter(nomeArquivoMovimentacao));

			esc.write("quantRotas, indexRotas e vetorRotas\\n");
			esc.write(nRotas + "\\n");

			for (int i = 0; i < indexRotas.size(); ++i) {
				esc.write(Integer.toString(indexRotas.get(i)));
				if (i < indexRotas.size() - 1) {
					esc.write(";");
				}
			}
			esc.write("\\n");

			for (int i = 0; i < vetorRotas.size(); ++i) {
				esc.write(Integer.toString(vetorRotas.get(i)));
				if (i < vetorRotas.size() - 1) {
					esc.write(";");
				}
			}
			esc.write("\\n\\n");

			esc.write("quantTrajetos e indexTrajetos\\n");
			esc.write(nTrajetos + "\\n");
			esc.write(indexTrajetos.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\\n\\n");

			esc.write("indexPeriodos e vetorPeriodos\\n");

			for (int i = 0; i < indexPeriodos.size(); ++i) {
				esc.write(Integer.toString(indexPeriodos.get(i)));
				if (i < indexPeriodos.size() - 1) {
					esc.write(";");
				}
			}
			esc.write("\\n");

			for (int i = 0; i < vetorPeriodos.size(); ++i) {
				esc.write(Integer.toString(vetorPeriodos.get(i)));
				if (i < vetorPeriodos.size() - 1) {
					esc.write(";");
				}
			}
			esc.write("\\n\\n");

			esc.write("indexTrajetosFaixaEtaria\\n");
			esc.write(indexTrajetosFaixaEtaria.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\\n");

			esc.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void salvarArquivoDistribuicaoMosquitos() {
		double percInsInfec = Double
				.parseDouble(params.get("PorcentagemCasosFemeas"));

		Map<Integer, List<CasoInfeccao>> dados = distribuicaoHumanos.stream()
				.collect(Collectors.groupingBy(CasoInfeccao::getDia));

		List<Entry<Integer, List<CasoInfeccao>>> q1 = dados.entrySet().stream()
				.filter(i -> i.getKey() >= 0 \&\& i.getKey() <= 91)
				.collect(Collectors.toList());
		List<Entry<Integer, List<CasoInfeccao>>> q2 = dados.entrySet().stream()
				.filter(i -> i.getKey() >= 92 \&\& i.getKey() <= 182)
				.collect(Collectors.toList());
		List<Entry<Integer, List<CasoInfeccao>>> q3 = dados.entrySet().stream()
				.filter(i -> i.getKey() >= 183 \&\& i.getKey() <= 273)
				.collect(Collectors.toList());
		List<Entry<Integer, List<CasoInfeccao>>> q4 = dados.entrySet().stream()
				.filter(i -> i.getKey() >= 274 \&\& i.getKey() <= 365)
				.collect(Collectors.toList());

		int nq1 = q1.stream()
				.collect(Collectors.summingInt(i -> i.getValue().size()));
		int nq2 = q2.stream()
				.collect(Collectors.summingInt(i -> i.getValue().size()));
		int nq3 = q3.stream()
				.collect(Collectors.summingInt(i -> i.getValue().size()));
		int nq4 = q4.stream()
				.collect(Collectors.summingInt(i -> i.getValue().size()));

		nq1 = (int) Math.round(nq1 * percInsInfec);
		nq2 = (int) Math.round(nq2 * percInsInfec);
		nq3 = (int) Math.round(nq3 * percInsInfec);
		nq4 = (int) Math.round(nq4 * percInsInfec);

		List<Integer> lq1 = new ArrayList<>();
		List<Integer> lq2 = new ArrayList<>();
		List<Integer> lq3 = new ArrayList<>();
		List<Integer> lq4 = new ArrayList<>();

		for (Entry<Integer, List<CasoInfeccao>> i : q1) {
			for (int j = 0; j < i.getValue().size(); ++j) {
				lq1.add(i.getKey());
			}
		}
		for (Entry<Integer, List<CasoInfeccao>> i : q2) {
			for (int j = 0; j < i.getValue().size(); ++j) {
				lq2.add(i.getKey());
			}
		}
		for (Entry<Integer, List<CasoInfeccao>> i : q3) {
			for (int j = 0; j < i.getValue().size(); ++j) {
				lq3.add(i.getKey());
			}
		}
		for (Entry<Integer, List<CasoInfeccao>> i : q4) {
			for (int j = 0; j < i.getValue().size(); ++j) {
				lq4.add(i.getKey());
			}
		}

		List<Integer> dados2 = new ArrayList<>();
		Collections.shuffle(lq1);
		dados2.addAll(lq1.subList(0, nq1));
		Collections.shuffle(lq2);
		dados2.addAll(lq2.subList(0, nq2));
		Collections.shuffle(lq3);
		dados2.addAll(lq3.subList(0, nq3));
		Collections.shuffle(lq4);
		dados2.addAll(lq4.subList(0, nq4));

		dados2.sort(null);

		List<Ponto> pontos2 = pontos.entrySet().stream().map(i -> i.getValue())
				.flatMap(i -> i.entrySet().stream()).flatMap(i -> i.getValue().stream())
				.collect(Collectors.toList());

		try {
			BufferedWriter esc = new BufferedWriter(
					new FileWriter(nomeArquivoDistribuicaoMosquitos));

			esc.write((distribuicaoMosquitos.size() + dados2.size()) + "\\n");
			esc.write("Quadra;QuantidadeTotal;Sexo;Fase;PercentualMinimoInfectados;");
			esc.write("PercentualMaximoInfectados;Sorotipo;Ciclo\\n");
			for (List<String> i : distribuicaoMosquitos) {
				esc.write(indicesQuadras.get(i.get(0)) + ";");
				esc.write(i.stream().skip(1).map(j -> j.toString())
						.collect(Collectors.joining(";")) + "\\n");
			}

			for (int ciclo : dados2) {
				Ponto ponto = pontos2.get((int) (Math.random() * pontos2.size()));

				esc.write(indicesQuadras.get(ponto.quadra) + ";");
				esc.write("1;");
				esc.write("F;");
				esc.write("A;");
				esc.write("1.0;");
				esc.write("1.0;");
				esc.write("2;");
				esc.write(ciclo + "\\n");
			}
			esc.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void salvarArquivoDistribuicaoHumanos() {
		double percInsInfec = Double
				.parseDouble(params.get("PorcentagemCasosHumanos"));

		Map<String, Double> percPop = new HashMap<>();
		percPop.put("B", Double.parseDouble(params.get("FracaoBebesMasculinos")));
		percPop.put("C",
				Double.parseDouble(params.get("FracaoCriancasMasculinas")));
		percPop.put("D",
				Double.parseDouble(params.get("FracaoAdolescentesMasculinos")));
		percPop.put("J", Double.parseDouble(params.get("FracaoJovensMasculinos")));
		percPop.put("A", Double.parseDouble(params.get("FracaoAdultosMasculinos")));
		percPop.put("I", Double.parseDouble(params.get("FracaoIdososMasculinos")));

		Map<Integer, List<CasoInfeccao>> dados = distribuicaoHumanos.stream()
				.collect(Collectors.groupingBy(CasoInfeccao::getDia));

		List<Entry<Integer, List<CasoInfeccao>>> q1 = dados.entrySet().stream()
				.filter(i -> i.getKey() >= 0 \&\& i.getKey() <= 91)
				.collect(Collectors.toList());
		List<Entry<Integer, List<CasoInfeccao>>> q2 = dados.entrySet().stream()
				.filter(i -> i.getKey() >= 92 \&\& i.getKey() <= 182)
				.collect(Collectors.toList());
		List<Entry<Integer, List<CasoInfeccao>>> q3 = dados.entrySet().stream()
				.filter(i -> i.getKey() >= 183 \&\& i.getKey() <= 273)
				.collect(Collectors.toList());
		List<Entry<Integer, List<CasoInfeccao>>> q4 = dados.entrySet().stream()
				.filter(i -> i.getKey() >= 274 \&\& i.getKey() <= 365)
				.collect(Collectors.toList());

		int nq1 = q1.stream()
				.collect(Collectors.summingInt(i -> i.getValue().size()));
		int nq2 = q2.stream()
				.collect(Collectors.summingInt(i -> i.getValue().size()));
		int nq3 = q3.stream()
				.collect(Collectors.summingInt(i -> i.getValue().size()));
		int nq4 = q4.stream()
				.collect(Collectors.summingInt(i -> i.getValue().size()));

		nq1 = (int) Math.round(nq1 * percInsInfec);
		nq2 = (int) Math.round(nq2 * percInsInfec);
		nq3 = (int) Math.round(nq3 * percInsInfec);
		nq4 = (int) Math.round(nq4 * percInsInfec);

		List<CasoInfeccao> lq1 = new ArrayList<>();
		List<CasoInfeccao> lq2 = new ArrayList<>();
		List<CasoInfeccao> lq3 = new ArrayList<>();
		List<CasoInfeccao> lq4 = new ArrayList<>();

		for (Entry<Integer, List<CasoInfeccao>> i : q1) {
			lq1.addAll(i.getValue());
		}
		for (Entry<Integer, List<CasoInfeccao>> i : q2) {
			lq2.addAll(i.getValue());
		}
		for (Entry<Integer, List<CasoInfeccao>> i : q3) {
			lq3.addAll(i.getValue());
		}
		for (Entry<Integer, List<CasoInfeccao>> i : q4) {
			lq4.addAll(i.getValue());
		}

		List<CasoInfeccao> dados2 = new ArrayList<>();
		Collections.shuffle(lq1);
		dados2.addAll(lq1.subList(0, nq1));
		Collections.shuffle(lq2);
		dados2.addAll(lq2.subList(0, nq2));
		Collections.shuffle(lq3);
		dados2.addAll(lq3.subList(0, nq3));
		Collections.shuffle(lq4);
		dados2.addAll(lq4.subList(0, nq4));

		Map<String, List<CasoInfeccao>> dados3 = dados2.stream()
				.collect(Collectors.groupingBy(CasoInfeccao::getFaixaEtaria));

		for (Entry<String, List<CasoInfeccao>> entry : dados3.entrySet()) {
			String key = entry.getKey();
			List<CasoInfeccao> value = entry.getValue();

			int nM = (int) (value.size() * percPop.get(key));
			Collections.shuffle(value);
			value.stream().forEach(i -> i.setSexo("F"));
			value.subList(0, nM).stream().forEach(i -> i.setSexo("M"));
		}

		List<CasoInfeccao> dados4 = dados3.entrySet().stream()
				.flatMap(i -> i.getValue().stream()).collect(Collectors.toList());
		dados4.sort(Comparator.comparing(CasoInfeccao::getDia));

		List<Ponto> pontos2 = pontos.entrySet().stream().map(i -> i.getValue())
				.flatMap(i -> i.entrySet().stream()).flatMap(i -> i.getValue().stream())
				.collect(Collectors.toList());
		for (CasoInfeccao caso : dados4) {
			Ponto ponto = pontos2.parallelStream()
					.collect(Collectors
							.minBy((i, j) -> Double.compare(i.distancia(caso.getPonto()),
									j.distancia(caso.getPonto()))))
					.get();
			caso.setX((int) ponto.getX());
			caso.setY((int) ponto.getY());
			caso.setQuadra(indicesQuadras.get(ponto.quadra));
			caso.setLote(indicesLotes.get(ponto.quadra).get(ponto.lote));
		}

		try {
			BufferedWriter esc = new BufferedWriter(
					new FileWriter(nomeArquivoDistribuicaoHumanos));
			esc.write(dados4.size() + "\\n");
			esc.write("Q;L;X;Y;Sexo;FaixaEtaria;SaudeDengue;SorotipoAtual;Ciclo\\n");
			for (CasoInfeccao caso : dados4) {
				esc.write(caso.getQuadra() + ";");
				esc.write(caso.getLote() + ";");
				esc.write(caso.getX() + ";");
				esc.write(caso.getY() + ";");
				esc.write(caso.getSexo() + ";");
				esc.write(caso.getFaixaEtaria() + ";");
				esc.write("I;");
				esc.write("2;");
				esc.write(caso.getDia() + "\\n");
			}
			esc.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void salvarArquivoControles() {
		Map<Integer, List<CasoInfeccao>> casosPorDia = distribuicaoHumanos.stream()
				.collect(Collectors.groupingBy(CasoInfeccao::getDia));

		int duracaoAno = Integer.parseInt(params.get("DuracaoAno"));

		for (int i = 0; i < duracaoAno; ++i) {
			if (!casosPorDia.containsKey(i)) {
				casosPorDia.put(i, new ArrayList<>());
			}
		}

		Map<Integer, Integer> casosAcumulados = new HashMap<>();

		int soma = casosPorDia.get(0).size();
		casosAcumulados.put(0, soma);
		for (int i = 1; i < casosPorDia.entrySet().size(); ++i) {
			soma += casosPorDia.get(i).size();
			casosAcumulados.put(i, soma);
		}

		double max = casosAcumulados.values().stream().max(Integer::compareTo)
				.get();
		max = (max == 0 ? 1 : max);

		Map<Integer, Double> percentuais = new HashMap<>();
		for (Entry<Integer, Integer> i : casosAcumulados.entrySet()) {
			double v = (max - i.getValue()) / max;
			percentuais.put(i.getKey(), v);
		}

		try {
			BufferedWriter esc = new BufferedWriter(
					new FileWriter(nomeArquivoControles));

			esc.write("quadVac. Quadras em que serao aplicadas a vacinacao\\n");
			esc.write(vac.size() + "\\n");
			esc.write(vac.stream().map(i -> Integer.toString(indicesQuadras.get(i)))
					.collect(Collectors.joining(";")) + "\\n\\n");

			esc.write("fEVac. Faixas etarias que receberao vacinacao\\n");
			esc.write(fEVac.size() + "\\n");
			esc.write(fEVac.stream().collect(Collectors.joining(";")) + "\\n\\n");

			esc.write("cicVac. Ciclos em que as vacinacoes serao executadas\\n");
			esc.write(cicVac.size() + "\\n");
			esc.write(cicVac.stream().collect(Collectors.joining(";")) + "\\n\\n");

			esc.write("conBio. Quadras com controle biologico\\n");
			esc.write(conBio.size() + "\\n");
			esc.write(
					conBio.stream().map(i -> Integer.toString(indicesQuadras.get(i)))
							.collect(Collectors.joining(";")) + "\\n\\n");

			esc.write("conAmb. Quadras com controle ambiental\\n");
			esc.write(conAmb.size() + "\\n");
			esc.write(
					conAmb.stream().map(i -> Integer.toString(indicesQuadras.get(i)))
							.collect(Collectors.joining(";")) + "\\n\\n");

			esc.write("pontEst. Lotes que sao pontos estrategicos\\n");
			esc.write((ponEst.size() * 2) + "\\n");
			esc.write(ponEst.stream()
					.flatMap(
							i -> Stream.of(Integer.toString(indicesQuadras.get(i.get(0))),
									Integer.toString(indicesLotes.get(i.get(0)).get(i.get(1)))))
					.collect(Collectors.joining(";")) + "\\n\\n");

			esc.write("Complemento\\n");
			esc.write(percentuais.size() + "\\n");
			esc.write(percentuais.entrySet().stream()
					.map(i -> String.format("%.10f", i.getValue()).replace(",", "."))
					.collect(Collectors.joining(";")) + "\\n\\n");

			esc.write("Casos\\n");
			esc.write((casosPorDia.size() + 1) + "\\n");
			esc.write("1;");
			esc.write(casosPorDia.entrySet().stream()
					.map(i -> Integer.toString(i.getValue().size()))
					.collect(Collectors.joining(";")) + "\\n\\n");

			esc.write("contr. Informacoes sobre controles\\n");
			esc.write(controles.size() + "\\n");
			for (Controle c : controles) {
				esc.write(indicesQuadras.get(c.getQuadra()) + ";");
				esc.write(c.getCiclo() + ";");
				esc.write(c.getTipoControle().charAt(0) + ";");
				esc.write(c.getTaxaMinMecanico() + ";");
				esc.write(c.getTaxaMaxMecanico() + ";");
				esc.write(c.getTaxaMinQuimico() + ";");
				esc.write(c.getTaxaMaxQuimico() + "\\n");
			}
			esc.write("\\n");

			esc.write(
					"indContrPontos e contrPontos. Pontos com controles para mosquitos\\n");
			esc.write(indexControlesPontos.size() + "\\n");
			esc.write(indexControlesPontos.stream().map(i -> Integer.toString(i))
					.collect(Collectors.joining(";")) + "\\n");
			esc.write(vetorControlesPontos.stream().map(i -> Integer.toString(i))
					.collect(Collectors.joining(";")) + "\\n\\n");

			esc.write("indRaios e raios. Pontos de raio\\n");
			esc.write(indexRaios.size() + "\\n");
			esc.write(indexRaios.stream().map(i -> Integer.toString(i))
					.collect(Collectors.joining(";")) + "\\n");
			esc.write(vetorRaios.stream().map(i -> Integer.toString(i))
					.collect(Collectors.joining(";")) + "\\n\\n");

			esc.write("vacs. Humanos vacinados. \\n");
			esc.write(vacs.size() + "\\n");
			for (Vacinado v : vacs) {
				esc.write(v.getCiclo() + ";");
				esc.write(indicesQuadras.get(v.getQuadra()) + ";");
				esc.write(indicesLotes.get(v.getQuadra()).get(v.getLote()) + ";");
				esc.write(v.getX() + ";");
				esc.write(v.getY() + ";");
				esc.write(v.getSexo() + ";");
				esc.write(v.getFaixaEtaria() + ";");
				esc.write(v.getDoses() + "\\n");
			}

			esc.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void salvarArquivoClimaticos() {
		try {
			BufferedWriter esc = new BufferedWriter(
					new FileWriter(nomeArquivoClimaticos));

			esc.write("cli\\n");
			int size = clim.get("txMinNaoAlados").size();
			esc.write(size + "\\n");

			for (int i = 0; i < size; ++i) {
				esc.write(Double.toString(clim.get("txMinNaoAlados").get(i)));
				esc.write(";");
				esc.write(Double.toString(clim.get("txMaxNaoAlados").get(i)));
				esc.write(";");
				esc.write(Double.toString(clim.get("txMinAlados").get(i)));
				esc.write(";");
				esc.write(Double.toString(clim.get("txMaxAlados").get(i)));
				esc.write("\\n");
			}

			esc.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void executar() {
		long tempoExecucao = System.currentTimeMillis();

		System.out.println("Processando vizinhancas...");
		gerarVizinhancas();
		gerarIndexVizinhancas();
		gerarVetorVizinhancas();

		System.out.println("Processando posicoes...");
		gerarVetorPosicoes();
		gerarIndexPosicoes();
		gerarIndexPosicoesRegioes();

		System.out.println("Processando fronteiras...");
		gerarFronteiras();
		gerarIndexFronteiras();
		gerarVetorFronteiras();

		System.out.println("Processando esquinas...");
		gerarIndexEsquinas();
		gerarVetorEsquinas();

		gerarIndexCentrosEsquinas();
		gerarVetorCentrosEsquinas();

		System.out.println("Processando trajetos...");
		distribuirLocais();
		gerarTrajetos();
		gerarCaminhos();
		gerarVetorIndexRotas();
		gerarIndexTrajetos();
		gerarVetorIndexPeriodos();
		gerarIndexTrajetosFaixaEtaria();

		System.out.println("Salvando arquivos...");
		salvarArquivoVetores();
		salvarArquivoDistribuicaoMosquitos();
		salvarArquivoDistribuicaoHumanos();
		gerarIndexControlesPontos();
		gerarVetorControlesPontos();
		gerarIndexRaios();
		gerarVetorRaios();
		salvarArquivoControles();
		salvarArquivoClimaticos();

		System.out.println("Tempo de execucao: "
				+ ((System.currentTimeMillis() - tempoExecucao) / 1000.0));
	}

}
