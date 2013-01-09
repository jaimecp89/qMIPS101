package qmips.devices.quantum;

import qmips.devices.Device;
import qmips.others.Behavior;
import qmips.others.Bus;

/**
 * 
 * Dispositivo que controla sobre que qubits del array se opera.
 * Si se indica el mismo qubit para control y objetivo se supone
 * que la operacion es no controlada.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class QubitTargetControl extends Device{

	private Bus qubit, control, target, clk;
	private QubitArray32 qarray;
	
	public QubitTargetControl(Bus qubit, Bus control, Bus target, Bus clk, QubitArray32 qarray){
		this.qubit = qubit;
		this.control = control;
		this.target = target;
		this.clk = clk;
		this.qarray = qarray;
		defineBehavior();
	}
	
	@Override
	protected void defineBehavior() {
		behavior(new Bus[]{clk}, new Behavior(){
			@Override
			public void task() {
				if(clk.read().get(0) && target.read().get(0)){
					int iq = qubit.read().toInteger();
					int ic = control.read().toInteger();
					boolean controlled = iq != ic;
					qarray.setSelectedTarget(iq);
					qarray.setSelectedControl(controlled? ic : -1);
				}
			}
		});
	}
	
}
