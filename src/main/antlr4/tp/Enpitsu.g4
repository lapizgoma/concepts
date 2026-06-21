grammar Enpitsu;

program: PROGRAM ID BRA_OPEN 
 sentence*
 BRA_CLOSE;

sentence: var_decl | var_assign | println;

var_decl: VAR ID SEMICOLON;
var_assign: ID ASSIGN NUM SEMICOLON;
println: PRINTLN NUM SEMICOLON;

// IGNORABLES
WS
:
 [ \t\r\n]+ -> skip
;

// PALABRAS RESERVADAS
PROGRAM:'program';
VAR:'var';
PRINTLN:'println';

// OPERADORES ARITMÉTICOS
PLUS:'+';
MINUS:'-';
MULT:'*';
DIV:'/';

// OPERADORES LÓGICOS
AND:'&&';
OR:'||';
NOT:'!';
NEQ:'!=';
EQ:'==';
GEQ:'>=';
LEQ:'<=';
GT:'>';
LT:'<';

ASSIGN:'=';

BRA_OPEN:'{';
BRA_CLOSE:'}';
PAR_OPEN:'(';
PAR_CLOSE:')';
SEMICOLON:';';

// IDENTIFICADORES
ID:[a-zA-Z_][a-zA-Z0-9_]*;

NUM:[0-9]+;
