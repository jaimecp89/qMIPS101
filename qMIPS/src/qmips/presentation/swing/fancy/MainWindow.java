package qmips.presentation.swing.fancy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import qmips.compiler.CompilationResults;
import qmips.compiler.Instruction;
import qmips.presentation.swing.fancy.onyxbits.Usher;

/**
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1148004565534821823L;
	private JPanel contentPane;
	private JSplitPane splitPane;
	private Controller controller;
	private JDesktopPane mainPane;
	private JDialog infoView;
	private JWindow cyclesPopUp;
	private JButton btnRun;
	private JTextField cyclesText;
	private ProgramProgressView ppv;
	private Map<String, JPanel> shownDevices;
	

	public MainWindow(Controller contr) {
		this.controller = contr;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/qmips/icons/qMIPSIcon.png")));
		setTitle("qMIPS Simulator");
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent arg0) {
				cyclesPopUp.setVisible(false);
			}
			
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				cyclesPopUp.setVisible(false);
			}
		});

		JMenuBar mainMenu = new JMenuBar();
		setJMenuBar(mainMenu);

		JMenu mnFile = new JMenu("File");
		mainMenu.add(mnFile);

		JMenuItem mntmLoadSource = new JMenuItem("Load source...");
		mntmLoadSource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File f;
				if ((f = askForFile()) != null){
					CompilationResults res = controller.loadSource(f, 0);
					if(res != null){
						createInternalSourceFrames(f, res.getInstructions());
					}
				}	
			}
		});
		mnFile.add(mntmLoadSource);

		JMenuItem mntmBuildAndLoad = new JMenuItem("Build and load source...");
		mntmBuildAndLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File f;
				if ((f = askForFile()) != null){
					CompilationResults res = controller.buildAndLoadSource(f, 0);
					if(res != null){
						createInternalSourceFrames(f, res.getInstructions());
					}
				}
			}
		});
		mnFile.add(mntmBuildAndLoad);

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenuItem mntmQuit = new JMenuItem("Quit");
		mnFile.add(mntmQuit);

		JMenu mnSystem = new JMenu("System");
		mainMenu.add(mnSystem);

		JMenuItem mntmBuildSystem = new JMenuItem("Build system");
		mntmBuildSystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.buildSystem();
			}
		});
		mnSystem.add(mntmBuildSystem);

		JMenu mnSimulation = new JMenu("Simulation");
		mainMenu.add(mnSimulation);
		
		JMenuItem mntmRunOneCycle = new JMenuItem("Run one cycle");
		mntmRunOneCycle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.runOneCycle();
			}
		});
		mnSimulation.add(mntmRunOneCycle);
		
		JMenuItem mntmRunUntilTrap = new JMenuItem("Run until trap");
		mntmRunUntilTrap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.runUntilTrap();
			}
		});
		mnSimulation.add(mntmRunUntilTrap);
		
		JMenuItem mntmRunCycles = new JMenuItem("Run cycles...");
		mntmRunCycles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cyclesPopUp.setVisible(!cyclesPopUp.isVisible());
				cyclesPopUp.setLocation((int)btnRun.getLocationOnScreen().getX(), (int)btnRun.getLocationOnScreen().getY() + btnRun.getHeight());
			}
		});
		mnSimulation.add(mntmRunCycles);
		
		mnSimulation.addSeparator();
		
		JMenuItem mntmResetSignal = new JMenuItem("Reset signal");
		mntmResetSignal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.resetSignal();
			}
		});
		mnSimulation.add(mntmResetSignal);
		
		mnSimulation.addSeparator();
		
		JMenuItem mntmContinue = new JMenuItem("Continue");
		mntmContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.releaseTrap();
			}
		});
		mnSimulation.add(mntmContinue);

		JMenu mnHelp = new JMenu("Help");
		mainMenu.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JToolBar simToolBar = new JToolBar();
		simToolBar.setFloatable(false);
		contentPane.add(simToolBar, BorderLayout.NORTH);

		JButton btnRunOneCycle = new JButton("");
		btnRunOneCycle.setToolTipText("Run one cycle");
		btnRunOneCycle.setIcon(new ImageIcon(MainWindow.class.getResource("/qmips/icons/play_one.png")));
		btnRunOneCycle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.runOneCycle();
			}
		});
		
		JButton btnLoadSource = new JButton("");
		btnLoadSource.setToolTipText("Load source...");
		btnLoadSource.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				File f;
				if ((f = askForFile()) != null){
					CompilationResults res = controller.loadSource(f, 0);
					if(res != null){
						createInternalSourceFrames(f, res.getInstructions());
					}
				}
			}
		});
		btnLoadSource.setIcon(new ImageIcon(MainWindow.class.getResource("/qmips/icons/load_source.png")));
		simToolBar.add(btnLoadSource);
		
		JButton btnBuildAndLoad = new JButton("");
		btnBuildAndLoad.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				File f;
				if ((f = askForFile()) != null){
					CompilationResults res = controller.buildAndLoadSource(f, 0);
					if(res != null){
						createInternalSourceFrames(f, res.getInstructions());
					}
				}
			}
		});
		btnBuildAndLoad.setIcon(new ImageIcon(MainWindow.class.getResource("/qmips/icons/build_load_source.png")));
		btnBuildAndLoad.setToolTipText("Build system and load source...");
		simToolBar.add(btnBuildAndLoad);
		
		simToolBar.addSeparator();
		
		JButton btnBuildSystem = new JButton("");
		btnBuildSystem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.buildSystem();
			}
		});
		btnBuildSystem.setIcon(new ImageIcon(MainWindow.class.getResource("/qmips/icons/build.png")));
		btnBuildSystem.setToolTipText("Build system");
		simToolBar.add(btnBuildSystem);
		
		simToolBar.addSeparator();
		
		simToolBar.add(btnRunOneCycle);

		JButton btnRunUntilTrap = new JButton("");
		btnRunUntilTrap.setToolTipText("Run Until Trap");
		btnRunUntilTrap.setIcon(new ImageIcon(MainWindow.class.getResource("/qmips/icons/play_trap.png")));
		btnRunUntilTrap.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.runUntilTrap();
			}
		});
		simToolBar.add(btnRunUntilTrap);
		
		cyclesPopUp = new JWindow(this);
		JPanel panel = new JPanel();
		cyclesPopUp.getContentPane().add(panel);
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel("Number of cycles: "), BorderLayout.WEST);
		cyclesText = new JTextField("1000", 10);
		cyclesText.setEditable(true);
		JButton runForAccept = new JButton("Accept");
		runForAccept.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				boolean err = false;
				int numCycles = 0;
				try{
					numCycles = Integer.parseInt(cyclesText.getText());
				}catch(Exception e){
					err = true;
					Log.err.println("Incorrect number of cycles");
				}	
				if(!err){
					controller.runCycles(numCycles);
					cyclesPopUp.setVisible(false);
				}
			}
		});
		panel.add(cyclesText, BorderLayout.CENTER);
		panel.add(runForAccept, BorderLayout.EAST);
		panel.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED), new EmptyBorder(10, 10, 10, 10)));
		cyclesPopUp.pack();
		cyclesPopUp.setSize(400, 50);
		
		btnRun = new JButton("");
		btnRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				cyclesPopUp.setVisible(!cyclesPopUp.isVisible());
				cyclesPopUp.setLocation((int)btnRun.getLocationOnScreen().getX(), (int)btnRun.getLocationOnScreen().getY() + btnRun.getHeight());
			}
		});
		
		btnRun.setIcon(new ImageIcon(MainWindow.class.getResource("/qmips/icons/play_cycles.png")));
		btnRun.setToolTipText("Run for...");
		simToolBar.add(btnRun);
		
		JButton btnResetSignal = new JButton("");
		btnResetSignal.setToolTipText("Reset signal");
		btnResetSignal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.resetSignal();
			}
		});
		btnResetSignal.setIcon(new ImageIcon(MainWindow.class.getResource("/qmips/icons/reset.png")));
		simToolBar.add(btnResetSignal);
		
		JButton btnContinue = new JButton("");
		btnContinue.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.releaseTrap();
			}
		});
		btnContinue.setIcon(new ImageIcon(MainWindow.class.getResource("/qmips/icons/continue.png")));
		simToolBar.add(btnContinue);
		
		simToolBar.addSeparator();
		
		JButton btnArrangewindows = new JButton("");
		btnArrangewindows.setToolTipText("Arrange windows");
		btnArrangewindows.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				arrangeInternalFrames();
			}
		});
		btnArrangewindows.setIcon(new ImageIcon(MainWindow.class.getResource("/qmips/icons/arrange.png")));
		simToolBar.add(btnArrangewindows);

		splitPane = new JSplitPane();
		splitPane.setResizeWeight(1.0);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);
		setVisible(true);
		splitPane.setDividerLocation(0.8);

		JScrollPane scrollPane2 = new JScrollPane();
		splitPane.setLeftComponent(scrollPane2);

		mainPane = new JDesktopPane();
		mainPane.setBackground(Color.LIGHT_GRAY);
		scrollPane2.setViewportView(mainPane);
		mainPane.addContainerListener(new Usher());

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);

		JTextPane logArea = new JTextPane();
		scrollPane.setViewportView(logArea);
		logArea.setBackground(Color.WHITE);

		Log.init(logArea);

	}

	public void displayDevicesViews(Map<String, JPanel> views) {
		if(shownDevices == null)
			shownDevices = new HashMap<String, JPanel>();
		else
			hideDevicesViews();
		for (Map.Entry<String, JPanel> view : views.entrySet()) {
			JInternalFrame jif = new JInternalFrame();
			JPanel vp = view.getValue();
			jif.setMaximizable(true);
			jif.setIconifiable(true);
			jif.setResizable(true);
			jif.setTitle(view.getKey());
			jif.setContentPane(vp);
			jif.pack();
			mainPane.add(jif);
			shownDevices.put(view.getKey(), view.getValue());
			jif.setVisible(true);
		}
	}
	
	public void hideDevicesViews(){
		for(JInternalFrame jif: mainPane.getAllFrames()){
			jif.setVisible(false);
			jif.dispose();
		}
		shownDevices.clear();
	}

	public void displayModalInfo(String msg, boolean showStop) {
		infoView = new JDialog(this, true);
		JLabel lbl = new JLabel(msg);
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		JButton stop = new JButton("Stop");
		stop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.killSimulationThread();
			}
		});
		JPanel pn = new JPanel();
		pn.setSize(150, 50);
		pn.setLayout(new BorderLayout());
		pn.add(lbl, BorderLayout.CENTER);
		if (showStop)
			pn.add(stop, BorderLayout.SOUTH);
		infoView.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		infoView.setSize(350, 200);
		infoView.setResizable(false);
		infoView.setContentPane(pn);
		infoView.setLocationRelativeTo(this);
		infoView.setVisible(true);
	}

	public void hideModalInfo() {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				infoView.setVisible(false);
				infoView.dispose();
			}
			
		});
		
	}

	public File askForFile() {
		File res;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")
				+ "//programs//"));
		int retVal = fileChooser.showOpenDialog(contentPane);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			res = fileChooser.getSelectedFile();
		} else {
			res = null;
		}
		return res;
	}
	
	private void arrangeInternalFrames(){
		Map<String, JPanel> aux = new HashMap<String, JPanel>();
		if(shownDevices!= null)
			if(!shownDevices.isEmpty()){
				for(Map.Entry<String, JPanel> view : shownDevices.entrySet()){
					aux.put(view.getKey(), view.getValue());
				}
				hideDevicesViews();
				displayDevicesViews(aux);
			}
	}
	
	private void createInternalSourceFrames(File f, Map<Integer, Instruction> instructions){
		JInternalFrame jif = new JInternalFrame();
		JPanel vp = new SourceCodeView(f, controller);
		jif.setMaximizable(true);
		jif.setIconifiable(true);
		jif.setResizable(true);
		jif.setTitle(f.getAbsolutePath());
		jif.setContentPane(vp);
		jif.setSize(300, 400);
		mainPane.add(jif);
		shownDevices.put("SourceView", vp);
		jif.setVisible(true);
		
		JInternalFrame jif2 = new JInternalFrame();
		ppv = new ProgramProgressView(instructions);
		jif2.setMaximizable(true);
		jif2.setIconifiable(true);
		jif2.setResizable(true);
		jif2.setTitle("Program progress view");
		jif2.setContentPane(ppv);
		jif2.setSize(300, 400);
		mainPane.add(jif2);
		shownDevices.put("ProgramProgressView", ppv);
		jif2.setVisible(true);
	}
	
	public ProgramProgressView getProgramProgressView(){
		return ppv;
	}

	interface Controller {

		void runOneCycle();

		void releaseTrap();

		void runCycles(int cycles);

		void runUntilTrap();

		void resetSignal();

		void buildSystem();

		void dismountSystem();

		CompilationResults loadSource(File file, int start);

		CompilationResults buildAndLoadSource(File file, int start);

		void killSimulationThread();

	}
}
