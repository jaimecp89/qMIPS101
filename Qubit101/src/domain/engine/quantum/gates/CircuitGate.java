// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 05/10/2011 0:28:24
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CircuitGate.java

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
