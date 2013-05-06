package domain.engine.quantum.maps;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;

public class NativeArrayMap implements IterableMap{

public long ref;
	
	@Override
	public void clear(){_clear(ref);}
	public native void _clear(long ref);

	@Override
	public boolean containsKey(Object arg0){return _containsKey(ref, arg0);}
	public native boolean _containsKey(long ref, Object arg0);

	@Override
	public boolean containsValue(Object arg0){return _containsValue(ref, arg0);}
	public native boolean _containsValue(long ref, Object arg0);

	@Override
	public Set<java.util.Map.Entry<ClassicState, Complex>> entrySet(){return _entrySet(ref);}
	public native Set<java.util.Map.Entry<ClassicState, Complex>> _entrySet(long ref);

	@Override
	public Complex get(Object arg0){return _get(ref, arg0);}
	public native Complex _get(long ref, Object arg0);

	@Override
	public boolean isEmpty(){return _isEmpty(ref);}
	public native boolean _isEmpty(long ref);

	@Override
	public Set<ClassicState> keySet(){return _keySet(ref);}
	public native Set<ClassicState> _keySet(long ref);

	@Override
	public Complex put(ClassicState arg0, Complex arg1){return _put(ref, arg0, arg1);}
	public native Complex _put(long ref, ClassicState arg0, Complex arg1);

	@Override
	public void putAll(Map<? extends ClassicState, ? extends Complex> arg0){_putAll(ref, arg0);}
	public native void _putAll(long ref, Map<? extends ClassicState, ? extends Complex> arg0);

	@Override
	public Complex remove(Object arg0){return _remove(ref, arg0);}
	public native Complex _remove(long ref, Object arg0);

	@Override
	public int size(){return _size(ref);}
	public native int _size(long ref);

	@Override
	public Collection<Complex> values(){return _values(ref);}
	public native Collection<Complex> _values(long ref);
	
	public Entry createEntry(ClassicState cs, Complex c){
		return new Entry(cs, c);
	}
	
	public class Entry implements Map.Entry<ClassicState, Complex>{

		private ClassicState cs;
		private Complex c;
		
		public Entry(ClassicState cs, Complex c){
			this.cs = cs;
			this.c = c;
		}
		
		@Override
		public ClassicState getKey() {
			return cs;
		}

		@Override
		public Complex getValue() {
			return c;
		}

		@Override
		public Complex setValue(Complex value) {
			Complex res = this.c;
			this.c = value;
			return res;
		}
		
	}

	@Override
	public Iterator<java.util.Map.Entry<ClassicState, Complex>> iterator() {
		return this.entrySet().iterator();
	}
	
	

}
