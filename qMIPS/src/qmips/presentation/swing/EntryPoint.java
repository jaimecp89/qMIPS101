package qmips.presentation.swing;

import qmips.presentation.builders.QuantumMIPS;

public class EntryPoint {

	public static void main(String[] args){
		new MainView(new QuantumMIPS());
	}
	
}
