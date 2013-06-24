package domain.engine.quantum.interfaces;

import domain.engine.quantum.QuantumState;

public interface Gate
{

    public abstract QuantumState operate(QuantumState quantumstate, int i);

    public abstract int getGateId();

    public abstract String getSymbol();
}
