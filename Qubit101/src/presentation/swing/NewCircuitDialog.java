package presentation.swing;

import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class NewCircuitDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel jLabel = null;
	private JTextField jQubitsField = null;
	private JLabel jLabel1 = null;
	private JTextField jNameField = null;
	private JButton jAcceptButton = null;
	private JButton jCancelButton = null;
	protected boolean accepted = false;

	/**
	 * @param owner
	 */
	public NewCircuitDialog(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(367, 163);
		this.setModal(true);
		this.setTitle("New circuit");
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(15, 45, 97, 26));
			jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel1.setText("Input qubits");
			jLabel = new JLabel();
			jLabel.setText("Name");
			jLabel.setBounds(new Rectangle(15, 15, 70, 23));
			jLabel.setHorizontalTextPosition(SwingConstants.CENTER);
			jLabel.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel.setName("jLabel");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(jLabel, null);
			jContentPane.add(getJQubitsField(), null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(getJNameField(), null);
			jContentPane.add(getJAcceptButton(), null);
			jContentPane.add(getJCancelButton(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jQubitsField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJQubitsField() {
		if (jQubitsField == null) {
			jQubitsField = new JTextField();
			jQubitsField.setHorizontalAlignment(JTextField.LEADING);
			jQubitsField.setBounds(new Rectangle(126, 48, 36, 22));
			jQubitsField.setName("jQubitsField");
		}
		return jQubitsField;
	}

	/**
	 * This method initializes jNameField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJNameField() {
		if (jNameField == null) {
			jNameField = new JTextField();
			jNameField.setBounds(new Rectangle(127, 14, 213, 22));
		}
		return jNameField;
	}
	
	public boolean isAccepted(){
		return accepted;
	}
	
	public Entry<String, Integer> showDialog(){
		accepted = false;
		this.setVisible(true);
		
		return new Entry<String, Integer>(){

			@Override
			public String getKey() {
				return jNameField.getText();
			}

			@Override
			public Integer getValue() {
				return Integer.parseInt(jQubitsField.getText());
			}

			@Override
			public Integer setValue(Integer value) {return -1;}
		};
		
	}

	/**
	 * This method initializes jAcceptButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJAcceptButton() {
		if (jAcceptButton == null) {
			jAcceptButton = new JButton();
			jAcceptButton.setBounds(new Rectangle(246, 87, 90, 26));
			jAcceptButton.setText("Accept");
			jAcceptButton.addActionListener(new NewCircuirActionListener(true));
		}
		return jAcceptButton;
	}

	/**
	 * This method initializes jCancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJCancelButton() {
		if (jCancelButton == null) {
			jCancelButton = new JButton();
			jCancelButton.setBounds(new Rectangle(137, 87, 91, 26));
			jCancelButton.setText("Cancel");
			jCancelButton.addActionListener(new NewCircuirActionListener(false));
		}
		return jCancelButton;
	}
	
	class NewCircuirActionListener implements ActionListener{
		private boolean acceptButton = false;
		
		public NewCircuirActionListener(boolean acceptButton){
			this.acceptButton = acceptButton;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(acceptButton)
				accepted = true;
			setVisible(false);
		}
		
	}

}  //  @jve:decl-index=0:visual-constraint="184,37"
