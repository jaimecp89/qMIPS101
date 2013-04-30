import java.io.File;

import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;
import domain.engine.quantum.QuantumState;
import domain.engine.quantum.maps.NativeMap;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.load(new File("src/lib/qMIPS101JNI.dll").getAbsolutePath());
		QuantumState qs = new QuantumState();
		qs.add(new Complex(1.0,0.0), new ClassicState(new byte[]{1,0,1}));
		System.out.println(qs.stringIterator().next());
//		NativeMap nm = new NativeMap();
//		nm.put(new ClassicState(new byte[]{1,0,1}), new Complex(1.0,0.0));
//		System.out.println(nm.get(new ClassicState(new byte[]{1,0,1})));
	}

}
