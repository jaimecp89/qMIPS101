package domain.files;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import domain.engine.circuit.Circuit;
import domain.engine.circuit.Stage;
import domain.engine.circuit.Stage.Position;
import domain.engine.quantum.gates.CircuitGate;
import domain.engine.quantum.gates.PhaseShiftGate;

import org.w3c.dom.Node;

public class CircuitSaver extends XMLSaver {

    private static Document dom;

    private CircuitSaver(Document d, File f) throws FileNotFoundException, XMLStreamException, IOException {
        super(d, f);
    }

    public CircuitSaver(Circuit c, File f) throws FileNotFoundException, XMLStreamException, ParserConfigurationException, IOException {
        super(buildDocument(c, f.getName()), f);
    }

    private static Document buildDocument(Circuit c, String name) throws ParserConfigurationException {
        dom = buildDOM();
        dom.appendChild(buildNodeTree(c, name, null, null));
        dom.normalize();
        return dom;
    }

    private static Node buildNodeTree(Circuit c, String name, String qubit, String controlled) throws ParserConfigurationException {
        Element nCir = dom.createElement("qcircuit");
        nCir.setAttribute("name", name);
        nCir.setAttribute("size", String.valueOf(c.size()));
        if(qubit != null)
        	nCir.setAttribute("qubit", qubit);
        if(controlled != null)
        	nCir.setAttribute("controlled", controlled);
        for (int j = 0; j < c.size(); j++) {
            generateStageDOM(nCir, c.getStage(j));
        }
        return nCir;
    }

    private static void generateStageDOM(Element e, Stage stage) throws DOMException, ParserConfigurationException {
        Element eSt = dom.createElement("stage");
        eSt.setAttribute("size", Integer.toString(stage.getInternalSize()));
        e.appendChild(eSt);
        for (int j = 0; j < stage.getInternalSize(); j++) {
            Position p = stage.getPosition(j);
            Element eG;
            switch (p.getContent()) {
                case Stage.CONTENT_GATE:
                case Stage.CONTENT_CONTROLLED:
                	if(p.getGate().getGateId() == Stage.GATE_CIRCUIT){
                		Circuit cir = ((CircuitGate)p.getGate()).getCircuit();
                        eG = dom.createElement("measure");
                        eG.setAttribute("qubit", Integer.toString(j));
                        if(p.getContent() == Stage.CONTENT_CONTROLLED)
                        	eSt.appendChild(buildNodeTree(cir, cir.getName(),Integer.toString(j), "true"));
                        else
                        	eSt.appendChild(buildNodeTree(cir, cir.getName(),Integer.toString(j), null));
                	}else{
                		eG = dom.createElement("gate");
                        eG.setAttribute("gate_id", Integer.toString(p.getGate().getGateId()));
                        eG.setAttribute("qubit", Integer.toString(j));
                        if(p.getContent() == Stage.CONTENT_CONTROLLED)
                        	eG.setAttribute("controlled", "true");
                        if(p.getGate().getGateId() == Stage.GATE_PHASE)
                        	eG.setAttribute("alpha",((PhaseShiftGate)p.getGate()).getAlfa());
                        eSt.appendChild(eG);
                	}
                    break;
                case Stage.CONTENT_CONTROL:
                    eG = dom.createElement("control");
                    eG.setAttribute("qubit", Integer.toString(j));
                    eSt.appendChild(eG);
                    break;
            }
        }
    }

    private static Document buildDOM() {
        Document d = null;
        try {
            d = (DocumentBuilderFactory.newInstance().newDocumentBuilder()).newDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }
}
