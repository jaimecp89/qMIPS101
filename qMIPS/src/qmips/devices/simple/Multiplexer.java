package qmips.devices.simple;

import qmips.devices.Device;
import qmips.others.Behavior;
import qmips.others.Bus;

/**
 * 
 * Multiplexor de cualquier numero de entradas.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class Multiplexer extends Device{

	private Bus control;
	private Bus inputs[];
	private Bus output;
	
	
	public Multiplexer(Bus control, Bus inputs[], Bus output) {
		this.control = control;
		this.inputs = inputs;
		this.output = output;
		defineBehavior();
	}
	
	private static Bus[] solveWires(Bus control, Bus inputs[]){
		Bus[] res = new Bus[inputs.length + 1];
		System.arraycopy(inputs, 0, res, 0, inputs.length);
		res[inputs.length] = control;
		return res;
	}


	@Override
	protected void defineBehavior() {
		behavior(solveWires(control, inputs), new Behavior() {

			@Override
			public void task() {
				int index = control.read().toInteger();
				output.write(inputs[index].read());
			}
			
		});
	}

}