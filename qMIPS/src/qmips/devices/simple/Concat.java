package qmips.devices.simple;

import qmips.devices.Device;
import qmips.others.Behavior;
import qmips.others.Bus;
import qmips.others.LogicVector;

/**
 * 
 * Dispositivo que concatena una serie de buses en uno solo.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class Concat extends Device{

	private Bus[] buses;
	private Bus output;
	
	public Concat(Bus[] buses, Bus output){
		this.buses = buses;
		this.output = output;
		defineBehavior();
	}
	
	@Override
	protected void defineBehavior() {
		behavior(buses, new Behavior(){

			@Override
			public void task() {
				int size = 0;
				int res = 0;
				for(int i = buses.length - 1; i >= 0; i--){
					res += buses[i].read().toInteger() << size;
					size += buses[i].size();
				}
				output.write(new LogicVector(res, size));
			}
			
		});
	}

}
