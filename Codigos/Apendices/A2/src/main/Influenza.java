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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import banco.ConsultasBanco;
import estruturas.CasoInfeccao;
import estruturas.Ponto;
import estruturas.Vizinhanca;

public class Influenza {

	public String pastaSaida;

	public String nomrArquivoAmbiental;
	public String nomeArquivoControles;
	public String nomeArquivoDistribuicaoHumanos;

	private HashMap<String, String> params;
	private ConsultasBanco con;
	private Map<String, HashMap<String, ArrayList<Ponto>>> pontos;
	private Map<String, Map<String, List<Vizinhanca>>> vizinhancas;
	private Map<String, Integer> indicesQuadras;
	private ArrayList<Integer> indexQuadras, indexVizinhancas, vetorVizinhancas,
			indexPosicoes, vetorPosicoes;
	private Map<String, SortedMap<String, Integer>> indicesLotes;
	private List<CasoInfeccao> distribuicaoHumanos;
	private List<String> fEVac, cicVac;

	public Influenza(HashMap<String, String> params) {
		this.params = params;

		this.pastaSaida = "Saidas" + File.separator + params.get("Ambiente") + "_"
				+ params.get("Doenca") + File.separator;
		if (!new File(pastaSaida).exists()) {
			new File(pastaSaida).mkdir();
		}

		nomrArquivoAmbiental = pastaSaida + "0-AMB.csv";
		nomeArquivoControles = pastaSaida + "1-CON.csv";
		nomeArquivoDistribuicaoHumanos = pastaSaida + "DistribuicaoHumanos.csv";

		con = new ConsultasBanco(params.get("Ambiente"), params.get("Doenca"));
		pontos = con.getPontos();
		criarIndexQuadraseLotes();
		distribuicaoHumanos = con.getDistribuicaoHumanos();
		fEVac = con.getFaixasEtariasVacinacao();
		cicVac = con.getCiclosVacinacao();
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

	private void salvarArquivoVetores() {
		try {
			BufferedWriter esc = new BufferedWriter(
					new FileWriter(nomrArquivoAmbiental));

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
    max = (max == 0 ? 1 : max);

		Map<Integer, Double> percentuais = new HashMap<>();
		for (Entry<Integer, Integer> i : casosAcumulados.entrySet()) {
			double v = (max - i.getValue()) / max;
			percentuais.put(i.getKey(), v);
		}

		try {
			BufferedWriter esc = new BufferedWriter(
					new FileWriter(nomeArquivoControles));

			esc.write("fEVac. Faixas etarias que receberao vacinacao\n");
			esc.write(fEVac.size() + "\n");
			esc.write(fEVac.stream().collect(Collectors.joining(";")) + "\n\n");

			esc.write("cicVac. Ciclos em que as vacinacoes serao executadas\n");
			esc.write(cicVac.size() + "\n");
			esc.write(cicVac.stream().collect(Collectors.joining(";")) + "\n\n");

			esc.write("Sazonalidade\n");
			esc.write(percentuais.size() + "\n");
			esc.write(percentuais.entrySet().stream()
					.map(i -> String.format("%.10f", i.getValue()).replace(",", "."))
					.collect(Collectors.joining(";")) + "\n\n");

			ArrayList<Double> percentuaisQuarentena = new ArrayList<>();
			for (int i = 0; i < Integer.parseInt(params.get("DuracaoAno")); ++i) {
				percentuaisQuarentena.add(Math.random());
			}

			esc.write("Percentuais de quarentena\n");
			esc.write(percentuaisQuarentena.size() + "\n");
			esc.write(percentuaisQuarentena.stream()
					.map(i -> String.format("%.10f", i).replace(",", "."))
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

		System.out.println("Salvando arquivos...");
		salvarArquivoVetores();
		salvarArquivoDistribuicaoHumanos();
		salvarArquivoControles();

		System.out.println("Tempo de execucao: "
				+ ((System.currentTimeMillis() - tempoExecucao) / 1000.0));
	}

}
