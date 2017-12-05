package estruturas;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Trajeto {

	public static final int CRIANCA = 0;
	public static final int JOVEM = 1;
	public static final int ADULTO = 2;
	public static final int IDOSO = 3;

	private int tipoTrajeto;
	private ArrayList<Rota> trajeto;
	private ArrayList<Integer> periodos;

	public Trajeto(int tipoTrajeto) {
		this.tipoTrajeto = tipoTrajeto;
		this.trajeto = new ArrayList<>();
		this.periodos = new ArrayList<>();
	}

	public int getTipoTrajeto() {
		return tipoTrajeto;
	}

	public void setTipoTrajeto(int tipoTrajeto) {
		this.tipoTrajeto = tipoTrajeto;
	}

	public ArrayList<Rota> getTrajeto() {
		return trajeto;
	}

	public void setTrajeto(ArrayList<Rota> trajeto) {
		this.trajeto = trajeto;
	}

	public ArrayList<Integer> getPeriodos() {
		return periodos;
	}

	public void setPeriodos(ArrayList<Integer> periodos) {
		this.periodos = periodos;
	}

	public void add(Rota rota) {
		trajeto.add(rota);
	}

	public void add(Integer periodo) {
		periodos.add(periodo);
	}

	public Stream<Rota> streamTrajeto() {
		return trajeto.stream();
	}

	public Stream<Integer> streamPeriodos() {
		return periodos.stream();
	}

	public boolean isTipoCrianca() {
		return tipoTrajeto == CRIANCA;
	}

	public boolean isTipoJovem() {
		return tipoTrajeto == JOVEM;
	}

	public boolean isTipoAdulto() {
		return tipoTrajeto == ADULTO;
	}

	public boolean isTipoIdoso() {
		return tipoTrajeto == IDOSO;
	}

}
