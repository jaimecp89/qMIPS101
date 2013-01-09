// $ANTLR : "Anasint.g" -> "Anasint.java"$

	package qmips.compiler;	
	import qmips.devices.memory.IMemory;
	import qmips.others.LogicVector;
	import java.util.Map;
	import java.util.Map.Entry;
	import java.util.HashMap;
	import java.util.Set;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;

public class Anasint extends antlr.LLkParser       implements AnasintTokenTypes
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
			if(opcode == 0x2){
				int addr =  ((labelMap.get(label) - e.getKey()-4)/4) & 0x0000FFFF;	
				instrMem.load(new LogicVector((opcode << 26) + addr, 32), e.getKey());
			}	else {
			  int addr = ((labelMap.get(label) - e.getKey()-4)/4) & 0x0000FFFF; 
			  int s = (Integer)e.getValue()[2];
			  int t = (Integer)e.getValue()[3];
			  instrMem.load(new LogicVector((opcode << 26) + (s << 21) + (t << 16) + addr, 32), e.getKey());
			}
		}
	}


protected Anasint(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public Anasint(TokenBuffer tokenBuf) {
  this(tokenBuf,6);
}

protected Anasint(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public Anasint(TokenStream lexer) {
  this(lexer,6);
}

public Anasint(ParserSharedInputState state) {
  super(state,6);
  tokenNames = _tokenNames;
}

	public final void program() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			{
			_loop3:
			do {
				if (((LA(1) >= BYTE && LA(1) <= WORD))) {
					dataDirective();
				}
				else {
					break _loop3;
				}
				
			} while (true);
			}
			{
			int _cnt5=0;
			_loop5:
			do {
				if ((LA(1)==TEXT)) {
					textDirective();
				}
				else {
					if ( _cnt5>=1 ) { break _loop5; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt5++;
			} while (true);
			}
			match(Token.EOF_TYPE);
			solveLabels();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
	}
	
	public final void dataDirective() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			int x; int[] v;
			{
			switch ( LA(1)) {
			case BYTE:
			{
				match(BYTE);
				x=integer();
				v=value();
				break;
			}
			case HWORD:
			{
				match(HWORD);
				x=integer();
				v=value();
				break;
			}
			case WORD:
			{
				match(WORD);
				x=integer();
				v=value();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			for(int i = 0; i < v.length; i++){
						      System.out.println(v[i]);
						  	 	if(dataMem == null) instrMem.load(new LogicVector(v[i], 32), x + i*4);
						  	 	else dataMem.load(new LogicVector(v[i], 32), x + i*4);
						     }
								
						
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
	}
	
	public final void textDirective() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			int i;
			match(TEXT);
			i=integer();
			pc = i;
			{
			int _cnt12=0;
			_loop12:
			do {
				if ((_tokenSet_2.member(LA(1)))) {
					instructions();
				}
				else {
					if ( _cnt12>=1 ) { break _loop12; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt12++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
	}
	
	public final int  integer() throws RecognitionException, TokenStreamException {
		int i = 0;
		
		Token  n = null;
		Token  mn = null;
		Token  h = null;
		Token  mh = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case NUMBER:
			{
				n = LT(1);
				match(NUMBER);
				i = Integer.parseInt(n.getText());
				break;
			}
			case MDEC:
			{
				mn = LT(1);
				match(MDEC);
				i = Integer.parseInt(mn.getText());
				break;
			}
			case HEXADECIMAL:
			{
				h = LT(1);
				match(HEXADECIMAL);
				i =  Integer.parseInt(h.getText().substring(2),16);
				break;
			}
			case MHEX:
			{
				mh = LT(1);
				match(MHEX);
				i = Integer.parseInt("-" + mh.getText().substring(3),16);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
		return i;
	}
	
	public final int[]  value() throws RecognitionException, TokenStreamException {
		int[] res = null;
		
		
		try {      // for error handling
			if (((LA(1) >= NUMBER && LA(1) <= MHEX)) && (_tokenSet_1.member(LA(2)))) {
				int i;
				i=integer();
				res = new int[]{i};
			}
			else if (((LA(1) >= NUMBER && LA(1) <= MHEX)) && ((LA(2) >= NUMBER && LA(2) <= MHEX))) {
				int i; int[] v;
				i=integer();
				v=value();
				int[] aux = new int[v.length +1];
												 for(int x = 1; x < v.length; x++) aux[x] = v[x]; 
												 aux[0] = i;
												 res = v;
												
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		return res;
	}
	
	public final void instructions() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case STRING:
			{
				String lbl;
				lbl=label();
				setLabel(lbl);
				break;
			}
			case ADD:
			case ADDU:
			case SUB:
			case SUBU:
			case MULT:
			case DIV:
			case DIVU:
			case AND:
			case OR:
			case XOR:
			case NOR:
			case SLT:
			case ADDI:
			case ADDIU:
			case ORI:
			case SLTI:
			case LW:
			case LH:
			case LHU:
			case LB:
			case LBU:
			case SW:
			case SH:
			case SB:
			case J:
			case BEQ:
			case BNE:
			case QPHS:
			case QMEA:
			case QRST:
			case QHAD:
			case QX:
			case QY:
			case QZ:
			case TRAP:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			body();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
	}
	
	public final String  label() throws RecognitionException, TokenStreamException {
		String l = "";
		
		Token  a = null;
		Token  b = null;
		
		try {      // for error handling
			if ((LA(1)==STRING) && (LA(2)==DP)) {
				a = LT(1);
				match(STRING);
				match(DP);
				l = a.getText();
			}
			else if ((LA(1)==STRING) && (_tokenSet_5.member(LA(2)))) {
				b = LT(1);
				match(STRING);
				l = b.getText();
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
		return l;
	}
	
	public final void body() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case ADD:
			case ADDU:
			case SUB:
			case SUBU:
			case MULT:
			case DIV:
			case DIVU:
			case AND:
			case OR:
			case XOR:
			case NOR:
			case SLT:
			{
				logicArithmetic();
				break;
			}
			case ADDI:
			case ADDIU:
			case ORI:
			case SLTI:
			{
				immediate();
				break;
			}
			case LW:
			case LH:
			case LHU:
			case LB:
			case LBU:
			{
				load();
				break;
			}
			case SW:
			case SH:
			case SB:
			{
				store();
				break;
			}
			case J:
			{
				jump();
				break;
			}
			case BEQ:
			case BNE:
			{
				branch();
				break;
			}
			case QPHS:
			case QMEA:
			case QRST:
			case QHAD:
			case QX:
			case QY:
			case QZ:
			{
				quantum();
				break;
			}
			case TRAP:
			{
				trap();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
	}
	
	public final void logicArithmetic() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			int funct,d,s,t;
			funct=logicArithmeticName();
			d=iregister();
			match(C);
			s=iregister();
			match(C);
			t=iregister();
			instrMem.load(new LogicVector((s << 21) + (t << 16) + (d << 11) + funct, 32), pc); pc = pc+4;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
	}
	
	public final void immediate() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			int opcode, s, t, imm;
			opcode=immediateName();
			t=iregister();
			match(C);
			s=iregister();
			match(C);
			imm=integer();
			instrMem.load(new LogicVector((opcode << 26) + (s << 21) + (t << 16) + (imm & 0x0000FFFF), 32), pc); pc = pc+4;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
	}
	
	public final void load() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			int opcode, s, t, imm;
			opcode=loadName();
			t=iregister();
			match(C);
			imm=integer();
			match(OP);
			s=iregister();
			match(CP);
			instrMem.load(new LogicVector((opcode << 26) + (s << 21) + (t << 16) + (imm & 0x0000FFFF), 32), pc); pc = pc+4;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
	}
	
	public final void store() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			int opcode, s, t, imm;
			opcode=storeName();
			imm=integer();
			match(OP);
			s=iregister();
			match(CP);
			match(C);
			t=iregister();
			instrMem.load(new LogicVector((opcode << 26) + (s << 21) + (t << 16) + (imm & 0x0000FFFF), 32), pc); pc = pc+4;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
	}
	
	public final void jump() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			if ((LA(1)==J) && ((LA(2) >= NUMBER && LA(2) <= MHEX))) {
				int opcode, addr;
				opcode=jumpName();
				addr=integer();
				instrMem.load(new LogicVector((opcode << 26) + addr, 32), pc); pc = pc+4;
			}
			else if ((LA(1)==J) && (LA(2)==STRING)) {
				int opcode; String lbl;
				opcode=jumpName();
				lbl=label();
				solveLater.put(pc, new Object[]{opcode, lbl}); pc = pc + 4;
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
	}
	
	public final void branch() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			if ((LA(1)==BEQ||LA(1)==BNE) && (LA(2)==R_REGISTER) && (LA(3)==C) && (LA(4)==R_REGISTER) && (LA(5)==C) && ((LA(6) >= NUMBER && LA(6) <= MHEX))) {
				int opcode, s, t, addr;
				opcode=branchName();
				s=iregister();
				match(C);
				t=iregister();
				match(C);
				addr=integer();
				instrMem.load(new LogicVector((opcode << 26) + (s << 21) + (t << 16) + addr, 32), pc); pc = pc+4;
			}
			else if ((LA(1)==BEQ||LA(1)==BNE) && (LA(2)==R_REGISTER) && (LA(3)==C) && (LA(4)==R_REGISTER) && (LA(5)==C) && (LA(6)==STRING)) {
				int opcode, s, t; String lbl;
				opcode=branchName();
				s=iregister();
				match(C);
				t=iregister();
				match(C);
				lbl=label();
				solveLater.put(pc, new Object[]{opcode, lbl, s, t}); pc = pc + 4;
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
	}
	
	public final void quantum() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case QHAD:
			case QX:
			case QY:
			case QZ:
			{
				int target, control, func;
				func=quantumName();
				target=qregister();
				match(C);
				control=qregister();
				instrMem.load(new LogicVector((0x0C << 26) + (target << 16) + (control << 11) + func, 32), pc); pc = pc+4;
				break;
			}
			case QPHS:
			{
				int target, control, arg;
				match(QPHS);
				target=qregister();
				match(C);
				control=qregister();
				match(C);
				arg=iregister();
				instrMem.load(new LogicVector((0x0C << 26) + (arg << 21) + (target << 16) + (control << 11) + 0x10, 32), pc); pc = pc+4;
				break;
			}
			case QMEA:
			{
				int target, reg, arg;
				match(QMEA);
				target=qregister();
				match(C);
				reg=iregister();
				match(C);
				arg=integer();
				instrMem.load(new LogicVector((0x0D << 26) + (arg << 21) + (target << 16) + (reg << 11) + 0x1A, 32), pc); pc = pc+4;
				break;
			}
			case QRST:
			{
				int reg;
				match(QRST);
				reg=iregister();
				instrMem.load(new LogicVector((0x0C << 26) + (reg << 21) + 0x1B, 32), pc); pc = pc+4;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
	}
	
	public final void trap() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			int imm;
			match(TRAP);
			imm=integer();
			instrMem.load(new LogicVector((0x1A << 26) + imm, 32), pc); pc = pc+4;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
	}
	
	public final int  logicArithmeticName() throws RecognitionException, TokenStreamException {
		int funct = 0;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case ADD:
			{
				match(ADD);
				funct =  0x20;
				break;
			}
			case ADDU:
			{
				match(ADDU);
				funct =  0x21;
				break;
			}
			case SUB:
			{
				match(SUB);
				funct =  0x22;
				break;
			}
			case SUBU:
			{
				match(SUBU);
				funct =  0x23;
				break;
			}
			case MULT:
			{
				match(MULT);
				funct =  0x18;
				break;
			}
			case DIV:
			{
				match(DIV);
				funct =  0x1A;
				break;
			}
			case DIVU:
			{
				match(DIVU);
				funct =  0x1B;
				break;
			}
			case AND:
			{
				match(AND);
				funct =  0x24;
				break;
			}
			case OR:
			{
				match(OR);
				funct =  0x25;
				break;
			}
			case XOR:
			{
				match(XOR);
				funct =  0x26;
				break;
			}
			case NOR:
			{
				match(NOR);
				funct =  0x27;
				break;
			}
			case SLT:
			{
				match(SLT);
				funct =  0x2A;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		return funct;
	}
	
	public final int  iregister() throws RecognitionException, TokenStreamException {
		int r = 0;
		
		Token  t = null;
		
		try {      // for error handling
			t = LT(1);
			match(R_REGISTER);
			r = Integer.parseInt(t.getText().substring(1));
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		return r;
	}
	
	public final int  immediateName() throws RecognitionException, TokenStreamException {
		int opcode = 0;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case ADDI:
			{
				match(ADDI);
				opcode = 0x8;
				break;
			}
			case ADDIU:
			{
				match(ADDIU);
				opcode = 0x9;
				break;
			}
			case ORI:
			{
				match(ORI);
				opcode = 0xD;
				break;
			}
			case SLTI:
			{
				match(SLTI);
				opcode = 0xA;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		return opcode;
	}
	
	public final int  loadName() throws RecognitionException, TokenStreamException {
		int opcode = 0;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case LW:
			{
				match(LW);
				opcode = 0x23;
				break;
			}
			case LH:
			{
				match(LH);
				opcode = 0x21;
				break;
			}
			case LHU:
			{
				match(LHU);
				opcode = 0x25;
				break;
			}
			case LB:
			{
				match(LB);
				opcode = 0x20;
				break;
			}
			case LBU:
			{
				match(LBU);
				opcode = 0x24;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		return opcode;
	}
	
	public final int  storeName() throws RecognitionException, TokenStreamException {
		int opcode = 0;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case SW:
			{
				match(SW);
				opcode = 0x2B;
				break;
			}
			case SH:
			{
				match(SH);
				opcode = 0x29;
				break;
			}
			case SB:
			{
				match(SB);
				opcode = 0x28;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_8);
		}
		return opcode;
	}
	
	public final int  jumpName() throws RecognitionException, TokenStreamException {
		int opcode = 0;
		
		
		try {      // for error handling
			match(J);
			opcode = 0x2;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_9);
		}
		return opcode;
	}
	
	public final int  branchName() throws RecognitionException, TokenStreamException {
		int opcode = 0;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case BEQ:
			{
				match(BEQ);
				opcode = 0x4;
				break;
			}
			case BNE:
			{
				match(BNE);
				opcode = 0x5;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		return opcode;
	}
	
	public final int  quantumName() throws RecognitionException, TokenStreamException {
		int func = -1;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case QHAD:
			{
				match(QHAD);
				func = 0x0;
				break;
			}
			case QX:
			{
				match(QX);
				func = 0x1;
				break;
			}
			case QY:
			{
				match(QY);
				func = 0x2;
				break;
			}
			case QZ:
			{
				match(QZ);
				func = 0x3;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_10);
		}
		return func;
	}
	
	public final int  qregister() throws RecognitionException, TokenStreamException {
		int r = 0;
		
		Token  t = null;
		
		try {      // for error handling
			t = LT(1);
			match(Q_REGISTER);
			r = Integer.parseInt(t.getText().substring(1));
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_11);
		}
		return r;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"BYTE",
		"HWORD",
		"WORD",
		"NUMBER",
		"MDEC",
		"HEXADECIMAL",
		"MHEX",
		"TEXT",
		"STRING",
		"DP",
		"C",
		"ADD",
		"ADDU",
		"SUB",
		"SUBU",
		"MULT",
		"DIV",
		"DIVU",
		"AND",
		"OR",
		"XOR",
		"NOR",
		"SLT",
		"ADDI",
		"ADDIU",
		"ORI",
		"SLTI",
		"OP",
		"CP",
		"LW",
		"LH",
		"LHU",
		"LB",
		"LBU",
		"SW",
		"SH",
		"SB",
		"J",
		"BEQ",
		"BNE",
		"QPHS",
		"QMEA",
		"QRST",
		"QHAD",
		"QX",
		"QY",
		"QZ",
		"TRAP",
		"R_REGISTER",
		"Q_REGISTER"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 2160L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 4503593184890880L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 2050L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 4503595332378610L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 4503593184892930L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 4503599627370496L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 4503597479876610L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 1920L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 6016L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 9007199254740992L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 4503593184909314L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	
	}
