package qmips.devices.registerFile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import qmips.others.LogicVector;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * 
 * Interfaz grafica simple para el fichero de registros.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class RegisterFileDisplay extends JPanel implements RegisterFile.Display {
	
	private static final long serialVersionUID = -5224894535840510807L;
	private JLabel selRegA;
	private JLabel selRegB;
	private JList<String> list;
	private String[] elems;
	
	public RegisterFileDisplay() {
		setSize(400, 430);
		setLayout(new BorderLayout(0, 0));

		elems = new String[32];
		for (int i = 0; i < 32; i++)
			elems[i] = "R" + i + ": " + new LogicVector(32).toString();
		
		list = new JList<String>(elems);
		list.setVisibleRowCount(32);
		list.setBorder(new LineBorder(new Color(0, 0, 0)));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(list);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.WEST);

		JPanel panel_1 = new JPanel();
		panel_1.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panel_1.setLayout(new BorderLayout(0, 0));

		JLabel lblSelectedRegisterA = new JLabel("Selected register A");
		lblSelectedRegisterA.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectedRegisterA.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_1.add(lblSelectedRegisterA, BorderLayout.NORTH);

		selRegA = new JLabel("0");
		selRegA.setFont(new Font("Tahoma", Font.BOLD, 18));
		selRegA.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(selRegA);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("center:135px"), }, new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC, RowSpec.decode("39px"),
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));
		panel.add(panel_1, "2, 2, left, top");

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(new BorderLayout(0, 0));

		JLabel lblSelectedRegisterB = new JLabel("Selected register B");
		lblSelectedRegisterB.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectedRegisterB.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_2.add(lblSelectedRegisterB, BorderLayout.NORTH);

		selRegB = new JLabel("0");
		selRegB.setFont(new Font("Tahoma", Font.BOLD, 18));
		selRegB.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(selRegB);
		panel.add(panel_2, "2, 4, left, top");
	}

	@Override
	public void setSelectedA(int iselA) {
		selRegA.setText(String.valueOf(iselA));
	}

	@Override
	public void setSelectedB(int iselB) {
		selRegB.setText(String.valueOf(iselB));
	}

	@Override
	public void write(int reg, LogicVector value) {
		elems[reg] = "R" + reg + ": " + value.toString();
		list.repaint();
	}

	public JLabel getSelRegA() {
		return selRegA;
	}

	public JLabel getSelRegB() {
		return selRegB;
	}

	public JList<String> getRegList() {
		return list;
	}

	@Override
	public void reset() {
		elems = new String[32];
		for (int i = 0; i < 32; i++)
			elems[i] = "R" + i + ": " + new LogicVector(32).toString();
		repaint();
	}
}
