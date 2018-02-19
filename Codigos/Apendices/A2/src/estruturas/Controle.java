package estruturas;

public class Controle {

	private String quadra, tipoControle;
	private int ciclo;
	private double taxaMinMecanico, taxaMaxMecanico, taxaMinQuimico,
			taxaMaxQuimico;

	public Controle(String quadra, int ciclo, String tipoControle,
			double taxaMinMecanico, double taxaMaxMecanico, double taxaMinQuimico,
			double taxaMaxQuimico) {
		this.quadra = quadra;
		this.tipoControle = tipoControle;
		this.ciclo = ciclo;
		this.taxaMinMecanico = taxaMinMecanico;
		this.taxaMaxMecanico = taxaMaxMecanico;
		this.taxaMinQuimico = taxaMinQuimico;
		this.taxaMaxQuimico = taxaMaxQuimico;
	}

	public String getQuadra() {
		return quadra;
	}

	public void setQuadra(String quadra) {
		this.quadra = quadra;
	}

	public String getTipoControle() {
		return tipoControle;
	}

	public void setTipoControle(String tipoControle) {
		this.tipoControle = tipoControle;
	}

	public int getCiclo() {
		return ciclo;
	}

	public void setCiclo(int ciclo) {
		this.ciclo = ciclo;
	}

	public double getTaxaMinMecanico() {
		return taxaMinMecanico;
	}

	public void setTaxaMinMecanico(double taxaMinMecanico) {
		this.taxaMinMecanico = taxaMinMecanico;
	}

	public double getTaxaMaxMecanico() {
		return taxaMaxMecanico;
	}

	public void setTaxaMaxMecanico(double taxaMaxMecanico) {
		this.taxaMaxMecanico = taxaMaxMecanico;
	}

	public double getTaxaMinQuimico() {
		return taxaMinQuimico;
	}

	public void setTaxaMinQuimico(double taxaMinQuimico) {
		this.taxaMinQuimico = taxaMinQuimico;
	}

	public double getTaxaMaxQuimico() {
		return taxaMaxQuimico;
	}

	public void setTaxaMaxQuimico(double taxaMaxQuimico) {
		this.taxaMaxQuimico = taxaMaxQuimico;
	}

}
