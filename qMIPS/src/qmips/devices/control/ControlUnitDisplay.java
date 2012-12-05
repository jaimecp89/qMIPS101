package qmips.devices.control;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Font;

/**
 * 
 * Interfaz grafica para unidades de control generica.
 * Tan solo muestra el estado actual en siglas y una pequeña
 * descrpcion.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class ControlUnitDisplay extends JPanel implements IControlUnitDisplay{

	private static final long serialVersionUID = -7223332015889309434L;
	private JLabel lblState;
	private JLabel lblInstructionFetch;
	public ControlUnitDisplay() {
		setLayout(new BorderLayout(0, 0));
		
		lblState = new JLabel("IF");
		lblState.setFont(new Font("Tahoma", Font.BOLD, 27));
		lblState.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblState);
		
		lblInstructionFetch = new JLabel("Instruction fetch");
		lblInstructionFetch.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblInstructionFetch, BorderLayout.SOUTH);
		
		setSize(180,100);
	}
	
	public JLabel getLblState() {
		return lblState;
	}
	public JLabel getLblDescription() {
		return lblInstructionFetch;
	}

	@Override
	public void setState(String state) {
		getLblState().setText(state);
	}

	@Override
	public void setDescription(String description) {
		getLblDescription().setText(description);
	}

	@Override
	public void setState(String state, String description) {
		getLblState().setText(state);
		getLblDescription().setText(description);
	}
}
