package qmips.devices.simple;

import qmips.devices.Device;
import qmips.others.Behavior;
import qmips.others.Bus;
import qmips.others.LogicVector;

/**
 * 
 * Extensor de signo de 16 a 32 bits.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class SignExt extends Device {

	Bus input, output;

	public SignExt(Bus input, Bus output) {
		this.input = input;
		this.output = output;
		defineBehavior();
	}

	@Override
	protected void defineBehavior() {

		behavior(new Bus[] { input }, new Behavior() {

			@Override
			public void task() {
				LogicVector res = new LogicVector(32);
				LogicVector in = input.read();
				boolean sign = in.get(15);
				for (int i = 0; i < 32; i++) {
					if (i <= 15)
						res.set(i, in.get(i));
					else
						res.set(i, sign);
				}
				output.write(res);
			}
		});
	}

}