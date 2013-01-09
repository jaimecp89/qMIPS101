package qmips.devices.quantum;

import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;
import domain.engine.quantum.QuantumState;

/**
 * 
 * Contiene un array de 32 qubits sobre el que se realizaran
 * las operaciones cuanticas.
 * El tipo QubitTargetControl define sobre que qubits concretos
 * se actuara y QuantumControl realiza las operaciones correspondientes
 * sobre este array.
 * 
 * El simulador de estados cuanticos se vuelve mas lento con el tamaño
 * de la superposicion del array. Al movernos en el orden de 20 qubits
 * en superposicion el sistema puede colgarse por falta de memoria.
 * 
 * La clase QuantumState esta importada del proyecto Qubit101.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class QubitArray32{

	private QuantumState state;
	private int selectedTarget = -1;
	private int selectedControl = - 1;
	
	public QubitArray32(){
		state = new QuantumState();
		state.add(new Complex(1.0,0.0), new ClassicState(new byte[32]));
	}

	public int getSelectedTarget() {
		return selectedTarget;
	}

	public void setSelectedTarget(int selectedTarget) {
		this.selectedTarget = selectedTarget;
	}

	public int getSelectedControl() {
		return selectedControl;
	}

	public void setSelectedControl(int selectedControl) {
		this.selectedControl = selectedControl;
	}

	public QuantumState getState(){
		return state;
	}
	
	public void setState(QuantumState state){
		this.state = state;
	}
}
