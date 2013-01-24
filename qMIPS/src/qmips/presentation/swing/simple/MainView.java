package qmips.presentation.swing.simple;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import qmips.compiler.MIPSCompiler;
import qmips.devices.Device;
import qmips.devices.clock.Clock;
import qmips.devices.control.ControlUnit;
import qmips.devices.memory.IMemory;
import qmips.others.Bus;
import qmips.presentation.builders.Builder;
import qmips.sync.SyncShortcut;

public class MainView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8281732341404064874L;
	private JPanel contentPane;
	private JFrame self;
	private Builder builder;
	private IMemory mem;
	private Clock clk;
	private Bus rst;
	private ControlUnit control;
	private Map<String, JInternalFrame> frameMap;
	private JDesktopPane desktopPane;
	private JMenu mnDevices;
	private JTextField numCyclesText;
	private JScrollPane scrollPane;

	/**
	 * Create the frame.
	 */
	public MainView(Builder b) {
		this.self = this;
		this.builder = b;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 864, 779);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmLoadSourceAndReset = new JMenuItem(
				"Load source and reset...");
		mntmLoadSourceAndReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System
						.getProperty("user.dir") + "//programs//"));
				int retVal = fileChooser.showOpenDialog(self);
				if (retVal == JFileChooser.APPROVE_OPTION) {
					builder.build();
					mem = builder.getInstrMemory();
					clk = builder.getClock();
					rst = builder.getResetBus();
					control = builder.getControlUnit();
					displayDevices();
					frameMap.put("Clock", clk.getDisplay());
					getMainPane().add(clk.getDisplay());
					clk.startRunning();
					File f = fileChooser.getSelectedFile();
					SourceCodeFrame scf = new SourceCodeFrame();
					BufferedReader bfr = null;
					try {
						bfr = new BufferedReader(new FileReader(f));
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					String src = "", line = "";
					try {
						while ((line = bfr.readLine()) != null)
							src += line + "\n";
					} catch (IOException e) {
						e.printStackTrace();
					}
					scf.getSrcPane().setText(src);
					frameMap.put("Source", scf);
					getMainPane().add(scf);
					MIPSCompiler.compile(f, mem);
					arrangeFrames();
				}
			}
		});

		mnFile.add(mntmLoadSourceAndReset);

		mnDevices = new JMenu("Devices");
		menuBar.add(mnDevices);
		
		JMenu mnFrames = new JMenu("Frames");
		menuBar.add(mnFrames);
		
		JMenuItem mntmArrangeFrames = new JMenuItem("Arrange frames");
		mntmArrangeFrames.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				arrangeFrames();
			}
		});
		mnFrames.add(mntmArrangeFrames);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JToolBar toolBar = new JToolBar();
		toolBar.setRollover(true);
		toolBar.setFloatable(false);
		contentPane.add(toolBar, BorderLayout.NORTH);

		JButton btnRunOneCycle = new JButton("Run one cycle");
		btnRunOneCycle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (control.checkTrap() == -1) {
					clk.runCycles(1);
				}else{
					JOptionPane.showMessageDialog(self,
							"Program terminated with code: " + control.checkTrap(),
							"Terminated", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		toolBar.add(btnRunOneCycle);

		JButton btnRunCycles = new JButton("Run cycles...");
		btnRunCycles.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int num = 0;
				boolean err = false;
				try {
					num = Integer.parseInt(getNumCyclesText().getText());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(self,
							"Number of cycles must be an integer.", "Error",
							JOptionPane.ERROR_MESSAGE);
					err = true;
				}
				if (!err) {
					while (control.checkTrap() == -1 && num > 0) {
						clk.runCycles(1);
						num--;
					}
					if (control.checkTrap() != -1)
						JOptionPane.showMessageDialog(
								self,
								"Program terminated with code: "
										+ control.checkTrap(), "Terminated",
								JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		toolBar.add(btnRunCycles);

		numCyclesText = new JTextField();
		numCyclesText.setHorizontalAlignment(SwingConstants.TRAILING);
		numCyclesText.setText("100");
		toolBar.add(numCyclesText);
		numCyclesText.setColumns(10);

		JButton btnRunUntilTrap = new JButton("Run until trap");
		btnRunUntilTrap.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				while (control.checkTrap() == -1) {
					clk.runCycles(1);
				}
				JOptionPane.showMessageDialog(self,
						"Program terminated with code: " + control.checkTrap(),
						"Terminated", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		toolBar.add(btnRunUntilTrap);

		JButton btnReset = new JButton("Reset");
		btnReset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("Reset!!");
				rst.write(1,1);
				SyncShortcut.sync.taskEnded();
				synchronized(SyncShortcut.sync){
					SyncShortcut.sync.clockLockWait();
					rst.write(0,1);
				}
				clk.setCycleCount(0);
			}
		});
		toolBar.add(btnReset);
		desktopPane = new JDesktopPane();
		desktopPane.setPreferredSize(getSize());
		scrollPane = new JScrollPane(desktopPane);
		
		contentPane.add(scrollPane, BorderLayout.CENTER);
		desktopPane.setBackground(Color.LIGHT_GRAY);

		setVisible(true);
	}

	private void displayDevices() {
		Map<String, Device> disp = builder.getDisplayableDevices();
		frameMap = new TreeMap<String, JInternalFrame>();
		getMainPane().removeAll();
		for (Entry<String, Device> e : disp.entrySet()) {
			if (e.getValue().display() != null) {
				JInternalFrame ji = createInternalFrame(e.getKey(), e
						.getValue().display());
				frameMap.put(e.getKey(), ji);
				ji.setVisible(true);
				JMenuItem devItem = new JMenuItem(e.getKey());
				devItem.addMouseListener(new DeviceListMouseAdapter(e.getKey()));
				getMenuDevices().add(devItem);
			}
		}
		repaint();
	}

	class DeviceListMouseAdapter extends MouseAdapter {

		String name;

		DeviceListMouseAdapter(String name) {
			this.name = name;
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			JInternalFrame ji = frameMap.get(name);
			ji.setVisible(!ji.isVisible());
		}
	}

	public JDesktopPane getMainPane() {
		return desktopPane;
	}

	private JInternalFrame createInternalFrame(String name, JPanel panel) {
		JInternalFrame ji = new JInternalFrame();
		getMainPane().add(ji);
		ji.setSize(panel.getSize().width + 30, panel.getSize().height + 30);
		ji.setMaximizable(true);
		ji.setResizable(true);
		ji.setTitle(name);
		ji.setContentPane(panel);
		return ji;
	}

	public JMenu getMenuDevices() {
		return mnDevices;
	}

	public JTextField getNumCyclesText() {
		return numCyclesText;
	}

	private void arrangeFrames() {
		TreeSet<JInternalFrame> ts = new TreeSet<JInternalFrame>(new Comparator<JInternalFrame>(){
			@Override
			public int compare(JInternalFrame arg0, JInternalFrame arg1) {
				return arg0.getHeight() - arg1.getHeight();
			}
		});
		ts.addAll(frameMap.values());
		int acumHeight = 0, acumWidth = 0;
		int maxWidth = 0, maxHeight = 0;
		int rowMaxWidth = 0;
		System.out.println("Tamaño del panel: " + getScrollPane().getSize());
		for(JInternalFrame ji : ts){
			ji.setBounds(acumWidth, acumHeight, ji.getWidth(), ji.getHeight());
			acumHeight += ji.getHeight();
			if(ji.getWidth() > rowMaxWidth)
				rowMaxWidth = ji.getWidth();
			if(acumHeight > getMainPane().getHeight()){
				maxWidth += rowMaxWidth;
				if(acumHeight > maxHeight)
					maxHeight = acumHeight;
				acumWidth += rowMaxWidth;
				acumHeight = 0;
			}
		}
		maxWidth+=rowMaxWidth;
		System.out.println(maxWidth + " ," + maxHeight);
		getMainPane().setPreferredSize(new Dimension(maxWidth, maxHeight));
	}
	
	class ScrollableJDesktopPane extends JDesktopPane implements Scrollable{

		private static final long serialVersionUID = -6212698582992034960L;

		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return super.getPreferredSize();
		}

		@Override
		public int getScrollableBlockIncrement(Rectangle arg0, int arg1,
				int arg2) {
			return 200;
		}

		@Override
		public boolean getScrollableTracksViewportHeight() {
			return false;
		}

		@Override
		public boolean getScrollableTracksViewportWidth() {
			return false;
		}

		@Override
		public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
			return 50;
		}
		
	}
	
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
}
