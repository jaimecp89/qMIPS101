package qmips.devices.quantum;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JPanel;

import qmips.devices.Device;
import qmips.others.Behavior;
import qmips.others.Bus;
import qmips.others.LogicVector;
import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;
import domain.engine.quantum.QuantumState;
import domain.engine.quantum.gates.HadamardGate;
import domain.engine.quantum.gates.Measure;
import domain.engine.quantum.gates.PauliXGate;
import domain.engine.quantum.gates.PauliYGate;
import domain.engine.quantum.gates.PauliZGate;
import domain.engine.quantum.gates.PhaseShiftGate;

/**
 * 
 * Este dispositivo opera sobre el array de qubits.
 * Las operaciones que realiza se pueden ver en el codigo.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class QuantumControl extends Device{

	private Bus param, funct, regValue, qexe, clk, rst, meas;
	private QubitArray32 qarray;
	private Display disp;
	private SortedSet<Integer> controlQubits;
	private int offset;
	
	public QuantumControl(Bus funct, Bus param, Bus regValue, Bus meas, Bus qexe, Bus clk, Bus rst, QubitArray32 qarray){
		this.param = param;
		this.funct = funct;
		this.regValue = regValue;
		this.meas = meas;
		this.qexe = qexe;
		this.clk = clk;
		this.rst = rst;
		this.qarray = qarray;
		this.controlQubits = new TreeSet<Integer>();
		this.offset = 0;
		this.disp = new QuantumControlDisplay();
		disp.updateText(qarray.getState().toString(), Integer.toString(offset), controlQubits.toString());
		defineBehavior();
	}
	
	@Override
	protected void defineBehavior() {
		
		behavior(new Bus[]{clk}, new Behavior(){

			@Override
			public void task() {
				if(clk.read().get(0)){
					if(qexe.read().get(0)){
						int ifunct = funct.read().toInteger();
						QuantumState state = qarray.getState();
						int[] control;
						Vector<Integer> v = new Vector<Integer>();
						for(Integer i : controlQubits){
							if(i != ((qarray.getSelectedTarget() + offset) % 32))
								v.add(i);
						}
						if(qarray.getSelectedControl() != -1)
							v.add((qarray.getSelectedControl() + offset) % 32);
						control = new int[v.size()];
						for(int i = 0; i < control.length; i++){
							control[i] = v.get(i);
						}
						switch(ifunct){
						case 0x00: //QHAD
							HadamardGate hg = new HadamardGate();
							qarray.setState(hg.operate(state, (qarray.getSelectedTarget() + offset) % 32, control));
							break;
						case 0x01: //QX
							PauliXGate px = new PauliXGate();
							qarray.setState(px.operate(state, (qarray.getSelectedTarget() + offset) % 32, control));
							break;
						case 0x02: //QY
							PauliYGate py = new PauliYGate();
							qarray.setState(py.operate(state, (qarray.getSelectedTarget() + offset) % 32, control));
							break;
						case 0x03: //QZ
							PauliZGate pz = new PauliZGate();
							qarray.setState(pz.operate(state, (qarray.getSelectedTarget() + offset) % 32, control));
							break;
						case 0x10: //QPHS
							PhaseShiftGate ph = new PhaseShiftGate("(2*pi)/(2^" + regValue.read().toInteger() + ")");
							qarray.setState(ph.operate(state, (qarray.getSelectedTarget() + offset) % 32, control));
							break;
						case 0x11: //QNPH
							PhaseShiftGate np = new PhaseShiftGate("(-2*pi)/(2^" + regValue.read().toInteger() + ")");
							qarray.setState(np.operate(state, (qarray.getSelectedTarget() + offset) % 32, control));
							break;
						case 0x1A: //QMEA
							Measure m = new Measure();
							qarray.setState(m.operate(state, qarray.getSelectedTarget()));
							meas.write(m.getMeasurementResult() << param.read().toInteger(), 32);
							break;
						case 0x1B: //QRST
							QuantumState qs = new QuantumState();
							byte[] val = new byte[32];
							LogicVector lv = regValue.read();
							for(int i = 0; i < 32; i++)
								val[i] = lv.get(i) ? (byte)1 : (byte)0;
							qs.add(new Complex(1.0,0.0), new ClassicState(val));
							qarray.setState(qs);
							break;
						case 0x1C: //QCNT
							int index = ((0X0000001F & regValue.read().toInteger()) + offset) % 32;
							if(controlQubits.contains(index)){
								controlQubits.remove(index);
							}else{
								controlQubits.add(index);
							}						
							break;
						case 0x1D: //QOFF
							offset = 0X0000001F & regValue.read().toInteger();
							break;
						}
						disp.updateText(qarray.getState().toString(), Integer.toString(offset), controlQubits.toString());
					}
				}
			}
			
		});
		
		behavior(new Bus[]{ rst }, new Behavior() {
			
			@Override
			public void task() {
				QuantumState qs = new QuantumState();
				qs.add(new Complex(1.0,0.0), new ClassicState(new byte[32]));
				qarray.setState(qs);
				controlQubits.clear();
				offset = 0;
				disp.updateText(qarray.getState().toString(), Integer.toString(offset), controlQubits.toString());
			}
			
		});
	}

	public JPanel display(){
		return (JPanel)disp;
	}
	
	interface Display{
		void updateText(String s, String o, String c);
	}
}
