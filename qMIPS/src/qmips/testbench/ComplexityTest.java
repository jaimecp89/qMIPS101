package qmips.testbench;

import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;
import domain.engine.quantum.QuantumState;
import domain.engine.quantum.gates.HadamardGate;

public class ComplexityTest {

	public static void main(String[] args){
		int iterations = 50;
		int qubits = 22;
		long[][] res = new long[iterations][qubits];
		for(int x = 0; x < iterations; x++){
			byte[] b = new byte[qubits];
			ClassicState cs = new ClassicState(b);
			QuantumState qs = new QuantumState();
			qs.add(new Complex(1.0,0.0), cs);
			HadamardGate hg = new HadamardGate();
			for(int i = 0; i < b.length; i++){
				long t = System.currentTimeMillis();
				qs = hg.operate(qs, i);
				res[x][i] = System.currentTimeMillis() - t;
			}
		}
		long med[] = new long[qubits];
		for(int i = 0; i < qubits; i++){
			med[i] = Long.MAX_VALUE;
		}
		for(int i = 0; i < iterations; i++){
			for(int j = 0; j < qubits; j++){
				med[j] = med[j] <  res[i][j] ? med[j] : res[i][j];
			}
		}
		for(int i = 0; i < qubits; i++){
			//System.out.println("Qubit: " + (i+1) + ", time: " + med[i] + " ms.");
			System.out.println((i+1) + "\t" + med[i]);
		}
		
	}
	
}
