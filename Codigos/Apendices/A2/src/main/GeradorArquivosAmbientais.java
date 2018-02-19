package main;

import java.util.Map;

import banco.ConsultasBanco;

public class GeradorArquivosAmbientais {

	public static final String RUA = "0000";

	public static final String REGIAO_RUA = "0";
	public static final String REGIAO_RURAL = "G";
	public static final String REGIAO_QUADRA = "Q";

	public static void main(String[] args) {
		Map<String, String> params = ConsultasBanco.getConfig();

		System.out.println("Ambiente: " + params.get("Ambiente"));
		System.out.println("Doenca: " + params.get("Doenca"));
		if (params.get("Doenca").equals("Influenza")) {
			Influenza ger = new Influenza(params);
			ger.executar();
		}
		if (params.get("Doenca").equals("Dengue")) {
			Dengue ger = new Dengue(params);
			ger.executar();
		}
	}

}
