package qmips.devices.counters;

import qmips.devices.Device;
import qmips.others.Behavior;
import qmips.others.Bus;
import qmips.others.LogicVector;

/**
 * 
 * Contador ascendente y descendente.
 * En el flanco de subida se incrementa o decrementa dependiendo
 * del estado de los buses inc y dec.
 * Se le puede cargar un valor por input si ld esta activo en el
 * flanco.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class UpdownCounter extends Device{
	
	private LogicVector content;
	private Bus input, output, ld, inc, dec, rst, clk;

	public UpdownCounter(Bus input, Bus output, Bus ld, Bus inc, Bus dec, Bus rst, Bus clk) {
		content = new LogicVector(32);
		this.input = input;
		this.output = output;
		this.ld = ld;
		this.inc = inc;
		this.dec = dec;
		this.rst = rst;
		this.clk = clk;
	}

	@Override
	protected void defineBehavior() {
		behavior(new Bus[]{clk, rst}, new Behavior(){
			@Override
			public void task() {
				if(rst.read().get(0)){
					content = new LogicVector(input.size());
					output.write(content);
				}else if(clk.read().get(0)){
					if(ld.read().get(0)){
						content = input.read();
					}else if(inc.read().get(0)){
						content = LogicVector.intToLogicVector(content.toInteger() + 1);
						output.write(content);
					}else if(dec.read().get(0)){
						content = LogicVector.intToLogicVector(content.toInteger() - 1);
						output.write(content);
					}
				}
			}
		});
	}

}