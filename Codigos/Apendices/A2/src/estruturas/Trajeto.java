package estruturas;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Trajeto {

	public static final int BEBE = 0;
	public static final int CRIANCA = 1;
	public static final int ADOLESCENTE = 2;
	public static final int JOVEM = 3;
	public static final int ADULTO = 4;
	public static final int IDOSO = 5;

	private int tipoTrajeto;
	private List<Rota> trajeto;
	private List<Integer> periodos;

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

	public List<Rota> getTrajeto() {
		return trajeto;
	}

	public void setTrajeto(List<Rota> trajeto) {
		this.trajeto = trajeto;
	}

	public List<Integer> getPeriodos() {
		return periodos;
	}

	public void setPeriodos(List<Integer> periodos) {
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

	public boolean isTipoBebe() {
		return tipoTrajeto == BEBE;
	}

	public boolean isTipoCrianca() {
		return tipoTrajeto == CRIANCA;
	}

	public boolean isTipoAdolescente() {
		return tipoTrajeto == ADOLESCENTE;
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
