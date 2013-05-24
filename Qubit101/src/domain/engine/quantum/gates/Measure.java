package domain.engine.quantum.gates;

import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;
import domain.engine.quantum.QuantumState;
import domain.engine.quantum.interfaces.Gate;

import java.util.*;
import java.util.Map.Entry;

public class Measure implements Gate {

	private byte measurement;
	
	public Measure() {
		measurement = -1;
	}

	public QuantumState operate(QuantumState input, int targetQubit) {
		QuantumState res = new QuantumState();
		double random = (new Random()).nextDouble();
		double prob0 = 0.0D;
		for (Entry<ClassicState, Complex> e : input) {
			if (e.getKey().getState()[targetQubit] == 0)
				prob0 += Math.pow(e.getValue().getMagnitude().doubleValue(),
						2.0);
		}
		
		measurement = 0;
		if (random > prob0)
			measurement = 1;
		if (prob0 == 0.0D) {
			res = input;
		} else {
			Complex norm[] = {
					new Complex(Double.valueOf(1.0D / Math.sqrt(prob0)),
							Double.valueOf(0.0D), Boolean.valueOf(false)),
					new Complex(Double.valueOf(1.0D / Math.sqrt(1.0D - prob0)),
							Double.valueOf(0.0D), Boolean.valueOf(false)) };
			for (Entry<ClassicState, Complex> e : input) {
				if (e.getKey().getState()[targetQubit] == measurement)
					res.add(e.getValue().multiply(norm[measurement]),
							(ClassicState) e.getKey().clone());
			}

		}
		return res;
	}

	public byte getMeasurementResult() {
		if (measurement == -1)
			throw new IllegalStateException("Measurement still not computed.");
		else
			return measurement;
	}

	public int getGateId() {
		return -3;
	}

	public String getSymbol() {
		return "Meas";
	}
	
}
