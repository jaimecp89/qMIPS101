package qmips.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import qmips.devices.memory.IMemory;
import qmips.presentation.swing.fancy.Log;

public class MIPSCompiler {
	
	

	public static boolean compile(File f, IMemory instr){
		FileInputStream fis = null;
		boolean res = true;
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
			Vector<String> errors = anasint.program();
			if(!errors.isEmpty()){
				res = false;
				for(String s : errors){
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