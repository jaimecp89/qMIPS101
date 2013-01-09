package qmips.devices.memory;

import qmips.devices.Device;
import qmips.others.Behavior;
import qmips.others.Bus;
import qmips.others.LogicVector;

/**
 * 
 * Memoria que responde automaticamente para lecturas
 * pero se escribe en el flanco de subida.
 * Se puede definir su tamaño en numero de parabras de 32 bits.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class AsyncMemory extends Device implements IMemory{

	private LogicVector[] memContents;
	private Bus input, output, addr, rd, wr, clk;
	
	public AsyncMemory(Bus input, Bus output, Bus addr, Bus rd, Bus wr, Bus clk, int words) {
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
		
		behavior(new Bus[]{rd, addr}, new Behavior(){
			@Override
			public void task() {
				if(rd.read().get(0)){
					int dir = addr.read().toInteger()/4;
					if(dir >= 0){
						output.write(memContents[dir]);
					}
				}
			}
		});
		
		behavior(new Bus[]{clk}, new Behavior(){
			@Override
			public void task() {
				if(wr.read().get(0)){
					int dir = addr.read().toInteger()/4;
					if(dir >= 0){
						memContents[dir] = input.read();
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