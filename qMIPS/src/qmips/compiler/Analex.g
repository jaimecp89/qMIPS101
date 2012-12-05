header{
	package qmips.compiler;	
}	

class Analex extends Lexer;

options{
 k = 2;
 importVocab = Anasint;
 caseSensitive = false;
}

tokens{
  ADD = "add";
  ADDU = "addu"; 
  SUB = "sub";
  SUBU = "subu";
  ADDI = "addi";
  ADDIU = "addiu";
  MULT = "mult";
  DIV = "div";
  DIVU = "divu";
  LD = "ld";
  LW = "lw";
  LH = "lh";
  LHU = "lhu";
  LB = "lb";
  LBU = "lbu";
  SD = "sd";
  SW = "sw";
  SH = "sh";
  SB = "sb";
  LUI = "lui";
  MFHI = "mfhi";
  MFLO = "mflo";
  MFCZ = "mfcz";
  MTCZ = "mtcz";
  
  AND = "and";
  OR = "or";
  ORI = "ori";
  XOR = "xor";
  NOR = "nor";
  SLT = "slt";
  SLTI = "slti";
  SLL = "sll";
  SRL = "srl";
  SRA = "sra";
  
  BEQ = "beq";
  BNE = "bne";
  J = "j";
  JR = "jr";
  JAL = "jal";
  
  QHAD = "qhad";
  QX = "qx";
  QY = "qy";
  QZ = "qz";
  QMEA = "qmea";
  QPHS = "qphs";
  QRST = "qrst";
  
  TRAP = "trap";
  
}

protected NEW_LINE: "\r\n"
{newline();};

BLANK: (' '|'\t' | "\r\n")
{$setType(Token.SKIP);};

LINECOM: "//" (~'\r')*
{$setType(Token.SKIP);};

BLOCKCOM: "/*" (options {greedy=false;}:(NEW_LINE|.))* "*/"
{$setType(Token.SKIP);};

protected DIGIT: '0'..'9';
protected CHAR: 'a'..'z';

NUMBER: DIGIT (DIGIT)*;
NUMDOUBLE: DIGIT "." (DIGIT)*;
HEXADECIMAL : "0x" (DIGIT | 'a'..'f') (DIGIT | 'a'..'f')*;
STRING: (CHAR)(CHAR)*;

MHEX: '-' HEXADECIMAL;
MDEC: '-' NUMBER;

R_REGISTER:  'r' NUMBER;
F_REGISTER:  'f' NUMBER;
D_REGISTER:  'd' NUMBER;
Q_REGISTER:  'q' NUMBER;

OP : '(';
CP : ')';
P  : '.';
C  : ',';
DP : ':';
OC : '[';
CC : ']';

 BYTE : ".byte";
 HWORD : ".hword";
 WORD : ".word";
 DOUBLE : ".double";
  
 TEXT : ".text";