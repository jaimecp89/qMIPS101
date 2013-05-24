package qmips.devices.quantum;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

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
		
		setSize(350, 395);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scroll = new JScrollPane();
		panel.add(scroll);
		
		stateText = new JTextArea();
		scroll.setViewportView(stateText);
		stateText.setEditable(false);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		JLabel lblOffset = new JLabel("Offset:");
		lblOffset.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblOffset.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblOffset);
		
		JLabel label = new JLabel("");
		panel_1.add(label);
		
		JLabel label_2 = new JLabel("");
		panel_1.add(label_2);
		
		JLabel label_3 = new JLabel("");
		panel_1.add(label_3);
		
		offsetLbl = new JLabel("0");
		offsetLbl.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_1.add(offsetLbl);
		
		JLabel lblControlQubits = new JLabel("Control Qubits:");
		lblControlQubits.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_1.add(lblControlQubits);
		
		controlLbl = new JLabel("[]");
		panel_1.add(controlLbl);
		setVisible(true);
	}

	private static final long serialVersionUID = -5305181440555107289L;
	private JTextArea stateText;
	private JLabel offsetLbl;
	private JLabel controlLbl;

	@Override
	public void updateText(String s, String o, String c) {
		getStateText().setText(s);
		getOffsetLbl().setText(o);
		getControlLbl().setText(c);
		repaint();
	}

	public JTextArea getStateText() {
		return stateText;
	}
	protected JLabel getOffsetLbl() {
		return offsetLbl;
	}
	protected JLabel getControlLbl() {
		return controlLbl;
	}
}
