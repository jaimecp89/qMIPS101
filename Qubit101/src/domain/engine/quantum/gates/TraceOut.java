

package domain.engine.quantum.gates;

import java.util.Map.Entry;

import domain.engine.circuit.Stage;
import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;
import domain.engine.quantum.QuantumState;
import domain.engine.quantum.interfaces.Gate;


public class TraceOut implements Gate {

	public QuantumState operate(QuantumState input, int targetQubit) {
		QuantumState res = new QuantumState();
		QuantumState input2 = new Measure().operate(input, targetQubit);
		byte stateRes[];
		for (Entry<ClassicState, Complex> e : input2) {
			byte state[] = e.getKey().getState();
			stateRes = new byte[state.length - 1];
			int j = 0;
			for (int i = 0; i < state.length; i++)
				if (i != targetQubit) {
					stateRes[j] = state[i];
					j++;
				}
			res.add(e.getValue(), new ClassicState(stateRes));
		}
		return res;
	}

	public int getGateId() {
		return Stage.GATE_TRACE;
	}

	public String getSymbol() {
		return "Tr";
	}
}
