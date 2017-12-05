package estruturas;

public class Local {

	private String quadra, lote;
	private int periodo;

	public Local(String quadra, String lote, int periodo) {
		super();
		this.setQuadra(quadra);
		this.setLote(lote);
		this.setPeriodo(periodo);
	}

	public Local(Local local, int periodo) {
		super();
		this.setQuadra(local.getQuadra());
		this.setLote(local.getLote());
		this.setPeriodo(periodo);
	}

	public Local(String quadra, String lote) {
		super();
		this.setQuadra(quadra);
		this.setLote(lote);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getLote() == null) ? 0 : getLote().hashCode());
		result = prime * result
				+ ((getQuadra() == null) ? 0 : getQuadra().hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		Local l2 = (Local) obj;
		return l2.getQuadra().equals(getQuadra()) && l2.getLote().equals(getLote());
	}

	public String toString() {
		return "\nLocal [quadra=" + getQuadra() + ", lote=" + getLote()
				+ ", periodo=" + getPeriodo() + "]\n";
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

	public int getPeriodo() {
		return periodo;
	}

	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}

}
