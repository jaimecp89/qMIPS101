package domain.engine.quantum.interfaces;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;

public class NativeMap implements Map<ClassicState, Complex>{

	public NativeMap(){
		
	}

	@Override
	public native void clear();

	@Override
	public native boolean containsKey(Object arg0);

	@Override
	public native boolean containsValue(Object arg0);

	@Override
	public native Set<Entry<ClassicState, Complex>> entrySet();

	@Override
	public native Complex get(Object arg0);

	@Override
	public native boolean isEmpty();

	@Override
	public native Set<ClassicState> keySet();

	@Override
	public native Complex put(ClassicState arg0, Complex arg1);

	@Override
	public native void putAll(Map arg0);

	@Override
	public native Complex remove(Object arg0);

	@Override
	public native int size();

	@Override
	public native Collection<Complex> values();
	
	
}
