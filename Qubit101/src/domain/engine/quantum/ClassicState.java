// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 05/10/2011 0:24:17
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ClassicState.java

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
