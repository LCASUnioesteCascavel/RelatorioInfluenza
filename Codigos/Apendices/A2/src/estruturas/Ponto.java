package estruturas;

import java.awt.Point;

import main.GeradorArquivosAmbientaisAcoplado;

public class Ponto implements Cloneable, Comparable<Ponto> {

	private double x, y;
	private int id, refinamento;

	public String quadra, lote;

	public Ponto(int id, String quadra, String lote, double x, double y,
			int refinamento) {
		this.setId(id);
		this.quadra = quadra;
		this.lote = lote;
		this.setX(x);
		this.setY(y);
		this.setRefinamento(refinamento);
	}

	public Ponto(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Ponto(String x, String y) {
		this.x = Double.parseDouble(x);
		this.y = Double.parseDouble(y);
	}

	public boolean equals(Object obj) {
		if (obj instanceof Ponto) {
			Ponto ponto = (Ponto) obj;
			if (getId() == ponto.getId() && quadra.equals(ponto.quadra)
					&& lote.equals(ponto.lote) && getX() == ponto.getX()
					&& getY() == ponto.getY()
					&& getRefinamento() == ponto.getRefinamento()) {
				return true;
			}
		}
		return false;
	}

	protected Object clone() throws CloneNotSupportedException {
		return new Ponto(getId(), quadra, lote, getX(), getY(), getRefinamento());
	}

	public int compareTo(Ponto o) {
		if (quadra.compareTo(o.quadra) < 0) {
			return -1;
		}
		if (quadra.compareTo(o.quadra) > 0) {
			return 1;
		}
		if (lote.compareTo(o.lote) < 0) {
			return -1;
		}
		if (lote.compareTo(o.lote) > 0) {
			return 1;
		}
		return 0;
	}

	public String toString() {
		return "Ponto [x=" + getX() + ", y=" + getY() + ", id=" + getId()
				+ ", refinamento=" + getRefinamento() + ", quadra=" + quadra + ", lote="
				+ lote + "]";
	}

	public boolean pertenceAoLote(String quadra, String lote) {
		if (this.quadra.equals(quadra) && this.lote.equals(lote)) {
			return true;
		}
		return false;
	}

	public boolean isRua() {
		return quadra.equals(GeradorArquivosAmbientaisAcoplado.RUA);
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRefinamento() {
		return refinamento;
	}

	public void setRefinamento(int refinamento) {
		this.refinamento = refinamento;
	}

	public double distancia(Ponto p) {
		return Point.distance(x, y, p.getX(), p.getY());
	}

}
