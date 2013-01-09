package qmips.devices.registerFile;

import javax.swing.JPanel;

import qmips.devices.Device;
import qmips.others.Behavior;
import qmips.others.Bus;
import qmips.others.LogicVector;

/**
 * 
 * Fichero de 32 registros de 32 bits.
 * Las lecturas son asincronas, el dispositivo responde
 * inmediatamente y se pueden leer dos datos a la vez.
 * Las escrituras se realizan en el flanco de subida.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class RegisterFile extends Device {

	private Bus selA, selB, outputA, outputB, selW, wr, input, rst, clk;
	private LogicVector[] contents;
	private Display disp;

	public RegisterFile(Bus selA, Bus selB, Bus outputA, Bus outputB, Bus selW,
			Bus wr, Bus input, Bus rst, Bus clk) {
		this.clk = clk;
		this.rst = rst;
		this.input = input;
		this.outputA = outputA;
		this.outputB = outputB;
		this.selA = selA;
		this.selB = selB;
		this.selW = selW;
		this.wr = wr;
		this.contents = new LogicVector[32];
		for (int i = 0; i < 32; i++) {
			contents[i] = new LogicVector(32);
		}
		disp = new RegisterFileDisplay();
		defineBehavior();
	}

	public void load(int index, LogicVector value) {
		this.contents[index] = value;
	}

	@Override
	protected void defineBehavior() {

		behavior(new Bus[] { clk }, new Behavior() {

			@Override
			public void task() {
				if (clk.read().get(0) && wr.read().get(0)) {
					disp.write(selW.read().toInteger(), input.read());
					contents[selW.read().toInteger()] = input.read();
				}
			}

		});

		behavior(new Bus[] {selA, selB}, new Behavior() {

			@Override
			public void task() {
				int iselA = selA.read().toInteger();
				int iselB = selB.read().toInteger();
				disp.setSelectedA(iselA);
				disp.setSelectedB(iselB);
				if (iselA == 0) {
					outputA.write(new LogicVector(32));
				} else {
					outputA.write(contents[iselA]);
				}
				if (iselB == 0) {
					outputB.write(new LogicVector(32));
				} else {
					outputB.write(contents[iselB]);
				}
			}

		});
		
		behavior(new Bus[]{rst}, new Behavior(){

			@Override
			public void task() {
				if(rst.read().get(0)){
					contents = new LogicVector[32];
					for (int i = 0; i < 32; i++) {
						contents[i] = new LogicVector(32);
						disp.write(i, new LogicVector(32));
					}
				}
			}
			
		});

	}
	
	@Override
	public JPanel display(){
		return (JPanel) disp;
	}
	
	public interface Display{
		
		void setSelectedA(int iselA);
		void setSelectedB(int iselB);
		
		void write(int reg, LogicVector value);
		void reset();
		
	}

}
