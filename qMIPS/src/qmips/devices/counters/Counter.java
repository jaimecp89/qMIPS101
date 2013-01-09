package qmips.devices.counters;

import qmips.devices.Device;
import qmips.others.Behavior;
import qmips.others.Bus;
import qmips.others.LogicVector;

/**
 * 
 * Un contador ascendente simple de 32 bits.
 * En el flanco de subida, si inc = 1, se incrementa.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class Counter extends Device{

	private LogicVector content;
	private Bus input, output, ld, inc, rst, clk;

	public Counter(Bus input, Bus output, Bus ld, Bus inc, Bus rst, Bus clk) {
		content = new LogicVector(32);
		this.input = input;
		this.output = output;
		this.ld = ld;
		this.inc = inc;
		this.rst = rst;
		this.clk = clk;
		defineBehavior();
	}

	@Override
	protected void defineBehavior() {
		behavior(new Bus[]{clk, rst}, new Behavior(){
			@Override
			public void task() {
				if(rst.read().get(0)){
					content = new LogicVector(input.size());
					output.write(content);
				}
				else if(clk.read().get(0)){
					if(ld.read().get(0)){
						content = input.read();
					}else if(inc.read().get(0)){
						content = LogicVector.intToLogicVector(content.toInteger() + 1);
						output.write(content);
					}
				}
			}
		});
	}
}