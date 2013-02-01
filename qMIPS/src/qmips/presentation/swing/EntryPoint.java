package qmips.presentation.swing;

import qmips.presentation.builders.QuantumMIPS;
import qmips.presentation.swing.fancy.MainWindow;
import qmips.presentation.swing.fancy.MainWindowController;
import qmips.presentation.swing.simple.MainView;

public class EntryPoint {

	public static void main(String[] args){
		new MainWindowController(new QuantumMIPS());
	}
	
}
