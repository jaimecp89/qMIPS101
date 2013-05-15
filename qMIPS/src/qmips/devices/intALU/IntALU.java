package qmips.devices.intALU;

import qmips.devices.Device;
import qmips.others.Behavior;
import qmips.others.Bus;
import qmips.others.LogicVector;

/**
 * 
 * Unidad aritmetico logica entera.
 * Es facil ver las operaciones que realiza en el codigo.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class IntALU extends Device {

	private Bus a, b, flags, op, outputlow, outputhi;
	public final static int 
		AND = 0, 
		OR = 1, 
		ADD = 2,
		ADDU = 3, 
		MULT = 4,
		MULTU = 5, 
		SUB = 6, 
		SUBU = 7, 
		DIV = 8, 
		DIVU = 9, 
		XOR = 10,
		NOR = 11,
		SLL = 12, 
		SRL = 13, 
		SRA = 14,
		SLT = 15;

	public IntALU(Bus a, Bus b, Bus op, Bus outputlow, Bus outputhi, Bus flags) {
		this.a = a;
		this.b = b;
		this.flags = flags;
		this.op = op;
		this.outputlow = outputlow;
		this.outputhi = outputhi;
		defineBehavior();
	}

	@Override
	protected void defineBehavior() {

		behavior(new Bus[] { a, b, op }, new Behavior() {

			@Override
			public void task() {
				int operation = op.read().toInteger();
				LogicVector opa = a.read();
				int iopa = opa.toInteger();
				LogicVector opb = b.read();
				int iopb = opb.toInteger();
				int ires = 0;
				int upres = 0;
				long aux = 0;
				boolean overf = false;
				boolean negat = false;
				switch (operation) {

				case ADD:
					ires = iopa + iopb;
					if ((iopa > 0 && iopb > 0 && ires < 0)
							|| (iopa < 0 && iopb < 0 && ires > 0))
						overf = true;
					if (ires < 0)
						negat = true;
					break;

				case ADDU:
					ires = iopa + iopb;
					if (ires < 0)
						negat = true;
					break;

				case SUB:
					ires = iopa - iopb;
					if ((iopa > 0 && iopb < 0 && ires < 0)
							|| (iopa < 0 && iopb > 0 && ires > 0))
						overf = true;
					if (ires < 0)
						negat = true;
					break;

				case SUBU:
					ires = iopa - iopb;
					break;

				case MULT:
					aux = (long) iopa * (long) iopb;
					ires = (int) aux;
					upres = (int) (aux >> 32);
					if (upres < 0)
						negat = true;
					break;

				case MULTU:
					aux = (long) iopa * (long) iopb;
					ires = (int) aux;
					upres = (int) (aux >> 32);
					if (upres < 0)
						negat = true;
					break;

				case DIV:
					ires = iopa / iopb;
					upres = iopa % iopb;
					if (ires < 0)
						negat = true;
					break;

				case DIVU:
					ires = iopa / iopb;
					upres = iopa % iopb;
					if (ires < 0)
						negat = true;
					break;

				case AND:
					ires = iopa & iopb;
					break;

				case OR:
					ires = iopa | iopb;
					break;

				case XOR:
					ires = iopa ^ iopb;
					break;

				case SLL:
					ires = iopa << iopb;
					break;

				case SRL:
					ires = iopa >>> iopb;
					break;

				case SRA:
					ires = iopa >> iopb;
					break;

				case SLT:
					ires = iopa < iopb ? 1 : 0;
					break;

				}

				LogicVector aflags = new LogicVector(3);
				if (overf)
					aflags.set(0);

				if (negat)
					aflags.set(1);

				if (ires == 0 && upres == 0)
					aflags.set(2);

				flags.write(aflags);
				outputhi.write(LogicVector.intToLogicVector(upres));
				outputlow.write(LogicVector.intToLogicVector(ires));
			}

		});

	}

}