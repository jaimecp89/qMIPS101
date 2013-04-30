package domain.engine.quantum;

public class ClassicState implements Comparable<ClassicState> {
	
	private byte state[];

	public ClassicState(byte state[]) {
		this.state = state;
	}

	public byte[] getState() {
		return state;
	}

	public int compareTo(ClassicState o) {
		int res = 0;
		for (int i = 0; res == 0 && i < state.length; i++)
			res = state[i] - o.state[i];

		return res;
	}

	public boolean equals(ClassicState o) {
		boolean res = true;
		for (int i = 0; i < state.length; i++) {
			if (o.state[i] == state[i])
				continue;
			res = false;
			break;
		}

		return res;
	}

	public String toString() {
		String res = "";
		for (int i = 0; i < state.length; i++)
			res = res + state[i];

		return res;
	}

	public Object clone() {
		ClassicState res = new ClassicState(new byte[state.length]);
		for (int i = 0; i < state.length; i++)
			res.state[i] = state[i];

		return res;
	}


}
