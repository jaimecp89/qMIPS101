package presentation.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.PopupMenu;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JPanel;

import domain.engine.circuit.Circuit;
import domain.engine.circuit.Stage;
import domain.engine.circuit.Stage.Position;
import domain.engine.quantum.gates.CircuitGate;

public class JCircuitPanel extends JPanel implements MouseListener, MouseMotionListener{

	private static final long serialVersionUID = 7591303926932774571L;
	private Circuit circuit;
	private int x, y, hoverStage, hoverQubit, selectedStage = -1, selectedQubit = -1;
	private boolean editable = true;
	private MainViewController controller;
	private byte[] input;
	private PopupMenu popupMenu;

	public JCircuitPanel(MainViewController controller, Circuit circuit){
		super();
		this.circuit = circuit;
		this.controller = controller;
		this.setBackground(new Color(255, 255, 255));
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.popupMenu = new PopupMenu();
		this.add(popupMenu);
	}
	
	public JCircuitPanel(Circuit circuit){
		super();
		this.circuit = circuit;
		editable = false;
		this.setBackground(new Color(255, 255, 255));
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
	}
	
	public Circuit getCircuit() {
		return circuit;
	}
	
	public int getSelectedStage() {
		return selectedStage;
	}

	public int getSelectedQubit() {
		return selectedQubit;
	}
	
	public void incSelectedStage(){
		if(selectedStage < circuit.size()-1)
			selectedStage++;
	}
	
	public void incSelectedQubit(){
		if(selectedQubit < circuit.getStage(selectedStage).getInternalSize()-1)
			selectedQubit++;
		else{
			selectedQubit = 0;
			incSelectedStage();
		}
			
	}
	
	public void setSelectedStage(int selectedStage) {
		this.selectedStage = selectedStage;
	}

	public void setEditable(boolean editable){
		this.editable = editable;
		if(!editable){
			this.removeMouseListener(this);
			this.removeMouseMotionListener(this);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		int heightZero = 30;
		int widthZero = 30;
		int height = this.getHeight() - heightZero*2;
		int width = this.getWidth() - widthZero*2;
		int gateSize = 25, gateHalf = gateSize/2;
		int hoverX = -1,hoverY = -1;
		@SuppressWarnings("unchecked")
		Vector<Integer>[] outputWireHeight = new Vector[circuit.size()];
		@SuppressWarnings("unchecked")
		Vector<Integer>[] inputWireHeight = new Vector[circuit.size()];
		for(int i = 0; i < circuit.size(); i++){
			//Draw each stage
			// 1. Get stage bounds
			int stageX1 = widthZero + (width*i)/circuit.size();
			int stageX2 = stageX1 + width/circuit.size()-50;
			g.drawString("S" + i, (stageX1+stageX2)/2, heightZero / 2);
			if(!editable && selectedStage == i){
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(stageX1, heightZero, stageX2-stageX1, height);
				g.setColor(Color.BLACK);
			}
			// 2. Draw stage qubits
			outputWireHeight[i] = new Vector<Integer>();
			inputWireHeight[i] = new Vector<Integer>();
			int stageSize = circuit.getStage(i).getInternalSize();
			for(int j = 0; j < stageSize; j++){
				int verticalWirePos = heightZero + (height/(stageSize + 1)) * (j + 1);
				if(isInside(x,y,(stageX1+stageX2)/2 - gateHalf - 15,verticalWirePos - gateHalf -15 , gateSize + 30, gateSize + 30)){
					hoverX = (stageX1+stageX2)/2 - gateHalf;
					hoverY = verticalWirePos - gateHalf;
					hoverStage = i;
					hoverQubit = j;
				}
				if(selectedStage == i && selectedQubit == j && editable)
					((Graphics2D) g).setStroke(new BasicStroke(2.0f));
				Position p = circuit.getStage(i).getPosition(j);
				switch(p.getContent()){
				case Stage.CONTENT_EMPTY:
					g.drawLine(stageX1, verticalWirePos, stageX2, verticalWirePos);
					outputWireHeight[i].add(verticalWirePos);
					inputWireHeight[i].add(verticalWirePos);
					break;
				case Stage.CONTENT_GATE:
				case Stage.CONTENT_CONTROLLED:
					if(p.getGate().getGateId() == Stage.GATE_TRACE){
						g.drawLine(stageX1, verticalWirePos, (stageX1+stageX2)/2 - gateHalf, verticalWirePos);
						inputWireHeight[i].add(verticalWirePos);
						g.drawRect((stageX1+stageX2)/2 - gateHalf, verticalWirePos - gateHalf, gateSize, gateSize);
						g.drawString(p.getGate().getSymbol(), (stageX1+stageX2)/2 - 5, verticalWirePos + 5);
					}else if(p.getGate().getGateId() == Stage.GATE_ADDQUBIT){
						g.drawLine((stageX1+stageX2)/2 + gateHalf, verticalWirePos, stageX2, verticalWirePos);
						g.drawRect((stageX1+stageX2)/2 - gateHalf, verticalWirePos - gateHalf, gateSize, gateSize);
						outputWireHeight[i].add(verticalWirePos);
						g.drawString(p.getGate().getSymbol(), (stageX1+stageX2)/2 - 5, verticalWirePos + 5);
					}else if(p.getGate().getGateId() == Stage.GATE_MEASURE){
						g.drawLine((stageX1+stageX2)/2 + gateHalf, verticalWirePos, stageX2, verticalWirePos);
						g.drawLine(stageX1, verticalWirePos, (stageX1+stageX2)/2 - gateHalf, verticalWirePos);
						outputWireHeight[i].add(verticalWirePos);
						inputWireHeight[i].add(verticalWirePos);
						g.drawRect((stageX1+stageX2)/2 - gateHalf, verticalWirePos - gateHalf, gateSize, gateSize);
						((Graphics2D) g).setStroke(new BasicStroke());
						g.drawArc((stageX1+stageX2)/2 - gateHalf + 2 , verticalWirePos - gateHalf/2, gateSize - 5, gateSize - 5, 0, 180);
			            g.drawLine((stageX1+stageX2)/2 - gateHalf/2, verticalWirePos + gateHalf/2, (stageX1+stageX2)/2 + gateHalf/2, verticalWirePos - gateHalf/2);
			            if(selectedStage == i && selectedQubit == j && editable)
							((Graphics2D) g).setStroke(new BasicStroke(2.0f));
					}else if(p.getGate().getGateId() == Stage.GATE_CIRCUIT){
						Circuit c = ((CircuitGate)p.getGate()).getCircuit();
						int output = c.getOutputSize();
						int input = c.getInputSize();
						int verticalLastWirePos = heightZero + (height/(stageSize + 1)) * (j + input);
						int circuitHeight = verticalLastWirePos - verticalWirePos + gateSize;
						String name = c.getName();
						for(int v = 0; v < name.length(); v++) 
							g.drawString(Character.toString(name.charAt(v)), (stageX1+stageX2)/2 - 5, verticalWirePos + 5 + 10*v);
						g.drawLine(stageX1, verticalWirePos, (stageX1+stageX2)/2 - gateHalf, verticalWirePos);
						inputWireHeight[i].add(verticalWirePos);
						if(p.getContent() == Stage.CONTENT_CONTROLLED)
							g.drawRoundRect((stageX1+stageX2)/2 - gateHalf, verticalWirePos - gateHalf, gateSize , circuitHeight,20,20);
						else
							g.drawRect((stageX1+stageX2)/2 - gateHalf, verticalWirePos - gateHalf, gateSize , circuitHeight);
						if(output == 1){
							int verticalOutputPos = verticalWirePos + ((verticalLastWirePos-verticalWirePos)/2);
							g.drawLine((stageX1+stageX2)/2 + gateHalf, verticalOutputPos, stageX2, verticalOutputPos);
							outputWireHeight[i].add(verticalOutputPos);
						}else{
							for(int w = 0; w < output; w++){
								if(w != 0)
									((Graphics2D) g).setStroke(new BasicStroke(1.0f));
								int verticalOutputPos = verticalWirePos + ((verticalLastWirePos-verticalWirePos)/(output-1))*w;
								g.drawLine((stageX1+stageX2)/2 + gateHalf, verticalOutputPos, stageX2, verticalOutputPos);
								outputWireHeight[i].add(verticalOutputPos);
							}
						}
						break;
					}else{
						g.drawLine((stageX1+stageX2)/2 + gateHalf, verticalWirePos, stageX2, verticalWirePos);
						g.drawLine(stageX1, verticalWirePos, (stageX1+stageX2)/2 - gateHalf, verticalWirePos);
						outputWireHeight[i].add(verticalWirePos);
						inputWireHeight[i].add(verticalWirePos);
						if(p.getContent() == Stage.CONTENT_GATE)
							g.drawRect((stageX1+stageX2)/2 - gateHalf, verticalWirePos - gateHalf, gateSize, gateSize);
						else
							g.drawRoundRect((stageX1+stageX2)/2 - gateHalf, verticalWirePos - gateHalf, gateSize, gateSize, 20, 20);
						g.drawString(p.getGate().getSymbol(), (stageX1+stageX2)/2 - 5, verticalWirePos + 5);
					}
					break;
				case Stage.CONTENT_CONTROL:
					g.drawLine(stageX1, verticalWirePos, stageX2, verticalWirePos);
					outputWireHeight[i].add(verticalWirePos);
					inputWireHeight[i].add(verticalWirePos);
					g.fillOval((stageX1+stageX2)/2 - 5, verticalWirePos - 5, 10, 10);
					break;
				case Stage.CONTENT_FULL:
					g.drawLine(stageX1, verticalWirePos, (stageX1+stageX2)/2 - gateHalf, verticalWirePos);
					inputWireHeight[i].add(verticalWirePos);
					break;
				}
				((Graphics2D) g).setStroke(new BasicStroke());
			}
		}
		//3. Draw transition zone
		for(int i = 0; i < circuit.size() - 1; i++){
			// 4. Get transition zone bounds
			int transX1 = widthZero + (width*i)/circuit.size() + width/circuit.size()-50;
			int transX2 = transX1 + 50;
			if(inputWireHeight[i+1].size() != outputWireHeight[i].size()){
				g.setColor(Color.RED);
				g.drawLine(transX1, heightZero, transX2, heightZero + height);
				g.drawLine(transX2, heightZero, transX1, heightZero + height);
				g.setColor(Color.BLACK);
			}else{
				for(int w = 0; w < outputWireHeight[i].size(); w++){
					g.drawLine(transX1, outputWireHeight[i].get(w), transX2, inputWireHeight[i+1].get(w));
				}
			}
		}
		//4. Draw hover zone
		if(hoverX != -1 && editable){
			((Graphics2D)g).setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, new float[]{1}, 0));
			g.drawRect(hoverX-2, hoverY-2, gateSize + 4, gateSize + 4);
		}
		//5. Draw input
		if(input != null && !editable){
			for(int i = inputWireHeight[0].size()-1; i>=0; i--){
				g.drawString(String.valueOf(input[i]), widthZero/2 ,inputWireHeight[0].get(i));
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		x = arg0.getX();
		y = arg0.getY();
		repaint();
	}
	
	private boolean isInside(int x, int y, int rectX, int rectY, int rectWidth, int rectHeight){
		return (x >= rectX && y >= rectY && x < rectX + rectWidth && y < rectY + rectHeight);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(controller != null){
			selectedStage = hoverStage;
			selectedQubit = hoverQubit;
			controller.selectionChanged();
			repaint();
			if(arg0.getButton() == MouseEvent.BUTTON1){
				if(arg0.getClickCount() == 2){
					controller.circuitDoubleClick();
				}
			}else if(arg0.getButton() == MouseEvent.BUTTON3){
				popupMenu.show(this, arg0.getX(), arg0.getY());
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	public void setInput(byte[] input) {
		this.input = input;
	}
	
	public class PopupPanel extends JPanel{
		
		private static final long serialVersionUID = 579473073079951333L;
		
		public PopupPanel(){
			super();
			this.setPreferredSize(new Dimension(200, 100));
			this.setBackground(Color.LIGHT_GRAY);
		}

		@Override
		protected void paintComponent(Graphics arg0) {
			super.paintComponent(arg0);
			if(selectedStage > -1 && selectedQubit > -1){
				Position p = circuit.getStage(selectedStage).getPosition(selectedQubit);
				if(p.getContent() == Stage.CONTENT_GATE)
					arg0.drawString(p.getGate().getSymbol(), 0, 0);
			}
		}
		
	}

}
