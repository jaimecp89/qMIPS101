header{
	package qmips.compiler;	
	import qmips.devices.memory.IMemory;
	import qmips.others.LogicVector;
	import java.util.Map;
	import java.util.Map.Entry;
	import java.util.HashMap;
	import java.util.Set;
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
	
	public Anasint(Analex l, IMemory instrMem){
		this(l);
		this.instrMem = instrMem;
		this.labelMap = new HashMap<String, Integer>();
		this.solveLater = new HashMap<Integer, Object[]>();
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
				throw new RuntimeException("Label: " + label +  " not declared.");
			if(opcode == 0x2 || opcode == 0x3){
				int addr =  (labelMap.get(label)/4) & 0x0000FFFF;	
				instrMem.load(new LogicVector((opcode << 26) + addr, 32), e.getKey());
			}else{
			  int addr = ((labelMap.get(label) - e.getKey()-4)/4) & 0x0000FFFF; 
			  int s = (Integer)e.getValue()[2];
			  int t = (Integer)e.getValue()[3];
			  instrMem.load(new LogicVector((opcode << 26) + (s << 21) + (t << 16) + addr, 32), e.getKey());
			}
		}
	}

}


program : (dataDirective)* (textDirective)+  EOF {solveLabels();}
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
			  
instructions : ({String lbl; } lbl = label {setLabel(lbl);})? body
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
	 | quantum
	 | trap
	 ;
	  
logicArithmetic : 
				{int funct,d,s,t;}
				funct = logicArithmeticName d=iregister C s=iregister C t=iregister
				{instrMem.load(new LogicVector((s << 21) + (t << 16) + (d << 11) + funct, 32), pc); pc = pc+4;}
				;
			 
logicArithmeticName returns[int funct = 0] 
					: ADD  {funct =  0x20;}
  					| ADDU {funct =  0x21;}
  					| SUB  {funct =  0x22;}
  					| SUBU {funct =  0x23;}
  					| MULT {funct =  0x18;}
  					| DIV  {funct =  0x1A;}
  					| DIVU {funct =  0x1B;}
  					| AND  {funct =  0x24;}
  					| OR   {funct =  0x25;}
  					| XOR  {funct =  0x26;}
  					| NOR  {funct =  0x27;}
  					| SLT  {funct =  0x2A;}
  					;
	
immediate : 
		  {int opcode, s, t, imm;}
		  opcode=immediateName t=iregister C s=iregister C imm=integer
		  {instrMem.load(new LogicVector((opcode << 26) + (s << 21) + (t << 16) + (imm & 0x0000FFFF), 32), pc); pc = pc+4;}
		  ;		
					
immediateName returns[int opcode = 0] 
		  : ADDI  {opcode = 0x8;}
  		  | ADDIU {opcode = 0x9;}
  		  | ORI   {opcode = 0xD;}
  		  | SLTI  {opcode = 0xA;}
  		  ;
  				
load :
     {int opcode, s, t, imm;}
     opcode=loadName t=iregister C imm=integer OP s=iregister CP
	   {instrMem.load(new LogicVector((opcode << 26) + (s << 21) + (t << 16) + (imm & 0x0000FFFF), 32), pc); pc = pc+4;}
 	 ;
  				
loadName returns[int opcode = 0]  
  		 : LW  {opcode = 0x23;}
  		 | LH  {opcode = 0x21;}
  		 | LHU {opcode = 0x25;}
  		 | LB  {opcode = 0x20;}
  		 | LBU {opcode = 0x24;}
  		 ;	
  			
store :
      {int opcode, s, t, imm;}
      opcode=storeName imm=integer OP s=iregister CP C t=iregister
      {instrMem.load(new LogicVector((opcode << 26) + (s << 21) + (t << 16) + (imm & 0x0000FFFF), 32), pc); pc = pc+4;}
      ;
      
storeName returns[int opcode = 0] 
		  : SW {opcode = 0x2B;}
		  | SH {opcode = 0x29;}
		  | SB {opcode = 0x28;}
		  ;
		  
jump : 
      {int opcode, addr;}
      opcode=jumpName addr=integer
      {instrMem.load(new LogicVector((opcode << 26) + addr, 32), pc); pc = pc+4;}
     |
      {int opcode; String lbl;}
      opcode=jumpName lbl=label
      {solveLater.put(pc, new Object[]{opcode, lbl}); pc = pc + 4;}
     ;
     
jumpName returns[int opcode = 0] 
         : J   {opcode = 0x2;}
         | JAL {opcode = 0x3;}
         ;
         
jumpr:
	{int s;}
	JR s = iregister
	{instrMem.load(new LogicVector((0x1B << 26) + (s << 21),32), pc); pc = pc + 4;}
	;
         
branch : 
       {int opcode, s, t, addr;}
       opcode = branchName s = iregister C t = iregister  C addr = integer
       {instrMem.load(new LogicVector((opcode << 26) + (s << 21) + (t << 16) + addr, 32), pc); pc = pc+4;}
       |
       {int opcode, s, t; String lbl;}
       opcode = branchName s = iregister C t = iregister  C lbl = label
       {solveLater.put(pc, new Object[]{opcode, lbl, s, t}); pc = pc + 4;}
       ;
       
branchName returns[int opcode = 0]
           : BEQ {opcode = 0x4;}
           | BNE {opcode = 0x5;}
           ;
         
quantum : 
        {int target, control, func;}
        func=quantumName target=qregister C control=qregister
        {instrMem.load(new LogicVector((0x0C << 26) + (target << 16) + (control << 11) + func, 32), pc); pc = pc+4;}
        |
        {int target, control, arg;}
        QPHS target=qregister C control=qregister C arg=iregister
        {instrMem.load(new LogicVector((0x0C << 26) + (arg << 21) + (target << 16) + (control << 11) + 0x10, 32), pc); pc = pc+4;}
        |
        {int target, reg, arg;}
        QMEA target=qregister C reg = iregister C arg=integer
        {instrMem.load(new LogicVector((0x0D << 26) + (arg << 21) + (target << 16) + (reg << 11) + 0x1A, 32), pc); pc = pc+4;}
        |
        {int reg;}
        QRST reg=iregister
        {instrMem.load(new LogicVector((0x0C << 26) + (reg << 21) + 0x1B, 32), pc); pc = pc+4;}
        ;
        
quantumName returns[int func = -1]
          : QHAD {func = 0x0;}
          | QX {func = 0x1;}
          | QY {func = 0x2;}
          | QZ {func = 0x3;} 
          ;
          
          
trap : {int imm;}
	 TRAP imm = integer
	 {instrMem.load(new LogicVector((0x1A << 26) + imm, 32), pc); pc = pc+4;}
	 ;
           

iregister returns[int r = 0]: t:R_REGISTER {r = Integer.parseInt(t.getText().substring(1));}
		 ;  				
		 
qregister returns[int r = 0]: t:Q_REGISTER {r = Integer.parseInt(t.getText().substring(1));}
     ;