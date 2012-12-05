package domain.engine.quantum.gates;

import java.util.Map.Entry;

import domain.engine.circuit.Stage;
import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;
import domain.engine.quantum.QuantumState;
import domain.engine.quantum.interfaces.Gate;

public class AddQubit implements Gate {

	public QuantumState operate(QuantumState input, int targetQubit) {
		QuantumState res = new QuantumState();
		byte stateRes[];
		for (Entry<ClassicState, Complex> e : input) {
			byte state[] = e.getKey().getState();
			stateRes = new byte[state.length + 1];
			int j = 0;
			for (int i = 0; i < stateRes.length; i++)
				if (i != targetQubit) {
					stateRes[i] = state[j];
					j++;
				} else {
					stateRes[i] = 0;
				}
			res.add(e.getValue(), new ClassicState(stateRes));
		}

		return res;
	}

	public int getGateId() {
		return Stage.GATE_ADDQUBIT;
	}

	public String getSymbol() {
		return "|0>";
	}
}
