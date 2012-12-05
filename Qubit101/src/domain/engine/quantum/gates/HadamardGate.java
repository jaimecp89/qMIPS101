package domain.engine.quantum.gates;

import domain.engine.circuit.Stage;
import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;
import domain.engine.quantum.QuantumState;
import java.util.Map.Entry;

public class HadamardGate extends UnitaryGateTemplate {

	private final double SQRT05 = 0.70710678118654757;
	private final Complex factor = new Complex(SQRT05, 0.0, false);

	public int getGateId() {
		return Stage.GATE_HADAMARD;
	}

	public void singleComponentOperation(Entry<ClassicState, Complex> e,
			int targetQubit, QuantumState res) {
		byte state0[] = (byte[]) ((ClassicState) e.getKey()).getState().clone();
		byte state1[] = (byte[]) state0.clone();
		Complex phase = state0[targetQubit] != 1 ? new Complex(1.0, 0.0,
				Boolean.valueOf(false)) : new Complex(-1.0, 0.0, false);
		state0[targetQubit] = 0;
		state1[targetQubit] = 1;
		res.add(e.getValue().multiply(factor), new ClassicState(state0));
		res.add(e.getValue().multiply(factor).multiply(phase),
				new ClassicState(state1));
	}

	public String getSymbol() {
		return "H";
	}

}
