package qmips.presentation.swing.fancy;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

public class SourceCodeView extends JPanel {
	
	
	private static final long serialVersionUID = -7918129438227614285L;
	
	private JEditorPane editorPane;
	private JButton btnSave;
	private JButton btnReset;
	
	private String originalText;
	private File file;
	private MainWindow.Controller contr;
	private boolean modified = false;
	
	public SourceCodeView(File f, MainWindow.Controller controller) {
		this.file = f;
		this.contr = controller;
		
		setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		this.add(toolBar, BorderLayout.NORTH);
		
		btnSave = new JButton("Save");
		btnSave.setEnabled(false);
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(modified){
					boolean err = false;
					try {
						FileWriter fw = new FileWriter(file);
						fw.write(editorPane.getText());
						fw.close();
					} catch (IOException e1) {
						err = true;
						Log.err.println("Unable to write file: " + file.getAbsolutePath());
					}
					if(!err){
						originalText = editorPane.getText();
						btnSave.setEnabled(false);
						btnReset.setEnabled(false);
						modified = false;
					}
				}
			}
		});
		toolBar.add(btnSave);
		
		btnReset = new JButton("Reset");
		btnReset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(modified){
					editorPane.setText(originalText);
					btnSave.setEnabled(false);
					btnReset.setEnabled(false);
					modified = false;
				}
			}
		});
		btnReset.setEnabled(false);
		toolBar.add(btnReset);
		
		JButton btnCompile = new JButton("Compile");
		btnCompile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				contr.loadSource(file, 0);
			}
		});
		toolBar.add(btnCompile);
		
		setSize(300, 400);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		editorPane = new JEditorPane();
		editorPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(originalText.equals(editorPane.getText())){
					btnSave.setEnabled(false);
					btnReset.setEnabled(false);
					modified = false;
				}else{
					btnSave.setEnabled(true);
					btnReset.setEnabled(true);
					modified = true;
				}
			}
		});
		scrollPane.setViewportView(editorPane);
		editorPane.setFont(new Font("Consolas", Font.PLAIN, 12));
		originalText = "";
		try {
			editorPane.setPage(f.toURI().toURL());
		} catch (MalformedURLException e1) {
			Log.err.println("Unable to open file: " + f.getAbsolutePath());
		} catch (IOException e1) {
			Log.err.println("Unable to open file: " + f.getAbsolutePath());
		}
		originalText = editorPane.getText();
		setVisible(true);
	}

	

}
