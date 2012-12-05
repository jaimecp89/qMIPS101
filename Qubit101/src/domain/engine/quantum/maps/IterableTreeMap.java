package domain.engine.quantum.maps;

import java.util.Iterator;
import java.util.TreeMap;

import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;

public class IterableTreeMap extends TreeMap<ClassicState, Complex> implements IterableMap{

	private static final long serialVersionUID = -3742358892070112142L;

	@Override
	public Iterator<java.util.Map.Entry<ClassicState, Complex>> iterator() {
		return this.entrySet().iterator();
	}

}
