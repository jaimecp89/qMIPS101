header{
	package qmips.compiler;	
	import qmips.devices.memory.IMemory;
	import qmips.others.LogicVector;
	import java.util.Map;
	import java.util.Map.Entry;
	import java.util.HashMap;
	import java.util.TreeMap;
	import java.util.Set;
	import java.util.Vector;
}

class Anasint extends Parser;

options{
  k = 6;
}

{

	private IMemory instrMem;
	private IMemory dataMem;
	private int pc = 0;
	private Map<String, Integer> labelMap;
	private Map<Integer, Object[]> solveLater;
	private Map<Integer, Instruction> instructions;
	private Vector<String> compilationErrors;
	private String name = "";
	
	public Anasint(Analex l, IMemory instrMem){
		this(l);
		this.instrMem = instrMem;
		this.labelMap = new HashMap<String, Integer>();
		this.solveLater = new HashMap<Integer, Object[]>();
		this.compilationErrors = new Vector<String>();
		this.instructions = new TreeMap<Integer, Instruction>();
	}
	
	public Anasint(Analex l, IMemory instrMem, IMemory dataMem){
		this(l, instrMem);
		this.dataMem = dataMem;
	}
	
	private void setLabel(String lbl){
		if(labelMap.containsKey(lbl))
			throw new RuntimeException("Duplicate label: " + lbl);
		labelMap.put(lbl,pc);	
	}
	
	private void solveLabels(){
		Set<Entry<Integer, Object[]>> entrySet = solveLater.entrySet();
		for(Entry<Integer, Object[]> e : entrySet){
			int opcode = (Integer)e.getValue()[0];
			String label = (String)e.getValue()[1];
			if(!labelMap.containsKey(label))
				//throw new RuntimeException("Label: " + label +  " not declared.");
				compilationErrors.add("Label: " + label +  " not declared.");
				else{
					if(opcode == 0x2 || opcode == 0x3){
						int addr =  (labelMap.get(label)/4) & 0x0000FFFF;	
						instrMem.load(new LogicVector((opcode << 26) + addr, 32), e.getKey());
					}else{
			  		int addr = ((labelMap.get(label) - e.getKey()-4)/4) & 0x0000FFFF; 
			  		int s = (Integer)e.getValue()[2];
			  		int t = (Integer)e.getValue()[3];
			  		instrMem.load(new LogicVector((opcode << 26) + (s << 21) + (t << 16) + addr, 32), e.getKey());
					}
					instructions.get(labelMap.get(label)).setLabel(label);
				}
		}
	}
	
  	@Override
  	public void reportError(RecognitionException ex){
    	compilationErrors.add(ex.getMessage());
    	try{
    		recover(ex,_tokenSet_0);
    	}catch(Exception e){
    		compilationErrors.add(e.getMessage());
    	}
  	}


}


program returns[CompilationResults res = null;]: (dataDirective)* (textDirective)+  EOF {solveLabels(); res = new CompilationResults(instructions, compilationErrors);}
		;

dataDirective : {int x; int[] v;}(BYTE x=integer v=value 
			  | HWORD x=integer v=value
			  | WORD x=integer v=value )
			  {  for(int i = 0; i < v.length; i++){
			  	 	if(dataMem == null) instrMem.load(new LogicVector(v[i], 32), x + i*4);
			  	 	else dataMem.load(new LogicVector(v[i], 32), x + i*4);
			     }
					
			  }
			  ;
	  
value returns[int[] res = null]: {int i;} i=integer {res = new int[]{i};}
						| {int i; int[] v;} i=integer v=value 
								{int[] aux = new int[v.length +1];
								 for(int x = 1; x < v.length; x++) aux[x] = v[x]; 
								 aux[0] = i;
								 res = v;
								}
	  					;
	  
integer returns[int i = 0] : n:NUMBER {i = Integer.parseInt(n.getText());}
              | mn:MDEC {i = Integer.parseInt(mn.getText());}
              | h:HEXADECIMAL {i =  Integer.parseInt(h.getText().substring(2),16);}
							| mh:MHEX {i = Integer.parseInt("-" + mh.getText().substring(3),16);}
							;
			  
textDirective : {int i;} TEXT i=integer {pc = i;} (instructions)+
			  ;
			  
instructions : ({String lbl;} lbl = label {setLabel(lbl);})? body
			 ;
			 
label returns[String l = ""]: a:STRING DP {l = a.getText();}
	  | b:STRING {l = b.getText();}
	  ;
	  
body : logicArithmetic 
	 | immediate 
	 | load 
	 | store
	 | jump
	 | jumpr
	 | branch
	 | mfhi
	 | quantum
	 | trap
	 ;
	  
logicArithmetic : 
				{int funct,d,s,t;}
				funct = logicArithmeticName d=iregister C s=iregister C t=iregister
				{
					instrMem.load(new LogicVector((s << 21) + (t << 16) + (d << 11) + funct, 32), pc); 
					instructions.put(pc, new Instruction(name, new String[]{"R" + d, "R" + s, "R" + t})); 
					pc = pc + 4;
				}
				;
			 
logicArithmeticName returns[int funct = 0] 
					: SLL  {funct =  0x00; name = "sll";}
					| SRL  {funct =  0x02; name = "srl";}
					| SRA  {funct =  0x03; name = "sra";}
					| ADD  {funct =  0x20; name = "add";}
  					| ADDU {funct =  0x21; name = "addu";}
  					| SUB  {funct =  0x22; name = "sub";}
  					| SUBU {funct =  0x23; name = "subu";}
  					| MULT {funct =  0x18; name = "mult";}
  					| DIV  {funct =  0x1A; name = "div";}
  					| DIVU {funct =  0x1B; name = "divu";}
  					| AND  {funct =  0x24; name = "and";}
  					| OR   {funct =  0x25; name = "or";}
  					| XOR  {funct =  0x26; name = "xor";}
  					| NOR  {funct =  0x27; name = "nor";}
  					| SLT  {funct =  0x2A; name = "slt";}
  					;
	
immediate : 
		  {int opcode, s, t, imm;}
		  opcode=immediateName t=iregister C s=iregister C imm=integer
		  {
		  	instrMem.load(new LogicVector((opcode << 26) + (s << 21) + (t << 16) + (imm & 0x0000FFFF), 32), pc); 
		  	instructions.put(pc, new Instruction(name, new String[]{"R" + t, "R" + s, String.valueOf(imm)})); 
		  	pc = pc + 4;
		  }
		  ;		
					
immediateName returns[int opcode = 0] 
		  : ADDI  {opcode = 0x8; name = "addi";}
  		  | ADDIU {opcode = 0x9; name = "addiu";}
  		  | ORI   {opcode = 0xD; name = "ori";}
  		  | SLTI  {opcode = 0xA; name = "slti";}
  		  ;
  				
load :
     {int opcode, s, t, imm;}
     opcode=loadName t=iregister C imm=integer OP s=iregister CP
	   {
	   	instrMem.load(new LogicVector((opcode << 26) + (s << 21) + (t << 16) + (imm & 0x0000FFFF), 32), pc); 
	   	instructions.put(pc, new Instruction(name, new String[]{"R" + t, String.valueOf(imm) + "(R" + s + ")"})); 
	   	pc = pc + 4;
	   }
 	 ;
  				
loadName returns[int opcode = 0]  
  		 : LW  {opcode = 0x23; name = "lw";}
  		 ;	
  			
store :
      {int opcode, s, t, imm;}
      opcode=storeName imm=integer OP s=iregister CP C t=iregister
      {
      	instrMem.load(new LogicVector((opcode << 26) + (s << 21) + (t << 16) + (imm & 0x0000FFFF), 32), pc); 
      	instructions.put(pc, new Instruction(name, new String[]{String.valueOf(imm) + "(R" + s + ")", "R" + t})); 
      	pc = pc+4;
      }
      ;
      
storeName returns[int opcode = 0] 
		  : SW {opcode = 0x2B; name = "sw";}
		  ;
		  
jump : 
      {int opcode, addr;}
      opcode=jumpName addr=integer
      {
      	instrMem.load(new LogicVector((opcode << 26) + addr, 32), pc);
      	instructions.put(pc, new Instruction(name, new String[]{String.valueOf(addr)})); 
      	pc = pc+4;
      }
     |
      {int opcode; String lbl;}
      opcode=jumpName lbl=label
      {
      	solveLater.put(pc, new Object[]{opcode, lbl}); 
      	instructions.put(pc, new Instruction(name, new String[]{lbl}));
      	pc = pc + 4;
      }
     ;
     
jumpName returns[int opcode = 0] 
         : J   {opcode = 0x2; name = "j";}
         | JAL {opcode = 0x3; name = "jal";}
         ;
         
jumpr:
	{int s;}
	JR s = iregister
	{
		instrMem.load(new LogicVector((0x1B << 26) + (s << 21),32), pc); 
		instructions.put(pc, new Instruction("jr", new String[]{"R" + s}));
		pc = pc + 4;
	}
	;
         
branch : 
       {int opcode, s, t, addr;}
       opcode = branchName s = iregister C t = iregister  C addr = integer
       {
       	instrMem.load(new LogicVector((opcode << 26) + (s << 21) + (t << 16) + addr, 32), pc); 
       	instructions.put(pc, new Instruction(name, new String[]{"R" + s, "R" + t, String.valueOf(addr)}));
       	pc = pc+4;
       }
       |
       {int opcode, s, t; String lbl;}
       opcode = branchName s = iregister C t = iregister  C lbl = label
       {
       	solveLater.put(pc, new Object[]{opcode, lbl, s, t});
       	instructions.put(pc, new Instruction(name, new String[]{"R" + s, "R" + t, lbl})); 
       	pc = pc + 4;
       }
       ;
       
branchName returns[int opcode = 0]
           : BEQ {opcode = 0x4; name = "beq";}
           | BNE {opcode = 0x5; name = "bne";}
           ;
           
mfhi : {int s;}
	 MFHI s = iregister
	 {
	 	instrMem.load(new LogicVector((0x1C << 26) + (s << 16), 32), pc); 
	 	instructions.put(pc, new Instruction("mfhi", new String[]{"R" + s}));
	 	pc = pc+4;
	 }
	 ;
         
quantum : 
        {int target, control, func;}
        func=quantumName target=qregister C control=qregister
        {
        	instrMem.load(new LogicVector((0x0C << 26) + (target << 16) + (control << 11) + func, 32), pc);
        	instructions.put(pc, new Instruction(name, new String[]{"Q" + target, "Q" + control}));
        	pc = pc+4;
        }
        |
        {int target, control, arg;}
        QPHS target=qregister C control=qregister C arg=iregister
        {
        	instrMem.load(new LogicVector((0x0C << 26) + (arg << 21) + (target << 16) + (control << 11) + 0x10, 32), pc);
        	instructions.put(pc, new Instruction("qphs", new String[]{"Q" + target, "Q" + control, "R" + arg})); 
        	pc = pc+4;
        }
        |
        {int target, reg, arg;}
        QMEA target=qregister C reg = iregister C arg=integer
        {
        	instrMem.load(new LogicVector((0x0F << 26) + (arg << 21) + (target << 16) + (reg << 11) + 0x1A, 32), pc);
        	instructions.put(pc, new Instruction("qmea", new String[]{"Q" + target, "R" + reg, String.valueOf(arg)})); 
        	pc = pc+4;
        }
        |
        {int reg;}
        QRST reg=iregister
        {
        	instrMem.load(new LogicVector((0x0C << 26) + (reg << 21) + 0x1B, 32), pc);
        	instructions.put(pc, new Instruction("qrst", new String[]{"R" + reg}));  
        	pc = pc+4;
        }
        |
        {int reg;}
        QCNT reg=iregister
        {
        	instrMem.load(new LogicVector((0x0C << 26) + (reg << 21) + 0x1C, 32), pc);
        	instructions.put(pc, new Instruction("qcnt", new String[]{"R" + reg})); 
        	pc = pc+4;
        }
        |
        {int reg;}
        QOFF reg=iregister
        {
        	instrMem.load(new LogicVector((0x0C << 26) + (reg << 21) + 0x1D, 32), pc);
        	instructions.put(pc, new Instruction("qoff", new String[]{"R" + reg})); 
        	pc = pc+4;
        }
        ;
        
quantumName returns[int func = -1]
          : QHAD {func = 0x0; name = "qhad";}
          | QX {func = 0x1; name = "qx";}
          | QY {func = 0x2; name = "qy";}
          | QZ {func = 0x3; name = "qz";} 
          ;
          
          
trap : {int imm;}
	 TRAP imm = integer
	 {
	 	instrMem.load(new LogicVector((0x1A << 26) + imm, 32), pc);
	 	instructions.put(pc, new Instruction("trap", new String[]{String.valueOf(imm)})); 
	 	pc = pc+4;
	 }
	 ;
           

iregister returns[int r = 0]: t:R_REGISTER {r = Integer.parseInt(t.getText().substring(1));}
		 ;  				
		 
qregister returns[int r = 0]: t:Q_REGISTER {r = Integer.parseInt(t.getText().substring(1));}
     ;