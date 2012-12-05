package presentation.swing;

import java.awt.Component;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import domain.engine.circuit.Circuit;
import domain.engine.circuit.Stage;
import domain.engine.circuit.Stage.Position;
import domain.engine.quantum.gates.AddQubit;
import domain.engine.quantum.gates.CircuitGate;
import domain.engine.quantum.gates.HadamardGate;
import domain.engine.quantum.gates.Measure;
import domain.engine.quantum.gates.PauliXGate;
import domain.engine.quantum.gates.PauliYGate;
import domain.engine.quantum.gates.PauliZGate;
import domain.engine.quantum.gates.PhaseShiftGate;
import domain.engine.quantum.gates.TraceOut;
import domain.engine.quantum.interfaces.Gate;
import domain.engine.quantum.interfaces.UnitaryGate;
import domain.files.CircuitLoader;
import domain.files.CircuitSaver;

public class MainViewController {

	private CircuitView selectedCircuit;
	private Vector<CircuitView> circuits = new Vector<CircuitView>();
	private Map<String, Circuit> circuitGates = new HashMap<String, Circuit>();
	private MainView mainFrame;
	private JDesktopPane circuitsPanel;

	public MainViewController() {
		mainFrame = new MainView(this);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(MainView.DISPOSE_ON_CLOSE);
		mainFrame.setVisible(true);
	}

	public void newCircuit() {
		CircuitView newView = null;
		boolean err = true;
		String name = "";
		int qubits = -1;
		while (err) {
			err = false;
			NewCircuitDialog ncd = new NewCircuitDialog(mainFrame);
			ncd.setLocationRelativeTo(mainFrame);
			Entry<String, Integer> properties = ncd.showDialog();
			if (ncd.accepted) {
				try {
					qubits = properties.getValue();
					name = properties.getKey();
					if (qubits < 1 || name.trim().equals("")) {
						JOptionPane
								.showMessageDialog(mainFrame, "Wrong input.");
						err = true;
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(mainFrame, "Wrong input.");
					err = true;
				}
				if (!err) {
					newView = new CircuitView(this, name, qubits);
					circuits.add(newView);
					newView.setSize(500, 200);
					newView.setVisible(true);
					circuitsPanel.add(newView);
					try {
						newView.setMaximum(true);
					} catch (PropertyVetoException e) {
					}
				}
			}
		}
	}

	public void circuitViewSelected(CircuitView view) {
		selectedCircuit = view;
		mainFrame.getJSimulateButton().setEnabled(true);
		int selectedStage = selectedCircuit.getSelectedStage();
		int circuitSize = selectedCircuit.getCircuit().size();
		if (selectedStage < 0 || selectedStage >= circuitSize) {
			setCircuitEditEnabled(false);
		} else {
			int selectedQubit = selectedCircuit.getSelectedQubit();
			int stageSize = selectedCircuit.getCircuit()
					.getStage(selectedStage).getInternalSize();
			if (selectedQubit < 0 || selectedQubit >= stageSize)
				setCircuitEditEnabled(false);
		}
	}

	public void circuitViewClosed(CircuitView view) {
		circuits.remove(view);
		if (circuits.size() == 0) {
			setCircuitEditEnabled(false);
			mainFrame.getJSimulateButton().setEnabled(false);
		}

	}

	public void setMainFrame(MainView main) {
		mainFrame = main;
	}

	public void setCircuitsPane(JDesktopPane panel) {
		circuitsPanel = panel;
	}

	public void setCircuitEditEnabled(boolean enabled) {
		for (Component c : mainFrame.getCircuitEditButtons()) {
			c.setEnabled(enabled);
		}
	}

	public void addCommonGate(int gateId, boolean controlled) {
		int selectedStage = selectedCircuit.getSelectedStage();
		int selectedQubit = selectedCircuit.getSelectedQubit();
		Gate gate = null;
		switch (gateId) {
		case Stage.GATE_HADAMARD:
			gate = new HadamardGate();
			break;
		case Stage.GATE_PAULIX:
			gate = new PauliXGate();
			break;
		case Stage.GATE_PAULIY:
			gate = new PauliYGate();
			break;
		case Stage.GATE_PAULIZ:
			gate = new PauliZGate();
			break;
		default:
			throw new IllegalArgumentException("No common gate.");
		}
		if (controlled)
			selectedCircuit.getCircuit().getStage(selectedStage)
					.addControlledGate((UnitaryGate) gate, selectedQubit);
		else
			selectedCircuit.getCircuit().getStage(selectedStage)
					.addGate(gate, selectedQubit);
		selectedCircuit.incSelectedQubit();
		selectedCircuit.repaint();
	}
	
	public void addPhaseGate(String phs, boolean controlled){
		int selectedStage = selectedCircuit.getSelectedStage();
		int selectedQubit = selectedCircuit.getSelectedQubit();
		Gate gate = new PhaseShiftGate(phs);
		if (controlled)
			selectedCircuit.getCircuit().getStage(selectedStage)
					.addControlledGate((UnitaryGate) gate, selectedQubit);
		else
			selectedCircuit.getCircuit().getStage(selectedStage)
					.addGate(gate, selectedQubit);
		selectedCircuit.incSelectedQubit();
		selectedCircuit.repaint();
	}

	public void addControlQubit() {
		int selectedStage = selectedCircuit.getSelectedStage();
		int selectedQubit = selectedCircuit.getSelectedQubit();
		selectedCircuit.getCircuit().getStage(selectedStage)
				.addControlQubit(selectedQubit);
		selectedCircuit.incSelectedQubit();
		selectedCircuit.repaint();
	}

	public void addNoUnitaryGate(int gateId) {
		int selectedStage = selectedCircuit.getSelectedStage();
		int selectedQubit = selectedCircuit.getSelectedQubit();
		Gate gate = null;
		switch (gateId) {
		case Stage.GATE_MEASURE:
			gate = new Measure();
			break;
		case Stage.GATE_ADDQUBIT:
			gate = new AddQubit();
			break;
		case Stage.GATE_TRACE:
			gate = new TraceOut();
			break;
		default:
			throw new IllegalArgumentException();
		}
		selectedCircuit.getCircuit().getStage(selectedStage)
				.addGate(gate, selectedQubit);
		selectedCircuit.incSelectedQubit();
		selectedCircuit.repaint();
	}

	public void addStage() {
		selectedCircuit.getCircuit().addStage(
				new Stage(selectedCircuit.getCircuit().getOutputSize()),
				selectedCircuit.getSelectedStage() + 1);
		selectedCircuit.repaint();
	}

	public void removeStage() {
		if (selectedCircuit.getCircuit().size() != 1) {
			selectedCircuit.getCircuit().removeStage(
					selectedCircuit.getSelectedStage());
			if (selectedCircuit.getSelectedStage() >= selectedCircuit
					.getCircuit().size())
				setCircuitEditEnabled(false);
		} else
			JOptionPane.showMessageDialog(mainFrame,
					"Cannot remove last stage", "Error",
					JOptionPane.ERROR_MESSAGE);
		selectedCircuit.repaint();
	}

	public void addWire() {
		selectedCircuit.getCircuit()
				.getStage(selectedCircuit.getSelectedStage())
				.addWire(selectedCircuit.getSelectedQubit());
		selectedCircuit.repaint();
	}

	public void removeWire() {
		if (selectedCircuit.getCircuit()
				.getStage(selectedCircuit.getSelectedStage()).getInternalSize() == 1) {
			removeStage();
		} else {
			selectedCircuit.getCircuit()
					.getStage(selectedCircuit.getSelectedStage())
					.removeWire(selectedCircuit.getSelectedQubit());
			if (selectedCircuit.getSelectedQubit() >= selectedCircuit
					.getCircuit().getStage(selectedCircuit.getSelectedStage())
					.getInternalSize())
				setCircuitEditEnabled(false);
		}
		selectedCircuit.repaint();
	}

	public void removeGate() {
		selectedCircuit.getCircuit()
				.getStage(selectedCircuit.getSelectedStage())
				.removeGate(selectedCircuit.getSelectedQubit());
		selectedCircuit.incSelectedQubit();
		selectedCircuit.repaint();
	}

	public void simulate() {
		new SimulationViewController(mainFrame, selectedCircuit);
	}

	public void selectionChanged() {
		if (selectedCircuit.getSelectedQubit() != -1
				&& selectedCircuit.getSelectedStage() != -1) {
			setCircuitEditEnabled(true);
		} else {
			setCircuitEditEnabled(false);
		}
	}

	public void saveSelectedCircuit() {
		saveCircuit(selectedCircuit.getCircuit());
	}

	public void saveCircuit(Circuit circuit) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")
				+ "//Circuits//"));
		fileChooser.setFileFilter(new FileNameExtensionFilter(
				"Quantum circuits", "qcml"));
		int retVal = fileChooser.showSaveDialog(mainFrame);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			File f = fileChooser.getSelectedFile();
			try {
				new CircuitSaver(circuit, f);
			} catch (Exception e) {
			}
		}
	}

	public void loadCircuit() {
		boolean err = false;
		CircuitView newView = null;
		Circuit circuit = null;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")
				+ "//Circuits//"));
		fileChooser.setFileFilter(new FileNameExtensionFilter(
				"Quantum circuits", "qcml"));
		int retVal = fileChooser.showOpenDialog(mainFrame);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			File f = fileChooser.getSelectedFile();
			try {
				circuit = new CircuitLoader(f).getCircuit();
			} catch (Exception e) {
				err = true;
				JOptionPane.showMessageDialog(mainFrame,
						"Error opening the circuit file.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			if (!err) {
				newView = new CircuitView(this, circuit);
				circuits.add(newView);
				newView.setSize(500, 200);
				newView.setVisible(true);
				circuitsPanel.add(newView);
				try {
					newView.setMaximum(true);
				} catch (PropertyVetoException e) {
				}
			}
		}
	}

	public void loadCircuitGate() {
		boolean err = false;
		Circuit circuit = null;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")
				+ "//Circuits//"));
		fileChooser.setFileFilter(new FileNameExtensionFilter(
				"Quantum circuits", "qcml"));
		int retVal = fileChooser.showOpenDialog(mainFrame);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			File f = fileChooser.getSelectedFile();
			try {
				circuit = new CircuitLoader(f).getCircuit();
			} catch (Exception e) {
				err = true;
				JOptionPane.showMessageDialog(mainFrame,
						"Error opening the circuit file.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			if (!err) {
				circuitGates.put(circuit.getName(), circuit);
				mainFrame.getJLoadedCircuitsBox().setEnabled(true);
				mainFrame.getJLoadedCircuitsBox().addItem(circuit.getName());
			}
		}
	}

	public void addCircuitGate(boolean controlled) {
		String sel = (String) mainFrame.getJLoadedCircuitsBox()
				.getSelectedItem();
		if (sel == null) {
			JOptionPane.showMessageDialog(mainFrame, "Select a circuit first.",
					"Error", JOptionPane.ERROR_MESSAGE);
		} else {
			Circuit circuit = circuitGates.get(sel);
			if (circuit.getInputSize() + selectedCircuit.getSelectedQubit() > selectedCircuit
					.getCircuit().getStage(selectedCircuit.getSelectedStage())
					.getInternalSize()) {
				JOptionPane.showMessageDialog(mainFrame,
						"The circuit will not fit the stage in that position",
						"Error", JOptionPane.ERROR_MESSAGE);
			} else {
				if (controlled) {
					selectedCircuit
							.getCircuit()
							.getStage(selectedCircuit.getSelectedStage())
							.addControlledGate(new CircuitGate(circuit),
									selectedCircuit.getSelectedQubit());
				} else {
					selectedCircuit
							.getCircuit()
							.getStage(selectedCircuit.getSelectedStage())
							.addGate(new CircuitGate(circuit),
									selectedCircuit.getSelectedQubit());
				}
				selectedCircuit.incSelectedQubit();
			}
		}
		selectedCircuit.repaint();
	}
	
	public void circuitDoubleClick(){
		Position p = selectedCircuit.getCircuit().getStage(selectedCircuit.getSelectedStage()).getPosition(selectedCircuit.getSelectedQubit());
		if((p.getContent() == Stage.CONTENT_GATE || p.getContent() == Stage.CONTENT_CONTROLLED) && p.getGate().getGateId() == Stage.GATE_CIRCUIT){
			CircuitView newView = new CircuitView(this, ((CircuitGate)p.getGate()).getCircuit());
			circuits.add(newView);
			newView.setSize(500, 200);
			newView.setVisible(true);
			circuitsPanel.add(newView);
			try {
				newView.setMaximum(true);
			} catch (PropertyVetoException e) {
			}
		}
	}

}
