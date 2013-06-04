package qmips.presentation.swing.fancy;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import qmips.compiler.Instruction;

public class ProgramProgressView extends JPanel{

	private static final long serialVersionUID = -978334991632200276L;
	private Map<Integer, Integer> lineToPC;
	private JTable table;
	
	public ProgramProgressView(Map<Integer, Instruction> instructions){
		
		lineToPC = new HashMap<Integer, Integer>();
		setLayout(new BorderLayout(0, 0));
		
		String[] headers = new String[]{"Address", "Operation", "Param1","Param2", "Param3"};
		String[][] sInst = new String[instructions.size()][6];
		int i = 0;
		for(Entry<Integer, Instruction> e : instructions.entrySet()){
			sInst[i][0] = new Integer(e.getKey()).toString() + (e.getValue().getLabel() != null? ": " + e.getValue().getLabel() : "") ;
			sInst[i][1] = e.getValue().getOperation();
			for(int x = 0; x < e.getValue().getParameters().length; x++){
				sInst[i][x+2] = e.getValue().getParameters()[x];
			}
			lineToPC.put(e.getKey(), i);
			i++;
		}
		
		JScrollPane scroll = new JScrollPane();
		add(scroll);
		
		table = new JTable(sInst, headers);
		table.setEnabled(false);
		scroll.setViewportView(table);
	}

	protected JTable getTable() {
		return table;
	}
	
	public void setProgramCounter(int programCounter){
		if(lineToPC.containsKey(programCounter)){
			int i = lineToPC.get(programCounter);
			table.setRowSelectionInterval(i, i);
			repaint();
		}else{
			table.getSelectionModel().removeSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
		}
	}
	
}
