package domain.engine.quantum.maps;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;

public class ArraySortedMap implements IterableMap{
	 
	private Entry<ClassicState, Complex>[] amap;
	private int stateSize;
	private static final Complex ZERO = new Complex();

	@Override
	public void clear() {
		amap = null;
	}

	@Override
	public boolean containsKey(Object arg0) {
		return false;
	}

	@Override
	public boolean containsValue(Object arg0) {
		Complex c = (Complex)arg0;
		for(Entry<ClassicState, Complex> aux : amap){
			if(c.equals(aux.getValue())) return true;
		}
		return false;
	}

	@Override
	public Set<Entry<ClassicState, Complex>> entrySet() {
		Set<Entry<ClassicState, Complex>> s = new HashSet<Entry<ClassicState, Complex>>();
		for(Entry<ClassicState, Complex> e : amap){
			s.add(e);
		}
		return s;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Complex get(Object arg0) {
		if(amap == null){ 
			return ZERO;
		}
		Entry<ClassicState, Complex> aux = amap[classicStateToInt((ClassicState)arg0)];
		if(aux != null)
			return aux.getValue();
		return ZERO;
	}

	@Override
	public boolean isEmpty() {
		for(Entry<ClassicState, Complex> aux : amap){
			if(aux != null) return false;
		}
		return true;
	}

	@Override
	public Set<ClassicState> keySet() {
		Set<ClassicState> res = new HashSet<ClassicState>();
		for(Entry<ClassicState, Complex> aux : amap){
			res.add(aux.getKey());
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Complex put(ClassicState arg0, Complex arg1) {
		if(amap == null){ 
			stateSize = arg0.getState().length; 
			amap = new Entry[(int)Math.pow(2, stateSize)];
		}
		Complex aux = get(arg0);
		if(arg1.equals(ZERO)){
			amap[classicStateToInt(arg0)] = null;
		}else
			amap[classicStateToInt(arg0)] = new MyEntry(arg0, arg1);
		return aux;
	}

	@Override
	public void putAll(Map<? extends ClassicState, ? extends Complex> arg0) {
		for(Map.Entry<? extends ClassicState, ? extends Complex> e: arg0.entrySet()){
			put(e.getKey(), e.getValue());
		}
	}

	@Override
	public Complex remove(Object arg0) {
		Complex aux = get(arg0);
		amap[classicStateToInt((ClassicState)arg0)] = null;
		return aux;
	}

	@Override
	public int size() {
		return amap.length;
	}

	@Override
	public Collection<Complex> values() {
		return null;
	}
	
	public String toString(){
		String res = "[";
		for(Entry<ClassicState, Complex> e : this){
			res+= e.getKey() + " -> " + e.getValue() + ", ";
		}
		res+="]";
		return res;
	}
	
	private int classicStateToInt(ClassicState s){
		byte[] b = s.getState();
		int res = 0;
		for(int i = 0; i < b.length; i++){
			if(b[i] == 1)
				res += 1 << i;
		}
		return res;
	}

	@Override
	public Iterator<java.util.Map.Entry<ClassicState, Complex>> iterator() {
		return new ArrayMapIterator();
	}
	
	private class ArrayMapIterator implements Iterator<java.util.Map.Entry<ClassicState, Complex>>{

		int i = 0;
		
		@Override
		public boolean hasNext() {
			if(amap!=null)
				while( i < amap.length && amap[i]==null )
					i++;
			else return false;
			return i < amap.length;
		}

		@Override
		public Entry<ClassicState, Complex> next() {
			Entry<ClassicState, Complex> e = null;
			while(e==null){
				e = amap[i];
				i++;
			}
			return e;
		}

		@Override
		public void remove() {}
		
	}
	
	private class MyEntry implements Map.Entry<ClassicState, Complex>{

		private ClassicState key;
		private Complex value;
		
		public MyEntry(ClassicState key, Complex value){
			this.key = key;
			this.value = value;
		}
		
		@Override
		public ClassicState getKey() {
			return key;
		}

		@Override
		public Complex getValue() {
			return value;
		}

		@Override
		public Complex setValue(Complex value) {
			Complex aux = value;
			this.value = value;
			return aux;
		}
		
		public String toString(){
			return value.toString();
		}
		
	}

}
