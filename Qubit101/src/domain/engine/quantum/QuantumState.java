package domain.engine.quantum;

import java.util.Iterator;
import java.util.Map.Entry;

import domain.engine.math.Complex;
import domain.engine.quantum.maps.NativeMap;
import domain.engine.quantum.maps.ArraySortedMap;
import domain.engine.quantum.maps.IterableMap;
import domain.engine.quantum.maps.IterableTreeMap;



public class QuantumState implements Iterable<Entry<ClassicState, Complex>> {

	public static final int TREEMAPTYPE = 1, ARRAYSORTEDMAPTYPE = 2, 
			NATIVETREEMAP = 3, NATIVEARRAYMAP = 4;
	private IterableMap qState;

	public QuantumState() {
		qState = new IterableTreeMap();
	}
	
	public QuantumState(int mapType){
		switch(mapType){
		case TREEMAPTYPE:
			qState = new IterableTreeMap();
			break;
		case ARRAYSORTEDMAPTYPE:
			qState = new ArraySortedMap();
			break;
		case NATIVETREEMAP:
			qState = new NativeMap();
			break;
		case NATIVEARRAYMAP:
			System.err.println("Native array map not implmented yet.");
			break;
		default:
			throw new IllegalArgumentException("Unknown map type.");
		}
	}

	public void add(Complex coef, ClassicState cs) {
		Complex tmp = qState.get(cs);
		if (tmp != null) {
			tmp = tmp.add(coef);
			qState.put(cs, tmp);
			if (tmp.equals(new Complex()))
				qState.remove(cs);
		} else if (!coef.equals(new Complex()))
			qState.put(cs, coef);
	}

	public String toString() {
		Iterator<Entry<ClassicState, Complex>> i = qState.iterator();
		String res;
		Entry<ClassicState, Complex> ent;
		ClassicState c;
		for (res = ""; i.hasNext(); res = res+ent.getValue()+"|"+c+"> + \n") {
			ent = i.next();
			c = (ClassicState) ent.getKey();
		}
		if (res.length() > 0)
			return res.substring(0, res.length() - 3);
		else
			return "0.0";
	}

	public Iterator<Entry<ClassicState, Complex>> iterator() {
		return qState.iterator();
	}

	public Iterator<String> stringIterator() {
		return new StringIterator();
	}

	private class StringIterator implements Iterator<String> {

		Iterator<Entry<ClassicState, Complex>> it = qState.entrySet().iterator();
		int length = qState.size();
		int cont = 0;

		public String next() {
			Entry<ClassicState, Complex> e = it.next();
			cont++;
			String res;
			if (cont == length)
				res = e.getValue() + " |" + e.getKey() + ">";
			else
				res = e.getValue() + " |" + e.getKey() + "> + ";
			return res;
		}

		public boolean hasNext() {
			return it.hasNext();
		}

		public void remove() {}
		
	}

}
