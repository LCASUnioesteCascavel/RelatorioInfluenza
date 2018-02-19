package estruturas;

import java.util.List;

public class Rota {

	private Local localInicial, localFinal;
	private List<Local> ruas;

	public Rota(Local localInicio, Local localFinal) {
		this.setLocalInicial(localInicio);
		this.setLocalFinal(localFinal);
	}

	public Local getLocalInicial() {
		return localInicial;
	}

	public void setLocalInicial(Local localInicio) {
		this.localInicial = localInicio;
	}

	public Local getLocalFinal() {
		return localFinal;
	}

	public void setLocalFinal(Local localFinal) {
		this.localFinal = localFinal;
	}

	public List<Local> getRuas() {
		return ruas;
	}

	public void setRuas(List<Local> ruas) {
		this.ruas = ruas;
	}

}
