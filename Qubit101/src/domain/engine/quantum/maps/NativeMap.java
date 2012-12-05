package domain.engine.quantum.maps;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;

public class NativeMap implements IterableMap{

	int dir;
	
	public NativeMap(){
		dir = _NativeMap();
	}
	
	private native int _NativeMap();

	@Override
	public void clear() {
		_clear(dir);
	}
	
	private static native void _clear(int dir);

	@Override
	public boolean containsKey(Object arg0) {
		return _containsKey(dir, arg0);
	}
	
	private static native boolean _containsKey(int dir, Object arg0);

	@Override
	public boolean containsValue(Object arg0) {
		return _containsValue(dir, arg0);
	}
	
	private static native boolean _containsValue(int dir, Object arg0);

	@Override
	//NOT SUPPORTED
	public Set<java.util.Map.Entry<ClassicState, Complex>> entrySet() {
		return null;
	}
	

	@Override
	public Complex get(Object arg0) {
		return _get(dir, arg0);
	}
	
	private static native Complex _get(int dir, Object arg0);

	@Override
	public boolean isEmpty() {
		return _isEmpty(dir);
	}

	private static native boolean _isEmpty(int dir);
	
	@Override
	//NOT SUPPORTED
	public Set<ClassicState> keySet() {
		return null;
	}

	@Override
	public Complex put(ClassicState arg0, Complex arg1) {
		return _put(dir, arg0, arg1);
	}
	
	private static native Complex _put(int dir, ClassicState arg0, Complex arg1);

	@Override
	//NOT SUPPORTED
	public void putAll(Map<? extends ClassicState, ? extends Complex> arg0) {}

	@Override
	public Complex remove(Object arg0) {
		return _remove(dir, arg0);
	}
	
	private static native Complex _remove(int dir, Object arg0);

	@Override
	public int size() {
		return _size(dir);
	}
	
	private static native int _size(int dir);

	@Override
	//NOT SUPPORTED
	public Collection<Complex> values() {
		return null;
	}

	@Override
	public Iterator<java.util.Map.Entry<ClassicState, Complex>> iterator() {
		return null;
	}
	
	
}
