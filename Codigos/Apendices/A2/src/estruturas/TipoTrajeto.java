package estruturas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TipoTrajeto {

	public class LocalPeriodo {

		private String local;
		private Double periodo;

		public LocalPeriodo(String local, String periodo) {
			super();
			this.local = local;
			this.periodo = Double.parseDouble(periodo);
		}

		public String getLocal() {
			return local;
		}

		public void setLocal(String local) {
			this.local = local;
		}

		public Double getPeriodo() {
			return periodo;
		}

		public void setPeriodo(Double periodo) {
			this.periodo = periodo;
		}

		public String toString() {
			return "LocalPeriodo [local=" + local + ", periodo=" + periodo + "]";
		}

	}

	private String faixaEtaria;
	private List<LocalPeriodo> locaisPeriodos;

	public TipoTrajeto(String faixaEtaria, List<String> locais,
			List<String> periodos) {
		super();
		this.faixaEtaria = faixaEtaria;
		this.locaisPeriodos = new ArrayList<>();

		Iterator<String> itLocais = locais.iterator();
		Iterator<String> itPeriodos = periodos.iterator();
		while (itLocais.hasNext() && itPeriodos.hasNext()) {
			locaisPeriodos.add(new LocalPeriodo(itLocais.next(), itPeriodos.next()));
		}
	}

	public String getFaixaEtaria() {
		return faixaEtaria;
	}

	public void setFaixaEtaria(String faixaEtaria) {
		this.faixaEtaria = faixaEtaria;
	}

	public List<LocalPeriodo> getLocaisPeriodos() {
		return locaisPeriodos;
	}

	public String toString() {
		return "TipoTrajeto [faixaEtaria=" + faixaEtaria + ", locaisPeriodos="
				+ locaisPeriodos + "]";
	}

}
