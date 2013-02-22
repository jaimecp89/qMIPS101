package qmips.presentation.swing;

import javax.swing.SwingUtilities;

import qmips.presentation.builders.QuantumMIPS;
import qmips.presentation.swing.fancy.MainWindowController;

public class EntryPoint {

	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
		      public void run() {
		    	  new MainWindowController(new QuantumMIPS());
		      }
		});
	}
	
}
