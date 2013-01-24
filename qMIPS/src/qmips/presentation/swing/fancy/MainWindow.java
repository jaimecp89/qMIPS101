package qmips.presentation.swing.fancy;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import qmips.presentation.builders.Builder;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JDesktopPane;
import java.awt.Color;
import javax.swing.JMenu;
import javax.swing.JButton;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1148004565534821823L;
	private JPanel contentPane;

	
	public MainWindow(Builder b) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 663, 524);
		
		JMenuBar mainMenu = new JMenuBar();
		setJMenuBar(mainMenu);
		
		JMenu mnFile = new JMenu("File");
		mainMenu.add(mnFile);
		
		JMenu mnSystem = new JMenu("System");
		mainMenu.add(mnSystem);
		
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
		simToolBar.add(btnRunOneCycle);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JTextArea logArea = new JTextArea();
		logArea.setForeground(Color.LIGHT_GRAY);
		logArea.setEditable(false);
		splitPane.setRightComponent(logArea);
		
		JDesktopPane mainPane = new JDesktopPane();
		mainPane.setBackground(Color.LIGHT_GRAY);
		splitPane.setLeftComponent(mainPane);
		
		setVisible(true);
		splitPane.setDividerLocation(0.8);
	}
}
