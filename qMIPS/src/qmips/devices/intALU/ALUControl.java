package qmips.devices.intALU;

import qmips.devices.Device;
import qmips.others.Behavior;
import qmips.others.Bus;
import qmips.others.LogicVector;

/**
 * 
 * Unidad de control de la ALU tal y como se define
 * en el libro de Hennessy Patterson.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class ALUControl extends Device{

	private Bus func, aluOp, aluControl;
	
	public ALUControl(Bus func, Bus aluOp, Bus aluControl) {
		this.func = func;
		this.aluOp = aluOp;
		this.aluControl = aluControl;
		defineBehavior();
	}

	@Override
	protected void defineBehavior() {
		
		behavior(new Bus[]{func, aluOp}, new Behavior() {

			@Override
			public void task() {
				switch(aluOp.read().toInteger()){
				case 0:
					aluControl.write(new LogicVector(IntALU.ADD, 4));
					break;
				case 1:
					aluControl.write(new LogicVector(IntALU.SUB, 4));
					break;
				default:
					switch(func.read().toInteger()){
					case 0x00: //SLL
						aluControl.write(12, 4);
						break;
					case 0x02: //SRL
						aluControl.write(13, 4);
						break;
					case 0x03: //SRA
						aluControl.write(14, 4);
						break;
					case 0x20://ADD
						aluControl.write(new LogicVector(2, 4));
						break;
					case 0x21://ADDU
						aluControl.write(new LogicVector(3, 4));
						break;
					case 0x22://SUB
						aluControl.write(new LogicVector(6, 4));
						break;
					case 0x23://SUBU
						aluControl.write(new LogicVector(7, 4));
						break;
					case 0x24://AND
						aluControl.write(new LogicVector(0, 4));
						break;
					case 0x25://OR
						aluControl.write(new LogicVector(1, 4));
						break;
					case 0x26://XOR
						aluControl.write(new LogicVector(10, 4));
						break;
					case 0x27://NOR
						aluControl.write(new LogicVector(11, 4));
						break;
					case 0x2A://SLT
						aluControl.write(new LogicVector(15, 4));
						break;
					case 0x18://MULT
						aluControl.write(new LogicVector(4, 4));
						break;
					case 0x1A://DIV
						aluControl.write(new LogicVector(8, 4));
						break;
					case 0x1B://DIVU
						aluControl.write(new LogicVector(9, 4));
						break;
					}
				}
			}
		});
		
	}

}