
package domain.engine.quantum.gates;

import java.util.Map.Entry;

import domain.engine.circuit.Stage;
import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;
import domain.engine.quantum.QuantumState;


public class PauliZGate extends UnitaryGateTemplate
{

    public int getGateId()
    {
        return Stage.GATE_PAULIZ;
    }

    public void singleComponentOperation(Entry<ClassicState, Complex> e, int targetQubit, QuantumState res)
    {
        byte state[] = e.getKey().getState();
        byte stateRes[] = (byte[])state.clone();
        if(stateRes[targetQubit] == 1)
            res.add(e.getValue().multiply(new Complex(-1.0, 0.0, false)), new ClassicState(stateRes));
        else
            res.add(e.getValue(), new ClassicState(stateRes));
    }

    public String getSymbol()
    {
        return "Z";
    }
}
