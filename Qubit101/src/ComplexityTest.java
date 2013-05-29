

import java.io.File;

import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;
import domain.engine.quantum.QuantumState;
import domain.engine.quantum.gates.HadamardGate;

public class ComplexityTest {

	public static void main(String[] args){
		System.load(new File("src/lib/qMIPS101JNI.dll").getAbsolutePath());
		int iterations = 10;
		int qubits = 22;
		int mb = 1024*1024;
		Runtime runtime = Runtime.getRuntime();
		long[][] res = new long[iterations][qubits];
		long[][] mem = new long[iterations][qubits];
		for(int x = 0; x < iterations; x++){
			byte[] b = new byte[qubits];
			ClassicState cs = new ClassicState(b);
			QuantumState qs = new QuantumState(QuantumState.NATIVEARRAYMAPTYPE);
			qs.add(new Complex(1.0,0.0), cs);
			HadamardGate hg = new HadamardGate();
			for(int i = 0; i < b.length; i++){
				long t = System.currentTimeMillis();
				qs = hg.operate(qs, i);
				res[x][i] = System.currentTimeMillis() - t;
				mem[x][i] = (runtime.totalMemory() - runtime.freeMemory()) / mb;
			}
		}
		long med[] = new long[qubits];
		long medmem[] = new long[qubits];
		for(int i = 0; i < qubits; i++){
			med[i] = Long.MAX_VALUE;
			medmem[i] = Long.MAX_VALUE;
		}
		for(int i = 0; i < iterations; i++){
			for(int j = 0; j < qubits; j++){
				med[j] = med[j] <  res[i][j] ? med[j] : res[i][j];
				medmem[j] = medmem[j] <  mem[i][j] ? medmem[j] : mem[i][j];
			}
		}
		for(int i = 0; i < qubits; i++){
			System.out.println((i+1) + "\t" + med[i] + "\t" +medmem[i]);
		}
		
	}
	
}
