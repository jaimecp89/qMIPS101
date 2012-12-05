package presentation.swing;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import domain.engine.circuit.Circuit;
import domain.engine.circuit.Stage;

public class CircuitView extends JInternalFrame implements PropertyChangeListener{

	private static final long serialVersionUID = 6979510521078122156L;
	private JCircuitPanel jContentPane = null;
	private String name;
	private Circuit circuit;
	private MainViewController controller;
	
	public CircuitView(MainViewController controller, String name, int inputQubits){
		super(name);
		this.controller = controller;
		circuit = new Circuit();
		circuit.addStage(new Stage(inputQubits),0);
		this.name = name;
		initialize();
		this.addPropertyChangeListener(this);
	}
	
	public CircuitView(MainViewController controller, Circuit circuit){
		super(circuit.getName());
		this.controller = controller;
		this.circuit = circuit;
		this.name = circuit.getName();
		initialize();
		this.addPropertyChangeListener(this);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle(name);
		this.setMaximizable(true);
		this.setClosable(true);
		this.setPreferredSize(new Dimension(500, 200));
		this.setResizable(true);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JCircuitPanel(controller, circuit);
			jContentPane.setLayout(new FlowLayout());
		}
		return jContentPane;
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		String property = arg0.getPropertyName();
		if(property.equals("selected")){
			controller.circuitViewSelected(this);
		}else if(property.equals("closed")){
			controller.circuitViewClosed(this);
		}
	}
	
	public Circuit getCircuit(){
		return this.circuit;
	}
	
	public int getSelectedStage(){
		return jContentPane.getSelectedStage();
	}
	
	public int getSelectedQubit(){
		return jContentPane.getSelectedQubit();
	}
	
	public void incSelectedQubit(){
		jContentPane.incSelectedQubit();
	}
	
	public void incSelectedStage(){
		jContentPane.incSelectedStage();
	}
	

}
