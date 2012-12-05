package presentation.swing;

import java.awt.Component;
import java.util.Vector;

import javax.swing.JOptionPane;

import domain.engine.circuit.Circuit;
import domain.engine.circuit.Stage;
import domain.engine.circuit.Stage.Position;
import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;
import domain.engine.quantum.QuantumState;
import domain.engine.quantum.gates.Measure;

public class SimulationViewController {

	SimulationView sim;
	CircuitView selectedCircuit;
	private Vector<Vector<Byte>> measurementRes = null;
	private Vector<QuantumState> states = null;

	public SimulationViewController(MainView mainFrame,
			CircuitView selectedCircuit) {
		sim = new SimulationView(this, mainFrame, selectedCircuit.getCircuit());
		sim.setLocationRelativeTo(mainFrame);
		sim.setTitle("Simulating circuit " + selectedCircuit.getTitle());
		this.selectedCircuit = selectedCircuit;
		Circuit circuit = selectedCircuit.getCircuit();
		for (int i = 0; i < circuit.size() - 1; i++) {
			if (circuit.getStage(i).getOutputSize() != circuit.getStage(i + 1)
					.getInputSize()) {
				JOptionPane
						.showMessageDialog(sim,
								"Cannot simulate the circuit. Different size between stage "
										+ i + " output and stage " + (i + 1)
										+ " input.", "Error",
								JOptionPane.ERROR_MESSAGE);
				sim = null;
			}
		}
		if (sim != null) {
			sim.setVisible(true);
		}
	}

	public void quickSimulation() {
		boolean err = false;
		byte[] inputArr = null;
		try {
			inputArr = parseInput(sim.getJInputField().getText(),
					sim.getInputFormat());
		} catch (IllegalArgumentException e) {
			err = true;
			JOptionPane.showMessageDialog(sim, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		if (!err) {
			QuantumState input = new QuantumState();
			input.add(new Complex(1.0, 0.0, false), new ClassicState(inputArr));
			measurementRes = new Vector<Vector<Byte>>();
			int i = 0;
			for (Stage s : selectedCircuit.getCircuit()) {
				input = s.simulate(input);
				measurementRes.add(new Vector<Byte>());
				for (Position p : s) {
					if (p.getContent() == Stage.CONTENT_GATE
							&& p.getGate().getGateId() == Stage.GATE_MEASURE) {
						measurementRes.get(i).add(
								((Measure) p.getGate()).getMeasurementResult());
					} else {
						measurementRes.get(i).add((byte) -1);
					}
				}
				i++;
			}
			setSimulationViewButtonsEnabled(true);
			sim.getJTabbedPane().setEnabledAt(0, true);
			sim.getJMeasurementsPane().setText("");
			i = 0;
			for (Byte b : measurementRes.get(selectedCircuit.getCircuit()
					.size() - 1)) {
				if (b != -1)
					sim.getJMeasurementsPane().setText(
							sim.getJMeasurementsPane().getText()
									+ "Measurement on qubit " + i
									+ ", result: " + b + ".\n");
				i++;
			}
			states = null;
			sim.getJTabbedPane().setEnabledAt(1, false);
			sim.getJTabbedPane().setSelectedIndex(0);
			sim.getJCircuitPanel().setSelectedStage(
					sim.getJCircuitPanel().getCircuit().size() - 1);
			sim.getJCircuitPanel().setInput(inputArr);
			sim.repaint();
		}
	}
	
	public void completeSimulation(){
		boolean err = false;
		byte[] inputArr = null;
		states = new Vector<QuantumState>();
		try {
			inputArr = parseInput(sim.getJInputField().getText(),
					sim.getInputFormat());
		} catch (IllegalArgumentException e) {
			err = true;
			JOptionPane.showMessageDialog(sim, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		if (!err) {
			QuantumState input = new QuantumState();
			input.add(new Complex(1.0, 0.0, false), new ClassicState(inputArr));
			measurementRes = new Vector<Vector<Byte>>();
			int i = 0;
			for (Stage s : selectedCircuit.getCircuit()) {
				if(states.size() == 0)
					states.add(s.simulate(input));
				else{
					states.add(s.simulate(states.lastElement()));
				}
				measurementRes.add(new Vector<Byte>());
				for (Position p : s) {
					if (p.getContent() == Stage.CONTENT_GATE
							&& p.getGate().getGateId() == Stage.GATE_MEASURE) {
						measurementRes.get(i).add(
								((Measure) p.getGate()).getMeasurementResult());
					} else {
						measurementRes.get(i).add((byte) -1);
					}
				}
				i++;
			}
			setSimulationViewButtonsEnabled(true);
			sim.getJTabbedPane().setEnabledAt(0, true);
			sim.getJTabbedPane().setEnabledAt(1, true);
			sim.getJMeasurementsPane().setText("");
			i = 0;
			for (Byte b : measurementRes.get(selectedCircuit.getCircuit()
					.size() - 1)) {
				if (b != -1)
					sim.getJMeasurementsPane().setText(
							sim.getJMeasurementsPane().getText()
									+ "Measurement on qubit " + i
									+ ", result: " + b + ".\n");
				i++;
			}
			sim.getJCircuitPanel().setSelectedStage(
					sim.getJCircuitPanel().getCircuit().size() - 1);
			sim.getJStatePane().setText(states.get(sim.getJCircuitPanel().getSelectedStage()).toString());
			sim.getJCircuitPanel().setInput(inputArr);
			sim.repaint();
		}
	}

	public byte[] parseInput(String input, int format) {
		int inputSize = selectedCircuit.getCircuit().getInputSize();
		byte[] res = new byte[inputSize];
		String resString = input;
		switch (format) {
		case SimulationView.INPUT_DEC:
			resString = Integer.toBinaryString(Integer.parseInt(input));
			break;
		case SimulationView.INPUT_HEX:
			resString = Integer.toBinaryString(Integer.decode("0x" + input));
			break;
		}
		int leadingZeros = inputSize - resString.length();
		if(leadingZeros < 0)
			throw new IllegalArgumentException("The input string do not fit the circuit input.");
		for(int i = inputSize-1; i >= 0; i--){
			if(leadingZeros > 0){
				res[i] = 0;
				leadingZeros--;
			}else{
				if(resString.charAt(i)!= '0' && resString.charAt(i)!= '1')
					throw new IllegalArgumentException("The input must be a binary string.");
				res[i] = Byte.parseByte(String.valueOf(resString.charAt(i)));
			}
		}
		return res;
	}

	private void setSimulationViewButtonsEnabled(boolean enabled) {
		for (Component c : sim.getSimulationButtons()) {
			c.setEnabled(enabled);
		}
	}

	public void nextStage() {
		if (sim.getJCircuitPanel().getSelectedStage() != (selectedCircuit
				.getCircuit().size() - 1)) {
			sim.getJMeasurementsPane().setText("");
			sim.getJCircuitPanel().setSelectedStage(
					sim.getJCircuitPanel().getSelectedStage() + 1);
			int i = 0;
			for (Byte b : measurementRes.get(sim.getJCircuitPanel()
					.getSelectedStage())) {
				if (b != -1)
					sim.getJMeasurementsPane().setText(
							sim.getJMeasurementsPane().getText()
									+ "Measurement on qubit " + i
									+ ", result: " + b + "\n");
				i++;
			}
			if(states != null)
				sim.getJStatePane().setText(states.get(sim.getJCircuitPanel().getSelectedStage()).toString());
		}
		sim.repaint();
	}

	public void previousStage() {
		if (sim.getJCircuitPanel().getSelectedStage() != 0) {
			sim.getJMeasurementsPane().setText("");
			sim.getJCircuitPanel().setSelectedStage(
					sim.getJCircuitPanel().getSelectedStage() - 1);
			int i = 0;
			for (Byte b : measurementRes.get(sim.getJCircuitPanel()
					.getSelectedStage())) {
				if (b != -1)
					sim.getJMeasurementsPane().setText(
							sim.getJMeasurementsPane().getText()
									+ "Measurement on qubit " + i
									+ ", result: " + b + "\n");
				i++;
			}
			if(states != null)
				sim.getJStatePane().setText(states.get(sim.getJCircuitPanel().getSelectedStage()).toString());
		}
		sim.repaint();
	}

}
