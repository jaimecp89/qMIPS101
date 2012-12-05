package domain.files;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import domain.engine.circuit.Circuit;
import domain.engine.circuit.Stage;
import domain.engine.quantum.gates.*;
import domain.engine.quantum.interfaces.Gate;
import domain.engine.quantum.interfaces.UnitaryGate;

import org.w3c.dom.NodeList;


public class CircuitLoader extends XMLLoader{
	private Circuit circuit;
	private String circuitName;
	
	public CircuitLoader(File source) throws ParserConfigurationException, SAXException, IOException{
		super(source);
		buildCircuit();
	}
	
	public CircuitLoader(Node n) throws ParserConfigurationException{
		super(n);
		buildCircuit();
	}
	
	
	private void buildCircuit() throws ParserConfigurationException{
		Document DOM = getDOM();
		Node NCir = DOM.getChildNodes().item(0);
		Map<String,String> atNCir = getAttributes(NCir);
		int size=Integer.valueOf(atNCir.get("size"));
		circuitName = atNCir.get("name");
		circuit = new Circuit();
		circuit.setName(circuitName);
		int x = 0;
		for(int i = 0; x < size; i++){
			Node n = NCir.getChildNodes().item(i);
			if(n.getNodeName().equals("stage")){
                buildStage(n, x);
                x++;
            }
		}
	}

    private void buildStage(Node n, int index) throws ParserConfigurationException{
        NodeList nl = n.getChildNodes();
        Stage stage = new Stage(Integer.parseInt(getAttributes(n).get("size")));
        circuit.addStage(stage, index);
        for(int i = 0; i< n.getChildNodes().getLength(); i++){
            Node nc = nl.item(i);
            if(nc.getNodeName().equals("gate")){
                Map<String,String> att = getAttributes(nc);
                int qubit = Integer.valueOf(att.get("qubit"));
                int gateId = Integer.valueOf(att.get("gate_id"));
                if(att.containsKey("controlled") && att.get("controlled").equals("true"))
                    stage.addControlledGate((UnitaryGate) getGateFromId(gateId,nc), qubit);
                else
                	stage.addGate(getGateFromId(gateId,nc), qubit);
            }else if(nc.getNodeName().equals("control")){
                Map<String,String> att = getAttributes(nc);
                int qubit = Integer.valueOf(att.get("qubit"));
                stage.addControlQubit(qubit);
            }else if(nc.getNodeName().equals("qcircuit")){
                Map<String,String> att = getAttributes(nc);
                int qubit = Integer.valueOf(att.get("qubit"));
                boolean controlled = "true".equals(att.get("controlled"));
                Circuit innc = buildCircuitGate(nc);
                if(!controlled)
                	stage.addGate(new CircuitGate(innc),qubit);
                else
                	stage.addControlledGate(new CircuitGate(innc),qubit); 
            }
        }
    }
	
	private Circuit buildCircuitGate(Node n) throws ParserConfigurationException{
		CircuitLoader cl = new CircuitLoader(n);
		return cl.circuit;
	}

    public Circuit getCircuit(){
        return circuit;
    }

    public String getCircuitName(){
        return circuitName;
    }

    private Gate getGateFromId(int id, Node nc){
    	Gate g = null;
    	switch(id){
    	case Stage.GATE_ADDQUBIT:
    		g = new AddQubit();
    		break;
    	case Stage.GATE_HADAMARD:
    		g = new HadamardGate();
    		break;
    	case Stage.GATE_MEASURE:
    		g = new Measure();
    		break;
    	case Stage.GATE_PAULIX:
    		g = new PauliXGate();
    		break;
    	case Stage.GATE_PAULIY:
    		g = new PauliYGate();
    		break;
    	case Stage.GATE_PAULIZ:
    		g = new PauliZGate();
    		break;
    	case Stage.GATE_PHASE:
    		g = new PhaseShiftGate(getAttributes(nc).get("alpha"));
    		break;
    	case Stage.GATE_TRACE:
    		g = new TraceOut();
    		break;
    	}
    	return g;
    }

}
