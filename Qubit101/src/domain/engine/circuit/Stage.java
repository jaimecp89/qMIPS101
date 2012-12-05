

package domain.engine.circuit;

import domain.engine.quantum.QuantumState;
import domain.engine.quantum.gates.CircuitGate;
import domain.engine.quantum.interfaces.Gate;
import domain.engine.quantum.interfaces.UnitaryGate;
import java.util.Iterator;
import java.util.Vector;



public class Stage implements Iterable<Stage.Position> {

	public static final int GATE_HADAMARD = 1, GATE_SWAP = 2, GATE_PAULIX = 3,
			GATE_PAULIY = 4, GATE_PAULIZ = 5, GATE_PHASE = 6, GATE_CIRCUIT = 7;
	public static final int GATE_ADDQUBIT = -1, GATE_TRACE = -2,
			GATE_MEASURE = -3;
	public static final int CONTENT_EMPTY = 0, CONTENT_GATE = 1,
			CONTENT_CONTROLLED = 2, CONTENT_CONTROL = 3, CONTENT_FULL = 4;
	private Vector<Position> stage;

	public Stage(int inputStateSize) {
		stage = new Vector<Position>();
		for (int i = 0; i < inputStateSize; i++)
			stage.add(new Position(null, CONTENT_EMPTY));

	}

	public void addGate(Gate gate, int qubit) {
		if (gate.getGateId() == GATE_CIRCUIT) {
			Circuit c = ((CircuitGate) gate).getCircuit();
			if (qubit < 0 || qubit + c.getInputSize() > stage.size())
				throw new IndexOutOfBoundsException(
						"Circuit out of stage bounds.");
			for (int i = 0; i < c.getInputSize(); i++) {
				removeGate(qubit + i);
				if (i == 0)
					stage.set(qubit, new Position(gate, CONTENT_GATE));
				else
					stage.set(qubit + i, new Position(null, CONTENT_FULL));
			}

		} else {
			if (qubit < 0 || qubit >= stage.size())
				throw new IndexOutOfBoundsException("Gate out of stage bounds.");
			removeGate(qubit);
			stage.set(qubit, new Position(gate, CONTENT_GATE));
		}
	}

	public void addControlledGate(UnitaryGate gate, int qubit) {
		if (gate.getGateId() == GATE_CIRCUIT) {
			Circuit c = ((CircuitGate) gate).getCircuit();
			if (qubit < 0 || qubit + c.getInputSize() > stage.size())
				throw new IndexOutOfBoundsException(
						"Circuit out of stage bounds.");
			for (int i = 0; i < c.getInputSize(); i++) {
				removeGate(qubit + i);
				if (i == 0)
					stage.set(qubit, new Position(gate, CONTENT_CONTROLLED));
				else
					stage.set(qubit + i, new Position(null, CONTENT_FULL));
			}

		} else {
			if (qubit < 0 || qubit >= stage.size())
				throw new IndexOutOfBoundsException("Gate out of stage bounds.");
			removeGate(qubit);
			stage.set(qubit, new Position(gate, CONTENT_CONTROLLED));
		}
	}

	public void addControlQubit(int qubit) {
		if (qubit < 0 || qubit >= stage.size()) {
			throw new IndexOutOfBoundsException("Gate out of stage bounds.");
		} else {
			removeGate(qubit);
			stage.set(qubit, new Position(null, CONTENT_CONTROL));
			return;
		}
	}

	public void removeGate(int qubit) {
		if (qubit < 0 || qubit >= stage.size())
			throw new IndexOutOfBoundsException("Index out of stage bounds.");
		Position old = (Position) stage.get(qubit);
		if (old.getContent() == CONTENT_FULL)
			removeGate(qubit - 1);
		else if ((old.getContent() == CONTENT_GATE || old.getContent() == CONTENT_CONTROLLED)
				&& old.getGate().getGateId() == GATE_CIRCUIT) {
			for (int i = 0; i < ((CircuitGate) old.getGate()).getCircuit()
					.getInputSize(); i++)
				stage.set(qubit + i, new Position(null, CONTENT_EMPTY));

		} else {
			stage.set(qubit, new Position(null, CONTENT_EMPTY));
		}
	}

	public void removeWire(int wire) {
		if (wire < 0 || wire >= stage.size()) {
			throw new IndexOutOfBoundsException("Index out of stage bounds.");
		} else {
			removeGate(wire);
			stage.remove(wire);
			return;
		}
	}

	public void addWire(int index) {
		if (index < 0 || index > stage.size())
			throw new IndexOutOfBoundsException("Index out of stage bounds.");
		if (index != stage.size()
				&& ((Position) stage.get(index)).getContent() == CONTENT_FULL)
			removeGate(index);
		stage.add(index, new Position(null, CONTENT_EMPTY));
	}

	public int getInputSize() {
		int res = 0;
		for (Position p : stage) {
			switch (p.getContent()) {
			case CONTENT_GATE:
			case CONTENT_CONTROLLED:
				if (p.getGate().getGateId() != GATE_ADDQUBIT)
					if(p.getGate().getGateId() == GATE_CIRCUIT)
						res = res + ((CircuitGate) p.getGate()).getCircuit().getInputSize();
					else
						res++;
				break;
			case CONTENT_FULL:
				break;
			default:
				res++;
				break;
			}
		}

		return res;
	}

	public int getOutputSize() {
		int res = 0;
		for (Position p : stage) {
			switch (p.getContent()) {
			case CONTENT_GATE:
			case CONTENT_CONTROLLED:
				if (p.getGate().getGateId() != GATE_TRACE)
					if(p.getGate().getGateId() == GATE_CIRCUIT)
						res = res + ((CircuitGate) p.getGate()).getCircuit().getOutputSize();
					else
						res++;
				break;
			case CONTENT_FULL:
				break;
			default:
				res++;
				break;
			}
		}

		return res;
	}

	public int getInternalSize() {
		return stage.size();
	}

	public Position getPosition(int index) {
		return stage.get(index);
	}

	public QuantumState simulate(QuantumState input, int disp,
			int externalControlQubits[]) {
		QuantumState res = input;
		Vector<Integer> controlQubits = new Vector<Integer>();
		for (int i = 0; i < stage.size(); i++)
			if (stage.get(i).getContent() == CONTENT_CONTROL)
				controlQubits.add(Integer.valueOf(i + disp));
		int controlQubitsAuxArray[] = new int[controlQubits.size()];
		for (int i = 0; i < controlQubits.size(); i++)
			controlQubitsAuxArray[i] = ((Integer) controlQubits.get(i))
					.intValue();
		int controlQubitsArray[] = new int[controlQubits.size()
				+ externalControlQubits.length];
		System.arraycopy(controlQubitsAuxArray, 0, controlQubitsArray, 0,
				controlQubitsAuxArray.length);
		System.arraycopy(externalControlQubits, 0, controlQubitsArray,
				controlQubitsAuxArray.length, externalControlQubits.length);
		for (int i = 0; i < stage.size(); i++) {
			Position p = stage.get(i);
			switch (stage.get(i).getContent()) {
			default:
				break;
			case CONTENT_GATE:
				if (p.getGate().getGateId() > 0) {
					res = ((UnitaryGate) p.getGate()).operate(res, i + disp,
							externalControlQubits);
					break;
				}
				if (p.getGate().getGateId() >= 0)
					break;
				res = p.getGate().operate(res, i + disp);
				if (p.getGate().getGateId() == GATE_TRACE)
					disp--;
				break;
			case CONTENT_CONTROLLED:
				UnitaryGate g = (UnitaryGate) p.getGate();
				res = g.operate(res, i + disp, controlQubitsArray);
				break;
			}
		}

		return res;
	}

	public QuantumState simulate(QuantumState input, int disp) {
		return simulate(input, disp, new int[0]);
	}

	public QuantumState simulate(QuantumState input) {
		return simulate(input, 0);
	}

	public Iterator<Position> iterator() {
		return stage.iterator();
	}


	public class Position {

		public int getContent() {
			return content;
		}

		public Gate getGate() {
			return gate;
		}

		private Gate gate;
		private int content;

		public Position(Gate gate, int content) {
			super();
			this.gate = gate;
			this.content = content;
		}
	}

}
