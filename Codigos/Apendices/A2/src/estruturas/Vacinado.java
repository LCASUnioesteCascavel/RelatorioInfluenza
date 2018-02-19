package estruturas;

public class Vacinado {

	private int ciclo, x, y, doses;
	private String quadra, lote, sexo, faixaEtaria;

	public Vacinado(int ciclo, String quadra, String lote, int x, int y,
			String sexo, String faixaEtaria, int doses) {
		this.ciclo = ciclo;
		this.quadra = quadra;
		this.lote = lote;
		this.x = x;
		this.y = y;
		this.sexo = sexo;
		this.faixaEtaria = faixaEtaria;
		this.doses = doses;
	}

	public int getCiclo() {
		return ciclo;
	}

	public void setCiclo(int ciclo) {
		this.ciclo = ciclo;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getQuadra() {
		return quadra;
	}

	public void setQuadra(String quadra) {
		this.quadra = quadra;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getFaixaEtaria() {
		return faixaEtaria;
	}

	public void setFaixaEtaria(String faixaEtaria) {
		this.faixaEtaria = faixaEtaria;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public int getDoses() {
		return doses;
	}

	public void setDoses(int doses) {
		this.doses = doses;
	}

}
