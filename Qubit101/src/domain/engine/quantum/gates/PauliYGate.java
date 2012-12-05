package domain.engine.quantum.gates;

import java.util.Map.Entry;

import domain.engine.circuit.Stage;
import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;
import domain.engine.quantum.QuantumState;

public class PauliYGate extends UnitaryGateTemplate {

	public int getGateId() {
		return Stage.GATE_PAULIY;
	}

	public void singleComponentOperation(Entry<ClassicState, Complex> e,
			int targetQubit, QuantumState res) {
		byte state[] = e.getKey().getState();
		byte stateRes[] = (byte[]) state.clone();
		Complex phase = null;
		if (stateRes[targetQubit] == 0) {
			stateRes[targetQubit] = 1;
			phase = e.getValue().multiply(new Complex(0.0, -1.0, false));
		} else {
			stateRes[targetQubit] = 0;
			phase = e.getValue().multiply(new Complex(0.0D, 1.0D, false));
		}
		res.add(phase, new ClassicState(stateRes));
	}

	public String getSymbol() {
		return "Y";
	}
}
