

package domain.engine.quantum.gates;

import java.util.Map.Entry;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;

import domain.engine.circuit.Stage;
import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;
import domain.engine.quantum.QuantumState;


public class PhaseShiftGate extends UnitaryGateTemplate
{

	private String alfa;
	
    public PhaseShiftGate(String alfa)
    {
        setAlfa(alfa);
    }

    public int getGateId()
    {
        return Stage.GATE_PHASE;
    }

    public void singleComponentOperation(Entry<ClassicState, Complex> e, int targetQubit, QuantumState res)
    {
        byte state[] = e.getKey().getState();
        byte stateRes[] = (byte[])state.clone();
        JEP j = new JEP();
		j.addStandardConstants();
		j.addStandardFunctions();
		j.setImplicitMul(true);
		double value = 0.0;
		try {
			value = (Double) j.evaluate(j.parse(alfa));
		} catch (ParseException e1) {
			System.out.println("Error parsing phase gate equation.");
		}
        if(stateRes[targetQubit] == 1)
            res.add(e.getValue().multiply(new Complex(1.0, value, true)), new ClassicState(stateRes));
        else
            res.add(e.getValue(), new ClassicState(stateRes));
    }

    public void setAlfa(String alfa)
    {
        this.alfa = alfa;
    }

    public String getAlfa()
    {
        return alfa;
    }

    public String getSymbol()
    {
        return "P( " + alfa + " )";
    }

    
}

