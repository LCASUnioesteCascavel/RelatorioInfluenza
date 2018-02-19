package estruturas;

public class ControlePonto {

	private int idControle;
	private String quadra, lote;
	private double x, y;

	public ControlePonto(int idControle, String quadra, String lote, double x,
			double y) {
		this.idControle = idControle;
		this.quadra = quadra;
		this.lote = lote;
		this.x = x;
		this.y = y;
	}

	public int getIdControle() {
		return idControle;
	}

	public void setIdControle(int idControle) {
		this.idControle = idControle;
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

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
