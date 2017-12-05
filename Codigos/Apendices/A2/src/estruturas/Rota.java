package estruturas;

import java.util.ArrayList;

public class Rota {

	private Local localInicial, localFinal;
	private ArrayList<Local> ruas;

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

	public ArrayList<Local> getRuas() {
		return ruas;
	}

	public void setRuas(ArrayList<Local> ruas) {
		this.ruas = ruas;
	}

}
