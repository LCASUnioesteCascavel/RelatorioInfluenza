package estruturas;

public class Vizinhanca implements Comparable<Vizinhanca> {

	private Ponto first, second;

	public Vizinhanca(Ponto first, Ponto second) {
		this.setFirst(first);
		this.setSecond(second);
	}

	Override
	public String toString() {
		return "Pair [first=" + getFirst() + ", second=" + getSecond() + "]";
	}

	Override
	public int compareTo(Vizinhanca o) {
		if (getFirst().compareTo(o.getFirst()) == -1) {
			return -1;
		}
		if (getFirst().compareTo(o.getFirst()) == 1) {
			return 1;
		}
		if (getSecond().compareTo(o.getSecond()) == -1) {
			return -1;
		}
		if (getSecond().compareTo(o.getSecond()) == 1) {
			return 1;
		}
		return 0;
	}

	public Ponto getFirst() {
		return first;
	}

	public void setFirst(Ponto first) {
		this.first = first;
	}

	public Ponto getSecond() {
		return second;
	}

	public void setSecond(Ponto second) {
		this.second = second;
	}

}
