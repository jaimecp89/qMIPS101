package qmips.others;

public class LogicVector{

	private HBoolean[] array;
	
	public LogicVector(int size){
		array = new HBoolean[size];
		for(int i = 0; i < size; i++)
			array[i] = new HBoolean();
	}
	
	public LogicVector(Boolean[] values){
		HBoolean[] res = new HBoolean[values.length];
		for(int i = 0; i < values.length; i++){
			res[i] = new HBoolean();
			res[i].value = values[i];
		}
	}
	
	public LogicVector(int number, int size){
		this(size);
		LogicVector lv = LogicVector.intToLogicVector(number);
		for(int i = 0; i < size; i++)
			array[i].value = lv.get(i);
	}
	
	public LogicVector(String bin){
		array = new HBoolean[bin.length()];
		char[] chr = bin.toCharArray();
		for(int i = 0; i < array.length; i++){
			HBoolean aux = new HBoolean();
			if(chr[i] == '1')
				aux.value = true;
			else if(chr[i] != '0')
				throw new IllegalArgumentException();
			array[i] = aux;
		}
	}
	
	private LogicVector(HBoolean[] values){
		array = values;
	}
	
	public Boolean get(int i){
		return array[i].value;
	}
	
	public LogicVector get(int from, int to){
		HBoolean[] res = new HBoolean[to - from];
		System.arraycopy(array, from, res, 0, to - from);
		return new LogicVector(res);
	}
	
	public void set(int i){
		array[i].value = true;
	}
	
	public void set(int i, boolean value){
		array[i].value = value;
	}
	
	public void set(int from, int to){
		for(int i = from; i < to; i++)
			array[i].value = true;
	}
	
	public int size(){
		return array.length;
	}
	
	public int toInteger(){
		int bitInteger = 0;
		int size = (size() < 32) ? size(): 32;
	    for(int i = 0 ; i < size; i++)
	        if(get(i))
	            bitInteger |= (1 << i);
	    return bitInteger;
	}
	
	public static LogicVector intToLogicVector(int value)
	{
		LogicVector lv = new LogicVector(32);
		int mask = 1;
		for (int i = 0; i < 32; ++i, mask <<= 1)
			if ((mask & value) > 0)
				lv.set(i);
		if (value < 0)
			lv.set(31);
		return lv;
	}
	
	public String toString(){
		String res = "";
		for(int i = size() - 1; i >= 0; i--){
			res += get(i)? "1" : "0";
		}
		return res + " (" + toInteger() + ")";
	}
	
	public boolean equals(LogicVector v){
		boolean res = v.size() == this.size();
		if(res)
			for(int i = 0; i < size(); i++){
				res = v.get(i).booleanValue() == this.get(i).booleanValue();
				if(!res) break;
			}
		return res;
	}
	
	class HBoolean { boolean value = false; }
	
}