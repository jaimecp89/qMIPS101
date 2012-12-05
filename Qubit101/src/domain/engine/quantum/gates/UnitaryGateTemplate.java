
package domain.engine.quantum.gates;

import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;
import domain.engine.quantum.QuantumState;
import domain.engine.quantum.interfaces.UnitaryGate;
import java.util.Map.Entry;

public abstract class UnitaryGateTemplate implements UnitaryGate {

	public QuantumState operate(QuantumState input, int targetQubit) {
		QuantumState res = new QuantumState();
		for (Entry<ClassicState, Complex> e : input)
			singleComponentOperation(e, targetQubit, res);

		return res;
	}

	public abstract int getGateId();

	public QuantumState operate(QuantumState input, int targetQubit,
			int controlQubits[]) {
		QuantumState res = new QuantumState();
		for (Entry<ClassicState, Complex> e : input) {
			byte state0[] = e.getKey().getState();
			boolean act = true;
			for (int i = 0; i < controlQubits.length; i++) {
				if (state0[controlQubits[i]] != 1){
					act = false;
					break;
				}
			}
			if (act)
				singleComponentOperation(e, targetQubit, res);
			else
				res.add(e.getValue(), e.getKey());
		}

		return res;
	}

	public abstract void singleComponentOperation(
			Entry<ClassicState, Complex> entry, int i, QuantumState quantumstate);

	public abstract String getSymbol();
}
