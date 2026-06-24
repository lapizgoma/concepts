grammar Enpitsu;

// ***************************************
// 1. REGLAS SINTÁCTICAS 
// ***************************************

program: PROGRAM ID BRA_OPEN 
 sentence*
 BRA_CLOSE
 EOF;

sentence
	: var_decl 
	| var_assign 
	| println
	| print
	| condicionalIf
	| estructuraDoWhile
	;
	
// VARIABLES

var_decl: VAR tipo ID SEMICOLON;

tipo
	: INT_TYPE
	| FLOAT_TYPE
	| STRING_TYPE
	| BOOLEAN_TYPE
	;


var_assign: ID ASSIGN expresion SEMICOLON;


// INSTRUCCIÓN DE SALIDA
// imprimir con salto de línea
println: PRINTLN PAR_OPEN expresion PAR_CLOSE SEMICOLON;
// imprimir sin salto de línea
print: PRINT PAR_OPEN expresion PAR_CLOSE SEMICOLON;

// ESTRUCTURAS DE CONTROL

condicionalIf
    : IF PAR_OPEN expresion PAR_CLOSE BRA_OPEN (ifSentences+=sentence)* BRA_CLOSE 
      (ELSE BRA_OPEN (elseSentences+=sentence)* BRA_CLOSE)?
    ;
	
estructuraDoWhile: DO BRA_OPEN sentence* BRA_CLOSE WHILE 
	PAR_OPEN expresion PAR_CLOSE SEMICOLON;


// EXPRESIONES

expresion
	: expresion ( MULT | DIV ) expresion # expMultDiv
	| expresion ( PLUS | MINUS ) expresion # expSumaResta
	| expresion ( EQ | NEQ | LT | LEQ | GT | GEQ ) expresion # expRelacional
	| expresion ( AND | OR ) expresion # expLogica
	| NOT expresion # expNot
	| PAR_OPEN expresion PAR_CLOSE # expParentesis
	| factor # expFactor
	;
	
factor
	: ID
	| NUM
	| FLOAT_VAL
	| STRING_VAL
	| BOOLEAN_VAL
	;
	
// ***************************************
// 2. REGLAS LÉXICAS 
// ***************************************

// PALABRAS RESERVADAS
PROGRAM:'program';
VAR:'var';
PRINTLN:'println';
PRINT:'print';
IF: 'if';
ELSE: 'else';
DO: 'do';
WHILE : 'while';

// TIPOS DE DATOS
INT_TYPE    : 'int';
FLOAT_TYPE  : 'float';
STRING_TYPE : 'string';
BOOLEAN_TYPE: 'boolean';


// VALORES LITERALES
NUM : '0' | [1-9] [0-9]* ; //para que no acepte 0 a la izq
FLOAT : '0' '.' [0-9]+ | [1-9] [0-9]* '.' [0-9]+ ;
STRING_VAL  : '"' (~['"\r\n'] | '\\' .)* '"' ;
BOOLEAN_VAL : 'true' | 'false';

// SIGNOS Y OPERADORES
PLUS: '+';
MINUS: '-';
MULT: '*';
DIV: '/';
AND: '&&';
OR: '||';
NOT: '!';
NEQ: '!=';
EQ: '==';
GEQ: '>=';
LEQ: '<=';
GT: '>';
LT: '<';
ASSIGN: '=';
BRA_OPEN: '{';
BRA_CLOSE: '}';
PAR_OPEN: '(';
PAR_CLOSE: ')';
SEMICOLON: ';';

// IDENTIFICADORES
ID:[a-zA-Z_][a-zA-Z0-9_]*;


// IGNORABLES
WS :  [ \t\r\n]+ -> skip;
COMENTARIO_LINEA : '//' ~[\r\n]* -> skip ;
