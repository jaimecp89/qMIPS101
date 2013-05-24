package qmips.presentation.builders;

import java.util.Map;

import qmips.devices.Device;
import qmips.devices.clock.Clock;
import qmips.devices.control.ControlUnit;
import qmips.devices.control.MonocycleControlUnit;
import qmips.devices.intALU.ALUControl;
import qmips.devices.intALU.IntALU;
import qmips.devices.memory.AsyncMemory;
import qmips.devices.memory.IMemory;
import qmips.devices.memory.Memory;
import qmips.devices.registerFile.RegisterFile;
import qmips.devices.simple.Concat;
import qmips.devices.simple.Logic;
import qmips.devices.simple.Multiplexer;
import qmips.devices.simple.ShiftLeft;
import qmips.devices.simple.SignExt;
import qmips.devices.simple.SynchronousRegister;
import qmips.others.Bus;

public class OneCycleMIPS implements Builder {
	
	private Map<String, Device> displayable;
	private Clock clockDev;
	private IMemory instrMemory;
	private Bus rst;
	private ControlUnit control;
	private SynchronousRegister programCounter;
	
	@Override
	public void build() {
		
		// Buses:
		
		// Clock
		Bus clk = new Bus(1);

		// Instruction pointer resolution
		Bus nextInstrPtr = new Bus(32);
		Bus instrPtr = new Bus(32);
		Bus instrPtr4 = new Bus(32);
		Bus instrJmpAddr = new Bus(32);
		Bus instrJmpAddrShift = new Bus(26);
		Bus instrBchAddr = new Bus(32);
		Bus bchToJmp = new Bus(32);

		// Current instruction
		Bus instr = new Bus(32);
		Bus sgnExtInstr = new Bus(32);
		Bus shfInstr = new Bus(32);

		// Register file management
		Bus wrReg = new Bus(5);
		Bus rdData1 = new Bus(32);
		Bus rdData2 = new Bus(32);
		Bus wbBus = new Bus(32);

		// ALU management
		Bus aluInput2 = new Bus(32);
		Bus aluFlags = new Bus(3);
		Bus aluRes = new Bus(32);

		// Data memory management
		Bus rdData = new Bus(32);

		// Control lines
		Bus regDst = new Bus(1);
		Bus jump = new Bus(1);
		Bus branch = new Bus(1);
		Bus memRd = new Bus(1);
		Bus memToReg = new Bus(1);
		Bus aluOp = new Bus(2);
		Bus aluControl = new Bus(4);
		Bus memWr = new Bus(1);
		Bus aluSrc = new Bus(1);
		Bus regWrEn = new Bus(1);

		// Constant
		Bus vcc1 = new Bus(1, 1);
		Bus gnd1 = new Bus(1);
		

		// Devices:
		
		// Clock
		clockDev = new Clock(clk);

		// Instruction memory
		instrMemory = new AsyncMemory(null, instr, instrPtr, vcc1, gnd1, clk, 512);

		// Data memory
		new Memory(rdData2, rdData, aluRes, vcc1, memWr, clk, 512);

		// Instruction pointer register
		programCounter = new SynchronousRegister(nextInstrPtr, instrPtr, vcc1, gnd1, clk);
		displayable.put("Instruction pointer", programCounter);
		
		// ALU
		new IntALU(rdData1, aluInput2, aluControl, aluRes, new Bus(32),
				aluFlags);
		
		// ALU Control unit
		new ALUControl(instr.getRange(0, 6), aluOp, aluControl);

		// No jump next instruction adder
		new IntALU(instrPtr, new Bus(4, 32), new Bus(IntALU.ADDU, 32),
				instrPtr4, new Bus(32), new Bus(3));

		// Branch solve adder
		new IntALU(instrPtr4, shfInstr, new Bus(IntALU.ADDU, 32), instrBchAddr,
				new Bus(32), new Bus(3));

		// Register file
		displayable.put("Register file", new RegisterFile(instr.getRange(21, 26), instr.getRange(16, 21),
				rdData1, rdData2, wrReg, regWrEn, wbBus, rst, clk));

		// Write register multiplexer
		new Multiplexer(regDst, new Bus[] { instr.getRange(16, 21),
				instr.getRange(11, 16) }, wrReg);

		// Alu data B multiplexer
		new Multiplexer(aluSrc, new Bus[] { rdData2, sgnExtInstr }, aluInput2);

		// Write back multiplexer
		new Multiplexer(memToReg, new Bus[] { aluRes, rdData }, wbBus);

		// Branch control multiplexer ( jump if zero & branch )
		Bus andRes = new Bus(1);
		new Multiplexer(andRes, new Bus[] { instrPtr4, instrBchAddr },
				bchToJmp);
		new Logic(branch, aluFlags.getRange(0, 1), andRes, Logic.AND);

		// Jump control multiplexer
		new Multiplexer(jump, new Bus[] { bchToJmp, instrJmpAddr },
				nextInstrPtr);

		// Control Unit
		control = new MonocycleControlUnit(instr.getRange(26, 32), regDst, jump, branch,
				memRd, memToReg, aluOp, memWr, aluSrc, regWrEn);
		
		//Sign extender
		new SignExt(instr.getRange(0, 16), sgnExtInstr);
		
		//Left shifter
		new ShiftLeft(sgnExtInstr, shfInstr, 2);
		
		//Left shifter
		new ShiftLeft(instr.getRange(0, 26), instrJmpAddrShift, 2);
		
		new Concat(new Bus[]{instrPtr4.getRange(28, 32), instrJmpAddrShift}, instrJmpAddr);
		
	}

	@Override
	public Map<String, Device> getDisplayableDevices() {
		return displayable;
	}

	@Override
	public Clock getClock() {
		return clockDev;
	}

	@Override
	public IMemory getInstrMemory() {
		return instrMemory;
	}

	@Override
	public Bus getResetBus() {
		return rst;
	}

	@Override
	public ControlUnit getControlUnit() {
		return control;
	}

	@Override
	public SynchronousRegister programCounter() {
		return programCounter;
	}

}
