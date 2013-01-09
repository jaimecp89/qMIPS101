package qmips.devices.quantum;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.BorderLayout;

/**
 * 
 * Interfaz grafica simple para el sistema de informacion
 * cuantica.
 * Muestra simplemente el estado actual del array de qubits
 * en formato texto.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class QuantumControlDisplay extends JPanel implements QuantumControl.Display{
	public QuantumControlDisplay() {
		setLayout(new BorderLayout(0, 0));
		
		stateText = new JTextArea();
		stateText.setEditable(false);
		add(stateText);
		
		setSize(350, 395);
		setVisible(true);
	}

	private static final long serialVersionUID = -5305181440555107289L;
	private JTextArea stateText;

	@Override
	public void updateText(String s) {
		getStateText().setText(s);
		repaint();
	}

	public JTextArea getStateText() {
		return stateText;
	}
}
