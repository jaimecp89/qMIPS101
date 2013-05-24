package qmips.presentation.builders;

import java.util.Map;

import qmips.devices.Device;
import qmips.devices.clock.Clock;
import qmips.devices.control.ControlUnit;
import qmips.devices.memory.IMemory;
import qmips.devices.simple.SynchronousRegister;
import qmips.others.Bus;

public interface Builder {

	public void build();
	
	public Map<String, Device> getDisplayableDevices();
	
	public Clock getClock();
	
	public IMemory getInstrMemory();
	
	public Bus getResetBus();
	
	public ControlUnit getControlUnit();
	
	public SynchronousRegister programCounter();
	
}
