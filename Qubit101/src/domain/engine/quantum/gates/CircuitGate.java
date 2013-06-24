package domain.engine.quantum.gates;

import domain.engine.circuit.Circuit;
import domain.engine.quantum.QuantumState;
import domain.engine.quantum.interfaces.UnitaryGate;

public class CircuitGate
    implements UnitaryGate
{

    public CircuitGate(Circuit circuit)
    {
        setCircuit(circuit);
    }

    public QuantumState operate(QuantumState input, int targetQubit)
    {
        return circuit.simulateCircuit(input, targetQubit);
    }

    public int getGateId()
    {
        return 7;
    }

    public String getSymbol()
    {
        return circuit.getName();
    }

    public QuantumState operate(QuantumState input, int targetQubit, int controlQubits[])
    {
        return circuit.simulateCircuit(input, targetQubit, controlQubits);
    }

    public Circuit getCircuit()
    {
        return circuit;
    }

    public void setCircuit(Circuit circuit)
    {
        this.circuit = circuit;
    }

    private Circuit circuit;
}
