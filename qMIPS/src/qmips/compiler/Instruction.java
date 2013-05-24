package qmips.compiler;

public class Instruction {

	private String operation;
	private String[] parameters;
	private String label;
	
	public Instruction(String operation, String[] parameters){
		this.operation = operation;
		this.parameters = parameters;
	}

	public String getOperation() {
		return operation;
	}

	public String[] getParameters(){
		return parameters;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}