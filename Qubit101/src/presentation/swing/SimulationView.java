package presentation.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;

import domain.engine.circuit.Circuit;

public class SimulationView extends JDialog {
	
	public static final int INPUT_BIN = 0, INPUT_DEC = 1, INPUT_HEX = 2;

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JSplitPane jSplitPane = null;
	private JPanel jPanel = null;
	private JTabbedPane jTabbedPane = null;
	private JToolBar jToolBar = null;
	public JCircuitPanel jCircuitPanel = null;
	private JButton jNextStageButton = null;
	private JButton jPreviousStageButton = null;
	private JTextPane jStatePane = null;
	private JTextArea jMeasurementsPane = null;
	private JButton jQuickSimButton = null;
	private JButton jSimButton = null;
	private Circuit circuit;
	private Vector<Component> simulationButtons = new Vector<Component>();
	private JToolBar jInputBar = null;
	private JTextField jInputField = null;
	private JLabel jLabel = null;
	private JRadioButton jBinaryButton = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JRadioButton jDecimalButton = null;
	private JLabel jLabel3 = null;
	private JRadioButton jHexButton = null;
	private SimulationViewController controller;
	
	private int inputFormat = INPUT_BIN;
	
	/**
	 * @param owner
	 */
	public SimulationView(SimulationViewController controller, Frame owner, Circuit circuit) {
		super(owner);
		this.controller = controller;
		this.circuit = circuit;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(1000, 600);
		this.setModal(true);
		this.setContentPane(getJContentPane());
		ButtonGroup radio = new ButtonGroup();
		radio.add(jBinaryButton);
		radio.add(jDecimalButton);
		radio.add(jHexButton);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJSplitPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPane.setDividerSize(5);
			jSplitPane.setDividerLocation(this.getHeight()/2);
			jSplitPane.setBottomComponent(getJTabbedPane());
			jSplitPane.setTopComponent(getJPanel());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add(getJToolBar(), BorderLayout.WEST);
			jPanel.add(getJCircuitPanel(), BorderLayout.CENTER);
			jPanel.add(getJInputBar(), BorderLayout.SOUTH);
		}
		return jPanel;
	}

	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	public JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setTabPlacement(JTabbedPane.TOP);
			jTabbedPane.setFont(new Font("Dialog", Font.BOLD, 12));
			jTabbedPane.addTab("Measurements", null, new JScrollPane(getJMeasurementsPane()), null);
			jTabbedPane.setEnabledAt(0, false);
			jTabbedPane.addTab("State", null, new JScrollPane(getJStatePane()), null);
			jTabbedPane.setEnabledAt(1, false);
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jToolBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getJToolBar() {
		if (jToolBar == null) {
			jToolBar = new JToolBar();
			jToolBar.setPreferredSize(new Dimension(30, 4));
			jToolBar.setFloatable(false);
			jToolBar.setOrientation(JToolBar.VERTICAL);
			jToolBar.add(getJNextStageButton());
			jToolBar.add(getJPreviousStageButton());
			jToolBar.addSeparator();
			jToolBar.add(getJQuickSimButton());
			jToolBar.add(getJSimButton());
		}
		return jToolBar;
	}

	/**
	 * This method initializes jCircuitPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public JCircuitPanel getJCircuitPanel() {
		if (jCircuitPanel == null) {
			jCircuitPanel = new JCircuitPanel(circuit);
			jCircuitPanel.setEditable(false);
			jCircuitPanel.setLayout(new GridBagLayout());
			jCircuitPanel.setBackground(Color.white);
		}
		return jCircuitPanel;
	}

	/**
	 * This method initializes jNextStageButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJNextStageButton() {
		if (jNextStageButton == null) {
			jNextStageButton = new JButton();
			jNextStageButton.setIcon(new ImageIcon(getClass().getResource("/icons/57.gif")));
			jNextStageButton.setMnemonic(KeyEvent.VK_RIGHT);
			jNextStageButton.setEnabled(false);
			jNextStageButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.nextStage();
				}
			});
			simulationButtons.add(jNextStageButton);
		}
		return jNextStageButton;
	}

	/**
	 * This method initializes jPreviousStageButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJPreviousStageButton() {
		if (jPreviousStageButton == null) {
			jPreviousStageButton = new JButton();
			jPreviousStageButton.setIcon(new ImageIcon(getClass().getResource("/icons/56.gif")));
			jPreviousStageButton.setMnemonic(KeyEvent.VK_LEFT);
			jPreviousStageButton.setEnabled(false);
			jPreviousStageButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.previousStage(); 
				}
			});
			simulationButtons.add(jPreviousStageButton);
		}
		return jPreviousStageButton;
	}

	/**
	 * This method initializes jStatePane	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	public JTextPane getJStatePane() {
		if (jStatePane == null) {
			jStatePane = new JTextPane();
			jStatePane.setEditable(false);
		}
		return jStatePane;
	}

	/**
	 * This method initializes jMeasurementsPane	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	public JTextArea getJMeasurementsPane() {
		if (jMeasurementsPane == null) {
			jMeasurementsPane = new JTextArea();
			jMeasurementsPane.setAutoscrolls(true);
			jMeasurementsPane.setEditable(false);
		}
		return jMeasurementsPane;
	}

	public Vector<Component> getSimulationButtons() {
		return simulationButtons;
	}

	/**
	 * This method initializes jQuickSimButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJQuickSimButton() {
		if (jQuickSimButton == null) {
			jQuickSimButton = new JButton();
			jQuickSimButton.setIcon(new ImageIcon(getClass().getResource("/icons/quick-play.gif")));
			jQuickSimButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.quickSimulation();
				}
			});
		}
		return jQuickSimButton;
	}

	/**
	 * This method initializes jSimButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJSimButton() {
		if (jSimButton == null) {
			jSimButton = new JButton();
			jSimButton.setIcon(new ImageIcon(getClass().getResource("/icons/play.png")));
			jSimButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.completeSimulation();
				}
			});
		}
		return jSimButton;
	}

	/**
	 * This method initializes jInputBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getJInputBar() {
		if (jInputBar == null) {
			jLabel3 = new JLabel();
			jLabel3.setText("Hexadecimal");
			jLabel2 = new JLabel();
			jLabel2.setText("Decimal");
			jLabel1 = new JLabel();
			jLabel1.setText("Binary");
			jLabel = new JLabel();
			jLabel.setText("Input");
			jInputBar = new JToolBar();
			jInputBar.setFloatable(false);
			jInputBar.add(jLabel);
			jInputBar.addSeparator();
			jInputBar.add(getJInputField());
			jInputBar.addSeparator();
			jInputBar.add(jLabel1);
			jInputBar.add(getJBinaryButton());
			jInputBar.addSeparator();
			jInputBar.add(jLabel2);
			jInputBar.add(getJDecimalButton());
			jInputBar.addSeparator();
			jInputBar.add(jLabel3);
			jInputBar.add(getJHexButton());
			jInputBar.addSeparator();
		}
		return jInputBar;
	}

	/**
	 * This method initializes jInputField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getJInputField() {
		if (jInputField == null) {
			jInputField = new JTextField();
		}
		return jInputField;
	}

	/**
	 * This method initializes jBinaryButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJBinaryButton() {
		if (jBinaryButton == null) {
			jBinaryButton = new JRadioButton();
			jBinaryButton.setSelected(true);
			jBinaryButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setInputFormat(INPUT_BIN);
				}
			});
		}
		return jBinaryButton;
	}

	/**
	 * This method initializes jDecimalButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJDecimalButton() {
		if (jDecimalButton == null) {
			jDecimalButton = new JRadioButton();
			jDecimalButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setInputFormat(INPUT_DEC);
				}
			});
		}
		return jDecimalButton;
	}

	/**
	 * This method initializes jHexButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJHexButton() {
		if (jHexButton == null) {
			jHexButton = new JRadioButton();
			jHexButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setInputFormat(INPUT_HEX);
				}
			});
		}
		return jHexButton;
	}

	public void setInputFormat(int inputFormat) {
		this.inputFormat = inputFormat;
	}

	public int getInputFormat() {
		return inputFormat;
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"
