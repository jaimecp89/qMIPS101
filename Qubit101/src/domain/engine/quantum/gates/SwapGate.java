

package domain.engine.quantum.gates;

import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;
import domain.engine.quantum.QuantumState;
import java.util.Map.Entry;


public class SwapGate extends UnitaryGateTemplate
{

    public SwapGate(int targetQubit)
    {
        this.targetQubit = targetQubit;
    }

    public int getTargetQubit()
    {
        return targetQubit;
    }

    public int getGateId()
    {
        return 2;
    }

    public void singleComponentOperation(Entry<ClassicState, Complex> e, int targetQubit, QuantumState res)
    {
        byte state[] = e.getKey().getState();
        byte stateRes[] = (byte[])state.clone();
        stateRes[this.targetQubit] = state[targetQubit];
        stateRes[targetQubit] = state[this.targetQubit];
        res.add(e.getValue(), new ClassicState(stateRes));
    }

    public String getSymbol()
    {
        return "SW";
    }

    private int targetQubit;
}
