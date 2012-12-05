package qmips.presentation.swing;

import javax.swing.JInternalFrame;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import java.awt.Font;

public class SourceCodeFrame extends JInternalFrame{
	public SourceCodeFrame() {
		setTitle("Source");
		setMaximizable(true);
		setIconifiable(true);
		
		srcPane = new JTextPane();
		srcPane.setFont(new Font("Lucida Console", Font.PLAIN, 15));
		srcPane.setEditable(false);
		getContentPane().add(srcPane, BorderLayout.CENTER);
		setSize(390, 480);
		setVisible(true);
	}

	private static final long serialVersionUID = 3575723225697373857L;
	private JTextPane srcPane;

	public JTextPane getSrcPane() {
		return srcPane;
	}
}
