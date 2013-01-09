package qmips.presentation.builders;

import java.util.Map;
import java.util.TreeMap;

import qmips.devices.Device;
import qmips.devices.clock.Clock;
import qmips.devices.control.ControlUnit;
import qmips.devices.control.QuantumMIPSControlUnit;
import qmips.devices.intALU.ALUControl;
import qmips.devices.intALU.IntALU;
import qmips.devices.memory.AsyncMemory;
import qmips.devices.memory.IMemory;
import qmips.devices.quantum.QuantumControl;
import qmips.devices.quantum.QubitArray32;
import qmips.devices.quantum.QubitTargetControl;
import qmips.devices.registerFile.RegisterFile;
import qmips.devices.simple.Concat;
import qmips.devices.simple.Logic;
import qmips.devices.simple.Multiplexer;
import qmips.devices.simple.ShiftLeft;
import qmips.devices.simple.SignExt;
import qmips.devices.simple.SynchronousRegister;
import qmips.others.Bus;

public class QuantumMIPS implements Builder{
	

	private Map<String, Device> displayable;
	private Clock clockDev;
	private IMemory instrMemory;
	private Bus rst;
	private ControlUnit control;
	
	@Override
	public void build() {
		
		displayable = new TreeMap<String, Device>();
		
		//Buses:
		
		//Clock and reset
		Bus clk = new Bus(1);
		rst = new Bus(1);
		
		//Instruction pointer resolution
		Bus instrPtr = new Bus(32);
		Bus jmpAddr = new Bus(32);
		Bus shftToJumpMux = new Bus(28);
		Bus concatToMux = new Bus(32);
		Bus andToOr = new Bus(1);
		Bus xorToAnd = new Bus(1);
		
		//Memory buses
		Bus addr = new Bus(32);
		Bus dataMem = new Bus(32);
		Bus memDataToMux = new Bus(32);
		
		//Instruction buses
		Bus instr = new Bus(32);
		Bus sExtToMuxAlu = new Bus(32);
		Bus shftToMuxAlu = new Bus(32);
		
		//Integer register file buses
		Bus selW = new Bus(5);
		Bus wrtData = new Bus(32);
		Bus dataARegIn = new Bus(32);
		Bus dataBRegIn = new Bus(32);
		Bus dataARegOut = new Bus(32);
		Bus dataBRegOut = new Bus(32);
		
		//ALU buses
		Bus aluDataA = new Bus(32);
		Bus aluDataB = new Bus(32);
		Bus aluOut = new Bus(32);
		Bus aluOutHigh = new Bus(32);
		Bus aluOutHighToMux = new Bus(32);
		Bus aluFlags = new Bus(3);
		Bus wbBus = new Bus(32);
		
		//  Control buses  //
		Bus pcWriteCond = new Bus(1);
		Bus pcWrite = new Bus(1);
		Bus iOrD = new Bus(1);
		Bus memRead = new Bus(1);
		Bus memWrite = new Bus(1);
		Bus memToReg = new Bus(2);
		Bus irWrite = new Bus(1);
		Bus pcSource = new Bus(2);
		Bus aluOp = new Bus(2);
		Bus aluSrcB = new Bus(2);
		Bus aluSrcA = new Bus(1);
		Bus regWrite = new Bus(1);
		Bus regDst = new Bus(1);
		Bus solPCWrite = new Bus(1);
		Bus aluControl = new Bus(4);
		
		Bus target = new Bus(1);
		Bus qExe = new Bus(1);
		/////////////////////
		
		//Quantum buses
		Bus mResult = new Bus(32);
		
		//Devices:
		
		//Memory
		instrMemory = new AsyncMemory(dataBRegOut, dataMem, addr, memRead, memWrite, clk, 2048);
		
		//Clock
		clockDev = new Clock(clk);
		
		//Program counter
		displayable.put("Program counter", new SynchronousRegister(jmpAddr, instrPtr, solPCWrite, rst, clk));
		
		//Instruction register
		displayable.put("Instruction register", new SynchronousRegister(dataMem, instr, irWrite, rst, clk));
		
		//Memory data register
		displayable.put("Memory data register", new SynchronousRegister(dataMem, memDataToMux, new Bus(1,1), rst, clk));
		
		//A
		new SynchronousRegister(dataARegIn, dataARegOut, new Bus(1,1), rst, clk);
		
		//B
		new SynchronousRegister(dataBRegIn, dataBRegOut, new Bus(1,1), rst, clk);
		
		//ALU out
		new SynchronousRegister(aluOut, wbBus, new Bus(1,1), rst, clk);
		
		//ALU out high
		new SynchronousRegister(aluOutHigh, aluOutHighToMux, new Bus(1,1), rst, clk);
		
		//Integer register file
		displayable.put("Register file", new RegisterFile(instr.getRange(21, 26), instr.getRange(16, 21), dataARegIn, dataBRegIn, selW, regWrite, wrtData, rst, clk));
		
		//ALU
		new IntALU(aluDataA, aluDataB, aluControl, aluOut, aluOutHigh, aluFlags);
		
		//ALUControl
		new ALUControl(instr.getRange(0, 6), aluOp, aluControl);
		
		//Quantum unit
		QubitArray32 qa32 = new QubitArray32();
		
		new QubitTargetControl(instr.getRange(16, 21), instr.getRange(11, 16), target, clk, qa32);
		
		displayable.put("Quantum array state", new QuantumControl(instr.getRange(0, 6), instr.getRange(21, 26), dataARegOut, mResult, qExe, clk, rst, qa32));
		
		//Multiplexers
		//1
		new Multiplexer(iOrD, new Bus[]{instrPtr, wbBus}, addr);
		
		//2
		new Multiplexer(regDst, new Bus[]{instr.getRange(16, 21), instr.getRange(11, 16)}, selW);
		
		//3
		new Multiplexer(memToReg, new Bus[]{wbBus,memDataToMux, aluOutHighToMux, mResult}, wrtData);
		
		//4
		new Multiplexer(aluSrcA, new Bus[]{instrPtr, dataARegOut}, aluDataA);
		
		//5
		new Multiplexer(aluSrcB, new Bus[]{dataBRegOut,new Bus(4,32), sExtToMuxAlu, shftToMuxAlu}, aluDataB);
		
		//6
		new Multiplexer(pcSource, new Bus[]{aluOut, wbBus, concatToMux}, jmpAddr);
		
		//Sign extender
		new SignExt(instr.getRange(0, 16), sExtToMuxAlu);
		
		//Left shifters
		//1
		new ShiftLeft(sExtToMuxAlu, shftToMuxAlu, 2);
		
		//2
		new ShiftLeft(instr.getRange(0, 26), shftToJumpMux, 0);
		
		//Concatenator
		new Concat(new Bus[]{instrPtr.getRange(28, 32), shftToJumpMux}, concatToMux);
		
		//B Or gate
		new Logic(andToOr, pcWrite, solPCWrite, Logic.OR);
		
		//B And gate
		new Logic(pcWriteCond, xorToAnd, andToOr, Logic.AND);
				
		//B Xor gate
		new Logic(instr.getRange(26, 27), aluFlags.getRange(2, 3), xorToAnd, Logic.XOR);
		
		//Control unit
		control = new QuantumMIPSControlUnit(pcWriteCond, pcWrite, iOrD, memRead, memWrite, memToReg, irWrite, pcSource, aluOp, aluSrcB, aluSrcA, regWrite, regDst, solPCWrite, aluControl, target, qExe, aluFlags.getRange(0, 1), instr.getRange(26, 32), clk, rst);
		displayable.put("Control unit", (Device)control);
		
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

}
