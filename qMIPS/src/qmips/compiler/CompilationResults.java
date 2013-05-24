package qmips.compiler;

import java.util.Map;
import java.util.Vector;

public class CompilationResults {

	private Map<Integer, Instruction> instructions;
	private Map<String, Integer> labelMap;
	private Vector<String> compilationErrors;
	private boolean successfulCompilation;
	
	public CompilationResults(Map<Integer, Instruction> instructions, Vector<String> compilationErrors){
		this.instructions = instructions;
		this.labelMap = labelMap;
		this.compilationErrors = compilationErrors;
		this.successfulCompilation = compilationErrors.isEmpty();
	}
	
	public Map<Integer, Instruction> getInstructions() {
		return instructions;
	}

	public boolean isSuccessfulCompilation() {
		return successfulCompilation;
	}

	public Vector<String> getCompilationErrors() {
		return compilationErrors;
	}
	
	public Map<String, Integer> getLabelMap() {
		return labelMap;
	}

	
	
}
