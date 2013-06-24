package domain.engine.quantum.interfaces;

import domain.engine.quantum.QuantumState;

public interface UnitaryGate extends Gate {

	public abstract QuantumState operate(QuantumState quantumstate, int i,
			int ai[]);
}
