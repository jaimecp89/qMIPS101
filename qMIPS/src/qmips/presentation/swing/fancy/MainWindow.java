package qmips.presentation.swing.fancy;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import qmips.devices.Device;
import qmips.presentation.builders.Builder;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JDesktopPane;
import java.awt.Color;
import javax.swing.JMenu;
import javax.swing.JButton;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.Map;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JEditorPane;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

public class MainWindow extends JFrame{

	private static final long serialVersionUID = 1148004565534821823L;
	private JPanel contentPane;
	private JSplitPane splitPane;
	private Controller controller;
	private JTextField textField;
	private JDesktopPane mainPane;

	
	public MainWindow(Controller contr) {
		this.controller = contr;
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				if(splitPane != null)
					splitPane.setDividerLocation(0.8);
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 663, 524);
		
		JMenuBar mainMenu = new JMenuBar();
		setJMenuBar(mainMenu);
		
		JMenu mnFile = new JMenu("File");
		mainMenu.add(mnFile);
		
		JMenuItem mntmLoadSource = new JMenuItem("Load source...");
		mnFile.add(mntmLoadSource);
		
		JMenuItem mntmBuildAndLoad = new JMenuItem("Build and load source...");
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
		
		JMenu mnAbout = new JMenu("About");
		mainMenu.add(mnAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JToolBar simToolBar = new JToolBar();
		simToolBar.setFloatable(false);
		contentPane.add(simToolBar, BorderLayout.NORTH);
		
		JButton btnRunOneCycle = new JButton("Run One Cycle");
		btnRunOneCycle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.runOneCycle();
			}
		});
		simToolBar.add(btnRunOneCycle);
		
		JButton btnRunUntilTrap = new JButton("Run Until Trap");
		btnRunUntilTrap.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.runUntilTrap();
			}
		});
		simToolBar.add(btnRunUntilTrap);
		
		JButton btnRun = new JButton("Run...");
		simToolBar.add(btnRun);
		
		textField = new JTextField();
		simToolBar.add(textField);
		textField.setHorizontalAlignment(SwingConstants.TRAILING);
		textField.setText("5");
		textField.setColumns(10);
		
		JLabel lblCycles = new JLabel(" cycles");
		simToolBar.add(lblCycles);
		
		splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);
		setVisible(true);
		splitPane.setDividerLocation(0.8);
		
		JScrollPane scrollPane2 = new JScrollPane();
		splitPane.setLeftComponent(scrollPane2);
		
		mainPane = new JDesktopPane();
		mainPane.setBackground(Color.LIGHT_GRAY);
		scrollPane2.setViewportView(mainPane);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
		JTextPane logArea = new JTextPane();
		scrollPane.add(logArea);
		scrollPane.setViewportView(logArea);
		logArea.setBackground(Color.WHITE);
		
		Log.init(logArea);
		
		
	}
	
	public void displayDevicesViews(Map<String, Device> views){
		for(Map.Entry<String, Device> view : views.entrySet()){
			JInternalFrame jif = new JInternalFrame();
			JPanel vp = view.getValue().display();
			jif.setSize(vp.getWidth()+30, vp.getHeight()+30);
			jif.setMaximizable(true);
			jif.setResizable(true);
			jif.setTitle(view.getKey());
			jif.setContentPane(vp);
			mainPane.add(jif);
			jif.setVisible(true);
		}
	}
	
	interface Controller{
		
		void runOneCycle();
		
		void runCycles(int cycles);
		
		void runUntilTrap();
		
		void resetSignal();
		
		void buildSystem();
		
		void dismountSystem();
		
		void loadSource(File file, int start);
		
		void buildAndLoadSource(File file, int start);
		
	}

}
