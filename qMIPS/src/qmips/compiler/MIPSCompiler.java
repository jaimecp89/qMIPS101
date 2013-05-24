package qmips.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import qmips.devices.memory.IMemory;
import qmips.presentation.swing.fancy.Log;
import antlr.RecognitionException;
import antlr.TokenStreamException;

public class MIPSCompiler {
	
	

	public static CompilationResults compile(File f, IMemory instr){
		FileInputStream fis = null;
		CompilationResults res = null;
		try {
			fis = new FileInputStream(f);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		Analex analex = null;
		Anasint anasint = null;
		
		analex = new Analex(fis);
		anasint = new Anasint(analex, instr);
		try {
			res = anasint.program();
			if(!res.isSuccessfulCompilation()){
				for(String s : res.getCompilationErrors()){
					Log.err.println(s);
				}
			}
		} catch (RecognitionException e) {
			e.printStackTrace();
		} catch (TokenStreamException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static void compile(File f, IMemory instr, IMemory data){
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		Analex analex = null;
		Anasint anasint = null;
		
		analex = new Analex(fis);
		anasint = new Anasint(analex, instr, data);
		try {
			anasint.program();
		} catch (RecognitionException e) {
			e.printStackTrace();
		} catch (TokenStreamException e) {
			e.printStackTrace();
		}
	}
	
}