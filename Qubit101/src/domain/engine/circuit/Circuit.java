
package domain.engine.circuit;

import domain.engine.quantum.QuantumState;
import java.util.Iterator;
import java.util.Vector;

public class Circuit implements Iterable<Stage> {

	private Vector<Stage> circuit;
	private String name;

	public Circuit() {
		circuit = new Vector<Stage>();
	}

	public int getInputSize() {
		return circuit.get(0).getInputSize();
	}

	public int getOutputSize() {
		return circuit.lastElement().getOutputSize();
	}

	public QuantumState simulateCircuit(QuantumState input) {
		return simulateCircuit(input, 0);
	}

	public QuantumState simulateCircuit(QuantumState input, int disp) {
		return simulateCircuit(input, disp, new int[0]);
	}

	public QuantumState simulateCircuit(QuantumState input, int disp,
			int externalControlQubits[]) {
		QuantumState res = input;
		for (int i = 0; i < circuit.size() - 1; i++)
			if (circuit.get(i).getOutputSize() != circuit.get(i + 1)
					.getInputSize())
				throw new CircuitStructureException(
						"Different size between stage " + i
								+ " output and stage " + (i + 1) + " input.");

		for (Stage g : circuit) {
			res = g.simulate(res, disp, externalControlQubits);
		}

		return res;
	}

	public void addStage(Stage stage, int index) {
		circuit.add(index, stage);
	}

	public void removeStage(int index) {
		circuit.remove(index);
	}

	public int size() {
		return circuit.size();
	}

	public Stage getStage(int index) {
		return circuit.get(index);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		String res = "";
		for (Stage s : circuit) {
			res = res + s + "\n";
		}

		return res;
	}

	public Iterator<Stage> iterator() {
		return circuit.iterator();
	}

	class CircuitStructureException extends RuntimeException {

		private static final long serialVersionUID = 0x16757970492ed491L;

		public CircuitStructureException() {
			super();
		}

		public CircuitStructureException(String msg) {
			super(msg);
		}
	}

}
