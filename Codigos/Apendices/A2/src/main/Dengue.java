package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import banco.ConsultasBanco;
import estruturas.CasoInfeccao;
import estruturas.Local;
import estruturas.Ponto;
import estruturas.Rota;
import estruturas.TipoTrajeto;
import estruturas.TipoTrajeto.LocalPeriodo;
import estruturas.Trajeto;
import estruturas.Vizinhanca;

public class Dengue {

	public String pastaSaida;

	public String nomeArquivoAmbiental;
	public String nomeArquivoMovimentacao;
	public String nomeArquivoControles;
	public String nomeArquivoDistribuicaoMosquitos;
	public String nomeArquivoDistribuicaoHumanos;
	public String nomeArquivoSazonalidade;

	private HashMap<String, String> params;
	private ConsultasBanco con;
	private Map<String, HashMap<String, ArrayList<Ponto>>> pontos;
	private Map<String, Map<String, List<Vizinhanca>>> vizinhancas;
	private Map<String, ArrayList<Ponto>> pontosEsquinas;
	private Map<String, Integer> indicesQuadras;
	private Map<String, SortedMap<String, Integer>> indicesLotes;
	private Map<String, Map<String, List<Vizinhanca>>> fronteiras;
	private ArrayList<Integer> indexQuadras, indexVizinhancas, vetorVizinhancas,
			indexPosicoes, vetorPosicoes, indexFronteiras, vetorFronteiras,
			indexEsquinas, vetorEsquinas, indexCentrosEsquinas, vetorCentrosEsquinas;
	private Map<String, List<Local>> locais;
	private List<Trajeto> trajetos;
	private Map<String, HashMap<String, ArrayList<ArrayList<String>>>> arestas;
	private Map<String, ArrayList<Ponto>> centroidesEsquinas;
	private Map<String, HashMap<String, Ponto>> centroidesLotes;
	private List<Integer> indexRotas, vetorRotas;
	private List<Integer> indexTrajetos;
	private List<Integer> indexPeriodos, vetorPeriodos;
	int nRotas, nTrajetos, trajetosCriancas, trajetosJovens, trajetosAdultos,
			trajetosIdosos;;
	private List<Integer> indexTrajetosFaixaEtaria;
	private List<Integer> indexPosicoesRegioes;
	private List<Local> poligonos;
	private List<Local> vertices;
	private List<List<String>> distribuicaoMosquitos;
	private List<CasoInfeccao> distribuicaoHumanos;
	private List<String> vac, fEVac, cicVac, raio, bloq, conBio, conAmb;
	private List<List<String>> ponEst;
	private Map<String, List<TipoTrajeto>> tiposTrajetos;

	public Dengue(HashMap<String, String> params) {
		this.params = params;

		this.pastaSaida = "Saidas" + File.separator + params.get("Ambiente") + "_"
				+ params.get("Doenca") + File.separator;
		if (!new File(pastaSaida).exists()) {
			new File(pastaSaida).mkdir();
		}

		nomeArquivoAmbiental = pastaSaida + "0-AMB.csv";
		nomeArquivoMovimentacao = pastaSaida + "1-MOV.csv";
		nomeArquivoControles = pastaSaida + "2-CON.csv";
		nomeArquivoDistribuicaoMosquitos = pastaSaida + "DistribuicaoMosquitos.csv";
		nomeArquivoDistribuicaoHumanos = pastaSaida + "DistribuicaoHumanos.csv";
		nomeArquivoSazonalidade = pastaSaida + "Sazonalidade.csv";

		con = new ConsultasBanco(params.get("Ambiente"), params.get("Doenca"));
		pontos = con.getPontos();
		criarIndexQuadraseLotes();
		poligonos = con.getPoligonos();
		vertices = poligonos.stream()
				.filter(
						i -> i.getQuadra().equals(GeradorArquivosAmbientaisAcoplado.RUA))
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
		raio = con.getQuadrasRaio();
		bloq = con.getQuadrasBloqueio();
		conBio = con.getQuadrasControleBiologico();
		conAmb = con.getQuadrasControleAmbiental();
		ponEst = con.getLotesPontosEstrategicos();
		tiposTrajetos = con.getTiposTrajetos();
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
		ArrayList<Vizinhanca> ret = con.getVizinhancasMoorePontos();
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
		if (c1.equals(GeradorArquivosAmbientaisAcoplado.REGIAO_RUA)) {
			regiao = 0;
		}
		if (c1.equals(GeradorArquivosAmbientaisAcoplado.REGIAO_RURAL)) {
			regiao = 1;
		}
		if (c1.equals(GeradorArquivosAmbientaisAcoplado.REGIAO_QUADRA)) {
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
		ArrayList<Vizinhanca> ret = (ArrayList<Vizinhanca>) vizinhancas.entrySet()
				.stream()
				.filter(i -> !i.getKey().equals(GeradorArquivosAmbientaisAcoplado.RUA))
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
				if (!quadra.equals(GeradorArquivosAmbientaisAcoplado.RUA) && !quadra
						.startsWith(GeradorArquivosAmbientaisAcoplado.REGIAO_RURAL)) {
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
				if (!quadra.equals(GeradorArquivosAmbientaisAcoplado.RUA) && !quadra
						.startsWith(GeradorArquivosAmbientaisAcoplado.REGIAO_RURAL)) {
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
		vetorEsquinas = (ArrayList<Integer>) pontosEsquinas.entrySet().stream()
				.sorted(Entry.comparingByKey()).flatMap(i -> i.getValue().stream())
				.flatMap(i -> Stream.of((int) i.getX(), (int) i.getY(),
						(int) indicesLotes.get(i.quadra).get(i.lote)))
				.collect(Collectors.toList());
	}

	private void gerarIndexCentrosEsquinas() {
		indexCentrosEsquinas = new ArrayList<>();
		int desl = 0;

		indexCentrosEsquinas.add(desl);
		for (String i : indicesLotes.get(GeradorArquivosAmbientaisAcoplado.RUA)
				.keySet()) {
			if (centroidesEsquinas.containsKey(i)) {
				desl += centroidesEsquinas.get(i).size();
			}
			indexCentrosEsquinas.add(desl);
		}
	}

	private void gerarVetorCentrosEsquinas() {
		vetorCentrosEsquinas = (ArrayList<Integer>) centroidesEsquinas.entrySet()
				.stream().sorted(Entry.comparingByKey())
				.flatMap(i -> i.getValue().stream())
				.flatMap(i -> Stream.of((int) i.getX(), (int) i.getY(),
						(int) indicesLotes.get(i.quadra).get(i.lote)))
				.collect(Collectors.toList());
	}

	private void distribuirLocais() {
		List<Local> lotes = poligonos.stream()
				.filter(i -> i.getQuadra()
						.startsWith(GeradorArquivosAmbientaisAcoplado.REGIAO_QUADRA))
				.collect(Collectors.toList());

		Collections.shuffle(lotes);
		ArrayList<Local> locaisCasa = new ArrayList<>(
				lotes.subList(0, Integer.parseInt(params.get("LotesCasa"))));
		lotes.removeAll(locaisCasa);

		Collections.shuffle(lotes);
		ArrayList<Local> locaisTrabalho = new ArrayList<>(
				lotes.subList(0, Integer.parseInt(params.get("LotesTrabalho"))));
		lotes.removeAll(locaisTrabalho);

		Collections.shuffle(lotes);
		ArrayList<Local> locaisLazer = new ArrayList<>(
				lotes.subList(0, Integer.parseInt(params.get("LotesLazer"))));
		lotes.removeAll(locaisLazer);

		Collections.shuffle(lotes);
		ArrayList<Local> locaisEstudo = new ArrayList<>(
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
			// TRAJETO PARA CRIANCA
			index = (int) (Math.random() * tiposTrajetos.get("Crianca").size());
			tipoTrajeto = tiposTrajetos.get("Crianca").get(index);
			trajetos.add(gerarTrajeto(Trajeto.CRIANCA, tipoTrajeto, casa));

			// TRAJETO PARA JOVEM
			index = (int) (Math.random() * tiposTrajetos.get("Jovem").size());
			tipoTrajeto = tiposTrajetos.get("Jovem").get(index);
			trajetos.add(gerarTrajeto(Trajeto.JOVEM, tipoTrajeto, casa));

			// TRAJETO PARA ADULTO
			index = (int) (Math.random() * tiposTrajetos.get("Adulto").size());
			tipoTrajeto = tiposTrajetos.get("Adulto").get(index);
			trajetos.add(gerarTrajeto(Trajeto.ADULTO, tipoTrajeto, casa));

			// TRAJETO PARA IDOSO
			index = (int) (Math.random() * tiposTrajetos.get("Idoso").size());
			tipoTrajeto = tiposTrajetos.get("Idoso").get(index);
			trajetos.add(gerarTrajeto(Trajeto.IDOSO, tipoTrajeto, casa));
		}
	}

	private ArrayList<Local> aEstrelaConstruirCaminho(
			HashMap<Local, Local> verticesAnteriores, Local verticeAtual) {
		ArrayList<Local> caminho = new ArrayList<>();
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

		HashSet<Local> verticesJaVisitados = new HashSet<>();

		HashSet<Local> verticesNaoVisitados = new HashSet<>();

		ArrayList<String> primeiraRuaAux = arestas.get(verticeOrigem.getQuadra())
				.get(verticeOrigem.getLote()).stream()
				.filter(i -> i.get(3).equals(GeradorArquivosAmbientaisAcoplado.RUA))
				.findFirst().get();
		Local primeiraRua = new Local(primeiraRuaAux.get(3), primeiraRuaAux.get(2));
		verticesNaoVisitados.add(primeiraRua);

		HashMap<Local, Local> verticesAnteriores = new HashMap<>();
		for (Local vertice : vertices) {
			verticesAnteriores.put(vertice, null);
		}

		HashMap<Local, Double> distanciaOrigemAoAtual = new HashMap<>();
		for (Local vertice : vertices) {
			distanciaOrigemAoAtual.put(vertice, Double.MAX_VALUE);
		}
		distanciaOrigemAoAtual.put(primeiraRua, 0.0);

		HashMap<Local, Double> distanciaAtualAoDestino = new HashMap<>();
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
							&& i.get(3).equals(verticeDestino.getQuadra()))
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
		HashMap<Integer, ArrayList<ArrayList<Integer>>> rotas = new HashMap<>();
		rotas.put(Trajeto.CRIANCA, new ArrayList<>());
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

				ArrayList<Integer> rota2 = new ArrayList<>();

				rota2.add(loteOrigem);
				rota2.add(quadraOrigem);
				rota2.add(loteDestino);
				rota2.add(quadraDestino);
				for (Local rua : rota.getRuas()) {
					rota2.add(indicesLotes.get(GeradorArquivosAmbientaisAcoplado.RUA)
							.get(rua.getLote()));
				}

				rotas.get(trajeto.getTipoTrajeto()).add(rota2);
			}
		}

		indexRotas = new ArrayList<>();
		int desl = 0;
		indexRotas.add(desl);
		for (ArrayList<Integer> i : rotas.get(Trajeto.CRIANCA)) {
			desl += i.size();
			indexRotas.add(desl);
		}
		for (ArrayList<Integer> i : rotas.get(Trajeto.JOVEM)) {
			desl += i.size();
			indexRotas.add(desl);
		}
		for (ArrayList<Integer> i : rotas.get(Trajeto.ADULTO)) {
			desl += i.size();
			indexRotas.add(desl);
		}
		for (ArrayList<Integer> i : rotas.get(Trajeto.IDOSO)) {
			desl += i.size();
			indexRotas.add(desl);
		}

		nRotas = rotas.get(Trajeto.CRIANCA).size() + rotas.get(Trajeto.JOVEM).size()
				+ rotas.get(Trajeto.ADULTO).size() + rotas.get(Trajeto.IDOSO).size();

		vetorRotas = new ArrayList<>();
		vetorRotas.addAll(rotas.get(Trajeto.CRIANCA).stream()
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

		trajetosCriancas = (int) trajetos.stream().filter(i -> i.isTipoCrianca())
				.count();
		trajetosJovens = (int) trajetos.stream().filter(i -> i.isTipoJovem())
				.count();
		trajetosAdultos = (int) trajetos.stream().filter(i -> i.isTipoAdulto())
				.count();
		trajetosIdosos = (int) trajetos.stream().filter(i -> i.isTipoIdoso())
				.count();

		nTrajetos = trajetosCriancas + trajetosJovens + trajetosAdultos
				+ trajetosIdosos;
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
		desl += trajetosCriancas;
		indexTrajetosFaixaEtaria.add(desl);
		desl += trajetosJovens;
		indexTrajetosFaixaEtaria.add(desl);
		desl += trajetosAdultos;
		indexTrajetosFaixaEtaria.add(desl);
		desl += trajetosIdosos;
		indexTrajetosFaixaEtaria.add(desl);
	}

	private void salvarArquivoVetores() {
		try {
			BufferedWriter esc = new BufferedWriter(
					new FileWriter(nomeArquivoAmbiental));

			esc.write("quantQuadras, quantLotes e indexQuadras\n");
			esc.write(pontos.keySet().size() + "\n");
			esc.write(pontos.entrySet().stream().sorted(Entry.comparingByKey())
					.map(i -> Integer.toString(i.getValue().size()))
					.collect(Collectors.joining(";")) + "\n");
			esc.write(indexQuadras.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\n\n");

			esc.write("indexVizinhancas e vetorVizinhancas\n");

			for (int i = 0; i < indexVizinhancas.size(); ++i) {
				esc.write(Integer.toString(indexVizinhancas.get(i)));
				if (i < indexVizinhancas.size() - 1) {
					esc.write(";");
				}
			}
			esc.write("\n");

			for (int i = 0; i < vetorVizinhancas.size(); ++i) {
				esc.write(Integer.toString(vetorVizinhancas.get(i)));
				if (i < vetorVizinhancas.size() - 1) {
					esc.write(";");
				}
			}
			esc.write("\n\n");

			esc.write("indexPosicoes, vetorPosicoes, indexPosicoesRegioes\n");
			esc.write(indexPosicoes.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\n");

			for (int i = 0; i < vetorPosicoes.size(); ++i) {
				esc.write(Integer.toString(vetorPosicoes.get(i)));
				if (i < vetorPosicoes.size() - 1) {
					esc.write(";");
				}
			}
			esc.write("\n");

			esc.write(indexPosicoesRegioes.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\n\n");

			esc.write("indexFronteiras e vetorFronteiras\n");
			esc.write(indexFronteiras.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\n");
			esc.write(vetorFronteiras.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\n\n");

			esc.write("indexEsquinas e vetorEsquinas\n");
			esc.write(indexEsquinas.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\n");
			esc.write(vetorEsquinas.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\n\n");

			esc.write("indexCentrosEsquinas e vetorCentrosEsquinas\n");
			esc.write(indexCentrosEsquinas.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\n");
			esc.write(vetorCentrosEsquinas.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\n");

			esc.close();

			esc = new BufferedWriter(new FileWriter(nomeArquivoMovimentacao));

			esc.write("quantRotas, indexRotas e vetorRotas\n");
			esc.write(nRotas + "\n");

			for (int i = 0; i < indexRotas.size(); ++i) {
				esc.write(Integer.toString(indexRotas.get(i)));
				if (i < indexRotas.size() - 1) {
					esc.write(";");
				}
			}
			esc.write("\n");

			for (int i = 0; i < vetorRotas.size(); ++i) {
				esc.write(Integer.toString(vetorRotas.get(i)));
				if (i < vetorRotas.size() - 1) {
					esc.write(";");
				}
			}
			esc.write("\n\n");

			esc.write("quantTrajetos e indexTrajetos\n");
			esc.write(nTrajetos + "\n");
			esc.write(indexTrajetos.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\n\n");

			esc.write("indexPeriodos e vetorPeriodos\n");
      
			for (int i = 0; i < indexPeriodos.size(); ++i) {
				esc.write(Integer.toString(indexPeriodos.get(i)));
				if (i < indexPeriodos.size() - 1) {
					esc.write(";");
				}
			}
			esc.write("\n");

			for (int i = 0; i < vetorPeriodos.size(); ++i) {
				esc.write(Integer.toString(vetorPeriodos.get(i)));
				if (i < vetorPeriodos.size() - 1) {
					esc.write(";");
				}
			}
			esc.write("\n\n");

			esc.write("indexTrajetosFaixaEtaria\n");
			esc.write(indexTrajetosFaixaEtaria.stream().map(i -> i.toString())
					.collect(Collectors.joining(";")) + "\n");

			esc.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void salvarArquivoDistribuicaoMosquitos() {
		try {
			BufferedWriter esc = new BufferedWriter(
					new FileWriter(nomeArquivoDistribuicaoMosquitos));

			esc.write(distribuicaoMosquitos.size() + "\n");
			esc.write("Quadra;QuantidadeTotal;Sexo;Fase;PercentualMinimoInfectados;");
			esc.write("PercentualMaximoInfectados;Sorotipo;Ciclo\n");
			for (List<String> i : distribuicaoMosquitos) {
				esc.write(indicesQuadras.get(i.get(0)) + ";");
				esc.write(i.stream().skip(1).map(j -> j.toString())
						.collect(Collectors.joining(";")) + "\n");
			}

			esc.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void salvarArquivoDistribuicaoHumanos() {
		double percInsInfec = Double.parseDouble(params.get("PorcentagemCasos"));

		Map<String, Double> percPop = new HashMap<>();
		percPop.put("C",
				Double.parseDouble(params.get("FracaoCriancasMasculinas")));
		percPop.put("J", Double.parseDouble(params.get("FracaoJovensMasculinos")));
		percPop.put("A", Double.parseDouble(params.get("FracaoAdultosMasculinos")));
		percPop.put("I", Double.parseDouble(params.get("FracaoIdososMasculinos")));

		Map<Integer, List<CasoInfeccao>> dados = distribuicaoHumanos.stream()
				.collect(Collectors.groupingBy(CasoInfeccao::getDia));

		for (Entry<Integer, List<CasoInfeccao>> entry : dados.entrySet()) {
			List<CasoInfeccao> value = entry.getValue();

			Collections.shuffle(value);
			int ind = (int) Math.floor(value.size() * (1.0 - percInsInfec));
			value.subList(0, ind).clear();
		}

		Map<String, List<CasoInfeccao>> dados2 = dados.entrySet().stream()
				.flatMap(i -> i.getValue().stream())
				.collect(Collectors.groupingBy(CasoInfeccao::getFaixaEtaria));

		for (Entry<String, List<CasoInfeccao>> entry : dados2.entrySet()) {
			String key = entry.getKey();
			List<CasoInfeccao> value = entry.getValue();

			int nM = (int) (value.size() * percPop.get(key));
			Collections.shuffle(value);
			value.stream().forEach(i -> i.setSexo("F"));
			value.subList(0, nM).stream().forEach(i -> i.setSexo("M"));
		}

		List<CasoInfeccao> dados3 = dados2.entrySet().stream()
				.flatMap(i -> i.getValue().stream()).collect(Collectors.toList());
		dados3.sort(Comparator.comparing(CasoInfeccao::getDia));

		List<Ponto> pontos2 = pontos.entrySet().stream().map(i -> i.getValue())
				.flatMap(i -> i.entrySet().stream()).flatMap(i -> i.getValue().stream())
				.collect(Collectors.toList());
		for (CasoInfeccao caso : dados3) {
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
			esc.write(dados3.size() + "\n");
			esc.write("Q;L;X;Y;Sexo;FaixaEtaria;SaudeDengue;SorotipoAtual;Ciclo\n");
			for (CasoInfeccao caso : dados3) {
				esc.write(caso.getQuadra() + ";");
				esc.write(caso.getLote() + ";");
				esc.write(caso.getX() + ";");
				esc.write(caso.getY() + ";");
				esc.write(caso.getSexo() + ";");
				esc.write(caso.getFaixaEtaria() + ";");
				esc.write("I;");
				esc.write("2;");
				esc.write(caso.getDia() + "\n");
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

		Map<Integer, Double> percentuais = new HashMap<>();
		for (Entry<Integer, Integer> i : casosAcumulados.entrySet()) {
			double v = (max - i.getValue()) / max;
			percentuais.put(i.getKey(), v);
		}

		try {
			BufferedWriter esc = new BufferedWriter(
					new FileWriter(nomeArquivoControles));

			esc.write("quadVac. Quadras em que serao aplicadas a vacinacao\n");
			esc.write(vac.size() + "\n");
			esc.write(vac.stream().map(i -> Integer.toString(indicesQuadras.get(i)))
					.collect(Collectors.joining(";")) + "\n\n");

			esc.write("fEVac. Faixas etarias que receberao vacinacao\n");
			esc.write(fEVac.size() + "\n");
			esc.write(fEVac.stream().collect(Collectors.joining(";")) + "\n\n");

			esc.write("cicVac. Ciclos em que as vacinacoes serao executadas\n");
			esc.write(cicVac.size() + "\n");
			esc.write(cicVac.stream().collect(Collectors.joining(";")) + "\n\n");

			esc.write("raio. Quadras com raio\n");
			esc.write(raio.size() + "\n");
			esc.write(raio.stream().map(i -> Integer.toString(indicesQuadras.get(i)))
					.collect(Collectors.joining(";")) + "\n\n");

			esc.write("bloq. Quadras com bloqueio\n");
			esc.write(bloq.size() + "\n");
			esc.write(bloq.stream().map(i -> Integer.toString(indicesQuadras.get(i)))
					.collect(Collectors.joining(";")) + "\n\n");

			esc.write("conBio. Quadras com controle biologico\n");
			esc.write(conBio.size() + "\n");
			esc.write(
					conBio.stream().map(i -> Integer.toString(indicesQuadras.get(i)))
							.collect(Collectors.joining(";")) + "\n\n");

			esc.write("conAmb. Quadras com controle ambiental\n");
			esc.write(conAmb.size() + "\n");
			esc.write(
					conAmb.stream().map(i -> Integer.toString(indicesQuadras.get(i)))
							.collect(Collectors.joining(";")) + "\n\n");

			esc.write("pontEst. Lotes que sao pontos estrategicos\n");
			esc.write((ponEst.size() * 2) + "\n");
			esc.write(ponEst.stream()
					.flatMap(
							i -> Stream.of(Integer.toString(indicesQuadras.get(i.get(0))),
									Integer.toString(indicesLotes.get(i.get(0)).get(i.get(1)))))
					.collect(Collectors.joining(";")) + "\n\n");

			esc.write("Sazonalidade\n");
			esc.write(percentuais.size() + "\n");
			esc.write(percentuais.entrySet().stream()
					.map(i -> String.format("%.10f", i.getValue()).replace(",", "."))
					.collect(Collectors.joining(";")) + "\n");

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
		salvarArquivoControles();

		System.out.println("Tempo de execucao: "
				+ ((System.currentTimeMillis() - tempoExecucao) / 1000.0));
	}

}
