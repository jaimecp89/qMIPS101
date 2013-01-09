package qmips.devices.simple;

import qmips.devices.Device;
import qmips.others.Behavior;
import qmips.others.Bus;
import qmips.others.LogicVector;

/**
 * 
 * Dispositivo que desplaza a la izquierda el valor
 * de la entrada la cantidad indicada.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class ShiftLeft extends Device{

	private Bus input, output;
	private int amount;
	
	public ShiftLeft(Bus input, Bus output, int amount) {
		this.input = input;
		this.output = output;
		this.amount = amount;
		defineBehavior();
	}

	@Override
	protected void defineBehavior() {
		
		behavior(new Bus[] { input }, new Behavior() {

			@Override
			public void task() {
				output.write(new LogicVector(input.read().toInteger() << amount, 32));
			}
		});
		
	}


}	