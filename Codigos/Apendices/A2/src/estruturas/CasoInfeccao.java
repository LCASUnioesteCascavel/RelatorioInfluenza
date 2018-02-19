package estruturas;

public class CasoInfeccao {

	private int dia, x, y, quadra, lote;
	private String faixaEtaria, sexo;

	public CasoInfeccao(int dia, int x, int y, String faixaEtaria) {
		this.dia = dia;
		this.x = x;
		this.y = y;
		this.faixaEtaria = faixaEtaria;
	}

	public int getDia() {
		return dia;
	}

	public void setDia(int dia) {
		this.dia = dia;
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

	public int getQuadra() {
		return quadra;
	}

	public void setQuadra(int quadra) {
		this.quadra = quadra;
	}

	public int getLote() {
		return lote;
	}

	public void setLote(int lote) {
		this.lote = lote;
	}

	public Ponto getPonto() {
		return new Ponto(x, y);
	}

	Override
	public String toString() {
		return "CasoInfeccao [dia=" + dia + ", x=" + x + ", y=" + y
				+ ", faixaEtaria=" + faixaEtaria + "]";
	}

}
