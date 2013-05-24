package qmips.devices.control;

import qmips.devices.Device;
import qmips.others.Behavior;
import qmips.others.Bus;
import qmips.others.LogicVector;

/**
 * 
 * Unidad de control del MIPS de Hennessy Patterson monociclo.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class MonocycleControlUnit extends Device implements ControlUnit{

	private Bus regDst, jump, branch, memRead, memToReg, aluOp, memWrt, aluSrc,
			regWrite;
	private Bus instr;

	public MonocycleControlUnit(Bus instr, Bus regDst, Bus jump, Bus branch,
			Bus memRead, Bus memToReg, Bus aluOp, Bus memWrt, Bus aluSrc,
			Bus regWrite) {
		this.instr = instr;
		this.regDst = regDst;
		this.jump = jump;
		this.branch = branch;
		this.memRead = memRead;
		this.memToReg = memToReg;
		this.aluOp = aluOp;
		this.memWrt = memWrt;
		this.aluSrc = aluSrc;
		this.regWrite = regWrite;
		defineBehavior();
	}

	@Override
	protected void defineBehavior() {
		behavior(new Bus[]{instr}, new Behavior(){
			@Override
			public void task() {
				int intInstr = instr.read().toInteger();
				if(intInstr == 0){ //R-Type instruction
					regDst.write(new LogicVector(1,1));
					aluSrc.write(new LogicVector(0,1));
					memToReg.write(new LogicVector(0,1));
					regWrite.write(new LogicVector(1,1));
					memRead.write(new LogicVector(0,1));
					memWrt.write(new LogicVector(0,1));
					branch.write(new LogicVector(0,1));
					aluOp.write(new LogicVector(2,2));
					jump.write(new LogicVector(0,1));
				}else if(intInstr == 35 ){ //LW instruction
					regDst.write(new LogicVector(0,1));
					aluSrc.write(new LogicVector(1,1));
					memToReg.write(new LogicVector(1,1));
					regWrite.write(new LogicVector(1,1));
					memRead.write(new LogicVector(1,1));
					memWrt.write(new LogicVector(0,1));
					branch.write(new LogicVector(0,1));
					aluOp.write(new LogicVector(0,2));
					jump.write(new LogicVector(0,1));
				}else if(intInstr == 43 ){ //SW instruction
					regDst.write(new LogicVector(0,1));
					aluSrc.write(new LogicVector(1,1));
					memToReg.write(new LogicVector(0,1));
					regWrite.write(new LogicVector(0,1));
					memRead.write(new LogicVector(0,1));
					memWrt.write(new LogicVector(1,1));
					branch.write(new LogicVector(0,1));
					aluOp.write(new LogicVector(0,2));
					jump.write(new LogicVector(0,1));
				}else if(intInstr == 4){//Branch on equal instruction
					regDst.write(new LogicVector(0,1));
					aluSrc.write(new LogicVector(0,1));
					memToReg.write(new LogicVector(0,1));
					regWrite.write(new LogicVector(0,1));
					memRead.write(new LogicVector(0,1));
					memWrt.write(new LogicVector(0,1));
					branch.write(new LogicVector(1,1));
					aluOp.write(new LogicVector(1,2));
					jump.write(new LogicVector(0,1));
				}else if(intInstr == 2){//Unconditional jump instruction
					regDst.write(new LogicVector(0,1));
					aluSrc.write(new LogicVector(0,1));
					memToReg.write(new LogicVector(0,1));
					regWrite.write(new LogicVector(0,1));
					memRead.write(new LogicVector(0,1));
					memWrt.write(new LogicVector(0,1));
					branch.write(new LogicVector(0,1));
					aluOp.write(new LogicVector(1,2));
					jump.write(new LogicVector(1,1));
				}else if(intInstr == 8){//ADD Immediate
					regDst.write(new LogicVector(0,1));
					aluSrc.write(new LogicVector(1,1));
					memToReg.write(new LogicVector(0,1));
					regWrite.write(new LogicVector(1,1));
					memRead.write(new LogicVector(0,1));
					memWrt.write(new LogicVector(0,1));
					branch.write(new LogicVector(0,1));
					aluOp.write(new LogicVector(0,2));
					jump.write(new LogicVector(0,1));
				}
			}
		});
	}

	@Override
	public int checkTrap() {
		return -1;
	}

	@Override
	public void releaseTrap() {}

	@Override
	public boolean isIF() {
		return true;
	}

}
