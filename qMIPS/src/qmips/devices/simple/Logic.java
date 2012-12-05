package qmips.devices.simple;

import qmips.devices.Device;
import qmips.others.Behavior;
import qmips.others.Bus;
import qmips.others.LogicVector;

/**
 * 
 * Puertas logicas genericas.
 * Compatible con cualquier ancho de bus de entrada.
 * Se concreta la puerta logica en el constructor.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class Logic extends Device{

	public static final int AND = 0, OR = 1, NAND = 2, XOR = 3, NOR = 4, XNOR = 5;
	private Bus i1, i2, o;
	private int op;
	
	public Logic(Bus i1, Bus i2, Bus o, int op) {
		this.i1 = i1;
		this.i2 = i2;
		this.o = o;
		this.op = op;
		defineBehavior();
	}

	@Override
	protected void defineBehavior() {
		
		behavior(new Bus[]{i1, i2}, new Behavior() {

			@Override
			public void task() {
				int ii1 = i1.read().toInteger();
				int ii2 = i2.read().toInteger();
				int res = 0;
				switch(op){
				case AND:
					res = ii1 & ii2;
					break;
				case OR:
					res = ii1 | ii2;
					break;
				case NAND:
					res = ~(ii1 & ii2);
					break;
				case XOR:
					res = ii1 ^ ii2;
					break;
				case NOR:
					res = ~(ii1 | ii2);
					break;
				case XNOR:
					res = ~(ii1 ^ ii2);
					break;
				}
				o.write(new LogicVector(res, o.size()));
			}
			
		});
		
	}

	

}