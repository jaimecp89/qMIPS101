package presentation.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;

import domain.engine.circuit.Stage;
import java.awt.Toolkit;

public class MainView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JMenuBar jMainMenuBar = null;
	private JMenu jFileMenu = null;
	private JMenu jAboutMenu = null;
	private JMenuItem jExitItem = null;
	private JMenuItem jNewItem = null;
	private JToolBar jToolBar = null;
	private JDesktopPane jCircuitsPane = null;
	private JButton jNewButton = null;
	private JButton jSaveButton = null;
	private JPanel jPanel = null;
	private JToolBar jToolBar2 = null;
	private JButton jSaveAllButton = null;
	private JButton jOpenButton = null;
	private JButton jHadamardButton = null;
	private JButton jXButton = null;
	private JButton jYButton = null;
	private JButton jZButton = null;
	private JButton jPhaseButton = null;
	private JComboBox jLoadedCircuitsBox = null;
	private JButton jLoadCircuitButton = null;
	private JButton jMeasureButton = null;
	private JButton jAddQubitButton = null;
	private JButton jTraceButton = null;
	private JLabel jLabel = null;
	private JCheckBox jControlledBox = null;
	private JButton jControlButton = null;
	private Set<Component> circuitEditButtons;
	private JButton jAddStageButton = null;
	private JButton jRemoveStageButton = null;
	private JButton jAddWireButton = null;
	private JButton jRemoveWireButton = null;
	private JButton jRemoveGateButton = null;
	private JButton jSimulateButton = null;
	private MainViewController controller;
	private JButton jAddCircuitGateButton = null;
	private MainView main = this;
	
	/**
	 * This is the default constructor
	 */
	public MainView(MainViewController controller) {
		super();
		this.controller = controller;
		circuitEditButtons = new HashSet<Component>();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		controller.setMainFrame(this);
		this.setSize(930, 603);
		this.setLocationRelativeTo(null);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/Qubit101Icon.png")));
		this.setJMenuBar(getJMainMenuBar());
		this.setContentPane(getJContentPane());
		this.setTitle("Qubit101");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(0);
			borderLayout.setVgap(0);
			jContentPane = new JPanel();
			jContentPane.setLayout(borderLayout);
			jContentPane.add(getJCircuitsPane(), BorderLayout.CENTER);
			jContentPane.add(getJPanel(), BorderLayout.NORTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jMainMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getJMainMenuBar() {
		if (jMainMenuBar == null) {
			jMainMenuBar = new JMenuBar();
			jMainMenuBar.add(getJFileMenu());
			jMainMenuBar.add(getJAboutMenu());
		}
		return jMainMenuBar;
	}

	/**
	 * This method initializes jFileMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJFileMenu() {
		if (jFileMenu == null) {
			jFileMenu = new JMenu();
			jFileMenu.setText("File");
			jFileMenu.add(getJNewItem());
			jFileMenu.add(getJExitItem());
		}
		return jFileMenu;
	}

	/**
	 * This method initializes jAboutMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJAboutMenu() {
		if (jAboutMenu == null) {
			jAboutMenu = new JMenu();
			jAboutMenu.setText("About");
		}
		return jAboutMenu;
	}

	/**
	 * This method initializes jExitItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJExitItem() {
		if (jExitItem == null) {
			jExitItem = new JMenuItem();
			jExitItem.setText("Exit");
		}
		return jExitItem;
	}

	/**
	 * This method initializes jNewItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJNewItem() {
		if (jNewItem == null) {
			jNewItem = new JMenuItem();
			jNewItem.setText("New");
			jNewItem.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					controller.newCircuit();
				}
			});
		}
		return jNewItem;
	}

	/**
	 * This method initializes jToolBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getJToolBar() {
		if (jToolBar == null) {
			jToolBar = new JToolBar();
			jToolBar.setFloatable(false);
			jToolBar.setPreferredSize(new Dimension(4, 30));
			jToolBar.add(getJNewButton());
			jToolBar.add(getJSaveButton());
			jToolBar.add(getJSaveAllButton());
			jToolBar.add(getJOpenButton());
			jToolBar.addSeparator();
			jToolBar.add(getJAddStageButton());
			jToolBar.add(getJRemoveStageButton());
			jToolBar.addSeparator();
			jToolBar.add(getJAddWireButton());
			jToolBar.add(getJRemoveWireButton());
			jToolBar.addSeparator();
			jToolBar.add(getJSimulateButton());
		}
		return jToolBar;
	}

	/**
	 * This method initializes jCircuitsPane	
	 * 	
	 * @return javax.swing.JDesktopPane	
	 */
	private JDesktopPane getJCircuitsPane() {
		if (jCircuitsPane == null) {
			jCircuitsPane = new JDesktopPane();
			jCircuitsPane.setBackground(SystemColor.controlShadow);
			controller.setCircuitsPane(jCircuitsPane);
		}
		return jCircuitsPane;
	}

	/**
	 * This method initializes jNewButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJNewButton() {
		if (jNewButton == null) {
			jNewButton = new JButton();
			jNewButton.setIcon(new ImageIcon(getClass().getResource("/icons/1.gif")));
			jNewButton.setToolTipText("New");
			jNewButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.newCircuit();
				}
			});
		}
		return jNewButton;
	}

	/**
	 * This method initializes jSaveButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJSaveButton() {
		if (jSaveButton == null) {
			jSaveButton = new JButton();
			jSaveButton.setIcon(new ImageIcon(getClass().getResource("/icons/22.gif")));
			jSaveButton.setEnabled(false);
			jSaveButton.setToolTipText("Save");
			jSaveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.saveSelectedCircuit();
				}
			});
			circuitEditButtons.add(jSaveButton);
		}
		return jSaveButton;
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
			jPanel.add(getJToolBar(), BorderLayout.NORTH);
			jPanel.add(getJToolBar2(), BorderLayout.SOUTH);
		}
		return jPanel;
	}

	/**
	 * This method initializes jToolBar2	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getJToolBar2() {
		if (jToolBar2 == null) {
			jLabel = new JLabel();
			jLabel.setText("Controlled ");
			jLabel.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
			jLabel.setEnabled(false);
			circuitEditButtons.add(jLabel);
			jToolBar2 = new JToolBar();
			jToolBar2.setPreferredSize(new Dimension(18, 30));
			jToolBar2.setFloatable(false);
			jToolBar2.add(getJHadamardButton());
			jToolBar2.add(getJXButton());
			jToolBar2.add(getJYButton());
			jToolBar2.add(getJZButton());
			jToolBar2.add(getJPhaseButton());
			jToolBar2.addSeparator();
			jToolBar2.add(getJControlButton());
			jToolBar2.addSeparator();
			jToolBar2.add(getJMeasureButton());
			jToolBar2.add(getJAddQubitButton());
			jToolBar2.add(getJTraceButton());
			jToolBar2.addSeparator();
			jToolBar2.add(getJLoadedCircuitsBox());
			jToolBar2.add(getJLoadCircuitButton());
			jToolBar2.add(getJAddCircuitGateButton());
			jToolBar2.addSeparator();
			jToolBar2.add(jLabel);
			jToolBar2.add(getJControlledBox());
			jToolBar2.addSeparator();
			jToolBar2.add(getJRemoveGateButton());
		}
		return jToolBar2;
	}

	/**
	 * This method initializes jSaveAllButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJSaveAllButton() {
		if (jSaveAllButton == null) {
			jSaveAllButton = new JButton();
			jSaveAllButton.setIcon(new ImageIcon(getClass().getResource("/icons/22b.gif")));
			jSaveAllButton.setEnabled(false);
			jSaveAllButton.setToolTipText("Save all");
		}
		return jSaveAllButton;
	}

	/**
	 * This method initializes jOpenButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJOpenButton() {
		if (jOpenButton == null) {
			jOpenButton = new JButton();
			jOpenButton.setIcon(new ImageIcon(getClass().getResource("/icons/52.gif")));
			jOpenButton.setToolTipText("Open");
			jOpenButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.loadCircuit();
				}
			});
		}
		return jOpenButton;
	}

	/**
	 * This method initializes jHadamardButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJHadamardButton() {
		if (jHadamardButton == null) {
			jHadamardButton = new JButton();
			jHadamardButton.setText("");
			jHadamardButton.setIcon(new ImageIcon(getClass().getResource("/icons/hadamard.gif")));
			jHadamardButton.setToolTipText("Add Hadamard gate");
			jHadamardButton.setEnabled(false);
			jHadamardButton.setPreferredSize(new Dimension(28, 28));
			jHadamardButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.addCommonGate(Stage.GATE_HADAMARD, jControlledBox.isSelected());
				}
			});
			circuitEditButtons.add(jHadamardButton);
		}
		return jHadamardButton;
	}

	/**
	 * This method initializes jXButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJXButton() {
		if (jXButton == null) {
			jXButton = new JButton();
			jXButton.setText("");
			jXButton.setToolTipText("Add Pauli X gate");
			jXButton.setEnabled(false);
			jXButton.setIcon(new ImageIcon(getClass().getResource("/icons/PauliX.gif")));
			jXButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.addCommonGate(Stage.GATE_PAULIX, jControlledBox.isSelected());
				}
			});
			circuitEditButtons.add(jXButton);
		}
		return jXButton;
	}

	/**
	 * This method initializes jYButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJYButton() {
		if (jYButton == null) {
			jYButton = new JButton();
			jYButton.setIcon(new ImageIcon(getClass().getResource("/icons/PauliY.gif")));
			jYButton.setEnabled(false);
			jYButton.setToolTipText("Add Pauli Y gate");
			jYButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.addCommonGate(Stage.GATE_PAULIY, jControlledBox.isSelected());
				}
			});
			circuitEditButtons.add(jYButton);
		}
		return jYButton;
	}

	/**
	 * This method initializes jZButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJZButton() {
		if (jZButton == null) {
			jZButton = new JButton();
			jZButton.setToolTipText("Add Pauli Z gate");
			jZButton.setEnabled(false);
			jZButton.setIcon(new ImageIcon(getClass().getResource("/icons/PauliZ.gif")));
			jZButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.addCommonGate(Stage.GATE_PAULIZ, jControlledBox.isSelected());
				}
			});
			circuitEditButtons.add(jZButton);
		}
		return jZButton;
	}

	/**
	 * This method initializes jPhaseButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJPhaseButton() {
		if (jPhaseButton == null) {
			jPhaseButton = new JButton();
			jPhaseButton.setToolTipText("Add phase shift gate");
			jPhaseButton.setEnabled(false);
			jPhaseButton.setIcon(new ImageIcon(getClass().getResource("/icons/Phase.gif")));
			jPhaseButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JEP j = new JEP();
					j.addStandardConstants();
					j.addStandardFunctions();
					j.setImplicitMul(true);
					boolean err = true;
					String alpha = "";
					while(err){
						err = false;
						alpha = JOptionPane.showInputDialog(main, "Insert alpha value (e^(i*alpha))", "Phase gate", JOptionPane.QUESTION_MESSAGE);
						try {
							j.evaluate(j.parse(alpha));
						} catch (ParseException e1) {
							err = true;
							JOptionPane.showMessageDialog(main, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
					controller.addPhaseGate(alpha, jControlledBox.isSelected());
				}
			});
			circuitEditButtons.add(jPhaseButton);
		}
		return jPhaseButton;
	}

	/**
	 * This method initializes jLoadedCircuitsBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	public JComboBox getJLoadedCircuitsBox() {
		if (jLoadedCircuitsBox == null) {
			jLoadedCircuitsBox = new JComboBox();
			jLoadedCircuitsBox.setPreferredSize(new Dimension(28, 28));
			jLoadedCircuitsBox.setEnabled(false);
			jLoadedCircuitsBox.setSize(new Dimension(338, 28));
			circuitEditButtons.add(jLoadedCircuitsBox);
		}
		return jLoadedCircuitsBox;
	}

	/**
	 * This method initializes jLoadCircuitButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJLoadCircuitButton() {
		if (jLoadCircuitButton == null) {
			jLoadCircuitButton = new JButton();
			jLoadCircuitButton.setText("...");
			jLoadCircuitButton.setToolTipText("Load circuit...");
			jLoadCircuitButton.setEnabled(false);
			jLoadCircuitButton.setPreferredSize(new Dimension(28, 28));
			jLoadCircuitButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.loadCircuitGate();
				}
			});
			circuitEditButtons.add(jLoadCircuitButton);
		}
		return jLoadCircuitButton;
	}

	/**
	 * This method initializes jMeasureButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJMeasureButton() {
		if (jMeasureButton == null) {
			jMeasureButton = new JButton();
			jMeasureButton.setToolTipText("Add measurement");
			jMeasureButton.setEnabled(false);
			jMeasureButton.setIcon(new ImageIcon(getClass().getResource("/icons/Measure.gif")));
			jMeasureButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.addNoUnitaryGate(Stage.GATE_MEASURE);
				}
			});
			circuitEditButtons.add(jMeasureButton);
		}
		return jMeasureButton;
	}

	/**
	 * This method initializes jAddQubitButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJAddQubitButton() {
		if (jAddQubitButton == null) {
			jAddQubitButton = new JButton();
			jAddQubitButton.setToolTipText("Add new ancillary qubit");
			jAddQubitButton.setEnabled(false);
			jAddQubitButton.setIcon(new ImageIcon(getClass().getResource("/icons/AddQubit.gif")));
			jAddQubitButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.addNoUnitaryGate(Stage.GATE_ADDQUBIT);
				}
			});
			circuitEditButtons.add(jAddQubitButton);
		}
		return jAddQubitButton;
	}

	/**
	 * This method initializes jTraceButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJTraceButton() {
		if (jTraceButton == null) {
			jTraceButton = new JButton();
			jTraceButton.setText("");
			jTraceButton.setIcon(new ImageIcon(getClass().getResource("/icons/Trace.gif")));
			jTraceButton.setEnabled(false);
			jTraceButton.setToolTipText("Trace out qubit");
			jTraceButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.addNoUnitaryGate(Stage.GATE_TRACE);
				}
			});
			circuitEditButtons.add(jTraceButton);
		}
		return jTraceButton;
	}

	/**
	 * This method initializes jControlledBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJControlledBox() {
		if (jControlledBox == null) {
			jControlledBox = new JCheckBox();
			jControlledBox.setEnabled(false);
			circuitEditButtons.add(jControlledBox);
		}
		return jControlledBox;
	}

	/**
	 * This method initializes jControlButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJControlButton() {
		if (jControlButton == null) {
			jControlButton = new JButton();
			jControlButton.setIcon(new ImageIcon(getClass().getResource("/icons/Control.gif")));
			jControlButton.setEnabled(false);
			jControlButton.setToolTipText("Set control qubit");
			jControlButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.addControlQubit();
				}
			});
			circuitEditButtons.add(jControlButton);
		}
		return jControlButton;
	}
	
	public Set<Component> getCircuitEditButtons(){
		return circuitEditButtons;
	}

	/**
	 * This method initializes jAddStageButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJAddStageButton() {
		if (jAddStageButton == null) {
			jAddStageButton = new JButton();
			jAddStageButton.setText("S+");
			jAddStageButton.setToolTipText("Add stage");
			jAddStageButton.setEnabled(false);
			jAddStageButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.addStage();
				}
			});
			circuitEditButtons.add(jAddStageButton);
		}
		return jAddStageButton;
	}

	/**
	 * This method initializes jRemoveStageButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJRemoveStageButton() {
		if (jRemoveStageButton == null) {
			jRemoveStageButton = new JButton();
			jRemoveStageButton.setText("S -");
			jRemoveStageButton.setBounds(new Rectangle(141, 1, 27, 26));
			jRemoveStageButton.setToolTipText("Remove stage");
			jRemoveStageButton.setEnabled(false);
			jRemoveStageButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.removeStage();
				}
			});
			circuitEditButtons.add(jRemoveStageButton);
		}
		return jRemoveStageButton;
	}

	/**
	 * This method initializes jAddWireButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJAddWireButton() {
		if (jAddWireButton == null) {
			jAddWireButton = new JButton();
			jAddWireButton.setText("W+");
			jAddWireButton.setToolTipText("Add wire");
			jAddWireButton.setEnabled(false);
			jAddWireButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.addWire();
				}
			});
			circuitEditButtons.add(jAddWireButton);
		}
		return jAddWireButton;
	}

	/**
	 * This method initializes jRemoveWireButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJRemoveWireButton() {
		if (jRemoveWireButton == null) {
			jRemoveWireButton = new JButton();
			jRemoveWireButton.setText("W -");
			jRemoveWireButton.setToolTipText("Remove wire");
			jRemoveWireButton.setEnabled(false);
			jRemoveWireButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.removeWire();
				}
			});
			circuitEditButtons.add(jRemoveWireButton);
		}
		return jRemoveWireButton;
	}

	/**
	 * This method initializes jRemoveGateButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJRemoveGateButton() {
		if (jRemoveGateButton == null) {
			jRemoveGateButton = new JButton();
			jRemoveGateButton.setText("");
			jRemoveGateButton.setIcon(new ImageIcon(getClass().getResource("/icons/RemGate.gif")));
			jRemoveGateButton.setEnabled(false);
			jRemoveGateButton.setToolTipText("Remove gate");
			jRemoveGateButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.removeGate();
				}
			});
			circuitEditButtons.add(jRemoveGateButton);
		}
		return jRemoveGateButton;
	}

	/**
	 * This method initializes jSimulateButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getJSimulateButton() {
		if (jSimulateButton == null) {
			jSimulateButton = new JButton();
			jSimulateButton.setIcon(new ImageIcon(getClass().getResource("/icons/play.png")));
			jSimulateButton.setEnabled(false);
			jSimulateButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.simulate();
				}
			});
		}
		return jSimulateButton;
	}

	/**
	 * This method initializes jAddCircuitGateButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJAddCircuitGateButton() {
		if (jAddCircuitGateButton == null) {
			jAddCircuitGateButton = new JButton();
			jAddCircuitGateButton.setIcon(new ImageIcon(getClass().getResource("/icons/circuitGate.gif")));
			jAddCircuitGateButton.setToolTipText("Add selected circuit gate");
			jAddCircuitGateButton.setEnabled(false);
			jAddCircuitGateButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					controller.addCircuitGate(jControlledBox.isSelected());
				}
			});
			circuitEditButtons.add(jAddCircuitGateButton);
		}
		return jAddCircuitGateButton;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
