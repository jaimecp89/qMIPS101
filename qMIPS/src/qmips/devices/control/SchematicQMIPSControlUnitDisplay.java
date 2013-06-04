package qmips.devices.control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SchematicQMIPSControlUnitDisplay extends JPanel implements IControlUnitDisplay{
	
	private static final long serialVersionUID = 1874419105761208518L;
	
	private Image ife, id, qt, qex, qmea, mac, mar, mrc, imm, rew, exe, rc, jrf, jrc, bc, jc, jal, mfhi;
	private Image current;
	private JPanel imgPanel;
	private JLabel lblDescription;
	
	public SchematicQMIPSControlUnitDisplay() {
		setLayout(new BorderLayout(0, 0));
		
		lblDescription = new JLabel("IF: Instruction fetch");
		add(lblDescription, BorderLayout.SOUTH);
		
		imgPanel = new ImageViewer();
		imgPanel.setBackground(Color.WHITE);
		add(imgPanel, BorderLayout.CENTER);
		
		ife = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_if.png")).getImage();
		id = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_id.png")).getImage();
		qt = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_qt.png")).getImage();
		qex = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_qex.png")).getImage();
		qmea = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_qmea.png")).getImage();
		mac = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_mac.png")).getImage();
		mar = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_mar.png")).getImage();
		mrc = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_mrc.png")).getImage();
		imm = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_imm.png")).getImage();
		rew = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_rew.png")).getImage();
		exe = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_exe.png")).getImage();
		rc = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_rc.png")).getImage();
		jrf = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_jrf.png")).getImage();
		jrc = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_jrc.png")).getImage();
		bc = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_bc.png")).getImage();
		jc = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_jc.png")).getImage();
		jal = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_jal.png")).getImage();
		mfhi = new ImageIcon(SchematicQMIPSControlUnitDisplay.class.getResource("/qmips/devices/control/qMIPSControlImgs/fase_mfhi.png")).getImage();
		
		this.setSize(800, 600);
		this.setPreferredSize(new Dimension(500, 300));
	}

	@Override
	public void setState(String state) {
		if(state.equals("IF")){
			current = ife;
		}else if(state.equals("ID")){
			current = id;
		}else if(state.equals("QT")){
			current = qt;
		}else if(state.equals("QEX")){
			current = qex;
		}else if(state.equals("QMEA")){
			current = qmea;
		}else if(state.equals("MAC")){
			current = mac;
		}else if(state.equals("MAR")){
			current = mar;
		}else if(state.equals("MRC")){
			current = mrc;
		}else if(state.equals("IMM")){
			current = imm;
		}else if(state.equals("REW")){
			current = rew;
		}else if(state.equals("EXE")){
			current = exe;
		}else if(state.equals("RC")){
			current = rc;
		}else if(state.equals("JRF")){
			current = jrf;
		}else if(state.equals("JRC")){
			current = jrc;
		}else if(state.equals("BC")){
			current = bc;
		}else if(state.equals("JC")){
			current = jc;
		}else if(state.equals("JAL")){
			current = jal;
		}else if(state.equals("MFHI")){
			current = mfhi;
		}
		imgPanel.repaint();
	}

	@Override
	public void setDescription(String description) {
		lblDescription.setText(description);
	}

	@Override
	public void setState(String state, String description) {
		setState(state);
		lblDescription.setText(state + ": " + description);
	}
	
	class ImageViewer extends JPanel{

		private static final long serialVersionUID = -1982314683516932644L;
		
		public void paint(Graphics g){
			g.drawImage(current, 0, 0, this.getWidth(), this.getHeight(), null);
		}
		
	}

	
}
