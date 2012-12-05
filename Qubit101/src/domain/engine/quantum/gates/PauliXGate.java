

package domain.engine.quantum.gates;

import java.util.Map.Entry;

import domain.engine.circuit.Stage;
import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;
import domain.engine.quantum.QuantumState;

public class PauliXGate extends UnitaryGateTemplate
{

    public int getGateId()
    {
        return Stage.GATE_PAULIX;
    }

    public void singleComponentOperation(Entry<ClassicState, Complex> e, int targetQubit, QuantumState res)
    {
        byte state[] = e.getKey().getState();
        byte stateneg[] = (byte[])state.clone();
        stateneg[targetQubit] = (byte)(stateneg[targetQubit] != 0 ? 0 : 1);
        res.add(e.getValue(), new ClassicState(stateneg));
    }

    public String getSymbol()
    {
        return "X";
    }
}
