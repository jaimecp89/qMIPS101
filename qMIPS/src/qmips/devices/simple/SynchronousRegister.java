package qmips.devices.simple;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import qmips.devices.Device;
import qmips.others.Behavior;
import qmips.others.Bus;
import qmips.others.LogicVector;

/**
 * 
 * Registro sincrono simple.
 * Se carga un valor con 'en' activo en el flanco de subida.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class SynchronousRegister extends Device {

	Bus input, output, en, clk, rst;
	Display disp;

	public SynchronousRegister(Bus input, Bus output, Bus en, Bus rst, Bus clk) {
		this.input = input;
		this.output = output;
		this.en = en;
		this.rst = rst;
		this.clk = clk;
		disp =  new SynchronousRegisterDisplay();
		defineBehavior();
	}

	
	@Override
	protected void defineBehavior() {
		behavior(new Bus[] { clk }, new Behavior() {

			@Override
			public void task() {
				 if (clk.read().get(0)) {
					if (en.read().get(0)) {
						disp.setContent(input.read());
						output.write(input.read());
					}
				}
			}
			
		});
		
		behavior(new Bus[]{ rst }, new Behavior() {
			
			@Override
			public void task() {
				System.out.println("Reset en registro: " + rst.read());
				if (rst.read().get(0)) {
					output.write(new LogicVector(output.size()));
					disp.setContent(new LogicVector(output.size()));
				}
			}
			
		});

	}
	
	@Override
	public JPanel display(){
		return (JPanel) disp;
	}
	
	public interface Display{
		void setContent(LogicVector v);
	}
	
	/**
	 * 
	 * Interfaz grafica simple para los registros.
	 * 
	 * @author Jaime Coello de Portugal
	 *
	 */
	class SynchronousRegisterDisplay extends JPanel implements Display{

		private static final long serialVersionUID = -5586274312021370406L;
		JLabel content;
		
		public SynchronousRegisterDisplay(){
			this.setSize(360, 30);
			content = new JLabel(new LogicVector(32).toString());
			content.setBackground(Color.WHITE);
			add(content);
		}
		
		@Override
		public void setContent(LogicVector v) {
			content.setText(v.toString());
		}
		
	}

}