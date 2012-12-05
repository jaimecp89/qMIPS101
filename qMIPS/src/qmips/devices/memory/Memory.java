package qmips.devices.memory;

import qmips.devices.Device;
import qmips.others.Behavior;
import qmips.others.Bus;
import qmips.others.LogicVector;

/**
 * 
 * Memoria totalmente sincrona.
 * Lee y escribe en el flanco de subida del reloj.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class Memory extends Device implements IMemory{

	private LogicVector[] memContents;
	private Bus input, output, addr, rd, wr, clk;
	
	public Memory(Bus input, Bus output, Bus addr, Bus rd, Bus wr, Bus clk, int words) {
		this.input = input;
		this.output = output;
		this.rd = rd;
		this.wr = wr;
		this.clk = clk;
		this.addr = addr;
		memContents = new LogicVector[words];
		for(int i = 0; i < words; i++){
			memContents[i] = new LogicVector(32);
		}
		defineBehavior();
	}
	
	public void load(LogicVector v, int dir){
		memContents[dir/4] = v;
	}

	@Override
	protected void defineBehavior() {
		behavior(new Bus[]{clk}, new Behavior() {

			@Override
			public void task() {
				if(clk.read().get(0) && wr.read().get(0)){
					memContents[addr.read().toInteger()/4] = input.read();
				}
				if(clk.read().get(0) && rd.read().get(0)){
					int intaddr = addr.read().toInteger()/4;
					if(intaddr >= 0){
						output.write(memContents[intaddr]);
					}
				}
			}
			
		});
	}
	
	@Override
	public int size() {
		return memContents.length;
	}

}