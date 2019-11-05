grammar DexterIR;

@header
{
package dexter.ir.parser;
}

program  : (classDecl | fnDecl | uFnDecl | genDecl)*  expr?;

uFnDecl  : name=ID '(' varDecl (',' varDecl)* ')' ':' retType=type ;

fnDecl
     : name=ID '(' varDecl (',' varDecl)* ')' ':' retType=type '->' body=expr
     | name=ID '(' ')' ':' retType=type '->' body=expr
     ;

genDecl
     : 'generator' name=ID '(' varDecl (',' varDecl)* ')' ':' retType=type '->' body=expr
     | 'generator' name=ID '(' ')' ':' retType=type '->' body=expr
     ;

classDecl : 'class' name=ID '(' varDecl (',' varDecl)* ')' ;

varDecl : name=ID ':' t=type;

type
     : name=ID                                          # classType
     | 'array' '(' dim=NUMBER ',' elemT=type ')'        # arrayType
     | 'ptr' '(' elemT=type ')'                         # pointerType
     | 'buffer' '(' elemT=type ',' dim=NUMBER ')'       # bufferType
     | 'tuple' '(' sz=NUMBER ',' elemT=type ')'         # tupleType
     | 'list' '(' elemT=type ')'                        # listType
     | '(' (type (',' type)* )? ')' '->' retT=type      # fnType
     | 'bv' '(' w=NUMBER ')'                            # bitvectorType
     | BASETYPE                                         # baseType
     ;

forIter : '(' 'for' v=ID 'in' l=expr ')' ;

expr // following Java's order of precedence

     : '(' expr ')'                                     # parenExpr

     // literals
     | ('true'|'false')                                 # boolLitExpr
     | NUMBER                                           # intLitExpr
     | FLOATNUM                                         # floatLitExpr
     | 'empty' '(' elemT=type ')'                       # emptyListExpr
     | 'uninterp' '(' t=BASETYPE ')'                    # uninterpExpr
     | 'null' '(' t=type ')'                            # nullExpr
     | 'concrete' '(' v=NUMBER ')'                      # cIntLitExpr
     | 'const' '(' t=BASETYPE ',' bw=NUMBER ')'         # chooseExpr
     | '[' expr (',' expr)* ']'                         # tupleExpr

     | 'ptr' '(' d=expr ',' o=expr ')'                  # ptrExpr
     | 'incr' '(' p=expr ',' o=expr ')'                 # incrPtrExpr
     | 'decr' '(' p=expr ',' o=expr ')'                 # decrPtrExpr

     | obj=expr '.' field=ID                            # fieldExpr

     // macros
     | '@' 'for' '(' v=ID 'in' m=expr ')' 'emit' body=expr    # forEachMacro
     | '@' 'init' '(' v=expr ')'                              # initMacro
     | '@' 'scope' '(' v=expr ')'                             # scopeMacro
     | '@' m=MACRO '(' t=type ')'                             # varsMacro
     | '@' m=('lb'|'ub') '(' idx=NUMBER ')'                   # boundsMacro

     // calls
     | name=ID '(' ')'                                  # callExpr
     | name=ID '(' expr (',' expr)* ')'                 # callExpr

     | ID                                               # varExpr

     // list comprehension
     | e=expr forIter+                                  # listCompExpr

     // unary and binary expressions
     | op=(PLUS|MINUS) b=expr                           # unaryExpr
     | op=NOT b=expr                                    # unaryExpr
     | op=BNOT b=expr                                   # unaryExpr

     | l=expr op=(MULT|DIV|MOD) r=expr                  # binaryExpr
     | l=expr op=(PLUS|MINUS) r=expr                    # binaryExpr

     | l=expr op=(SHL|LSHR|ASHR|BAND|BOR|BXOR) r=expr   # binaryExpr

     | l=expr op=(LE|GE|LT|GT) r=expr                   # binaryExpr

     | l=expr op=(EQ|NEQ) r=expr                        # binaryExpr
     | l=expr op=(AND|OR) r=expr                        # binaryExpr

     // our new expression types
     | 'forall' '(' v=ID ',' start=expr ',' end=expr ',' body=expr ')'          # forallExpr

     | 'if' cond=expr 'then' cons=expr 'else' alt=expr                          #ifExpr

     | ls=expr '-->' rs=expr                                                    # implyExpr

     | '(' '(' varDecl (',' varDecl)* ')' ':' retType=type '->' body=expr ')'   # lambdaExpr

     | 'let' '(' varDecl (',' varDecl)*  ')' 'in' body=expr                     # letExpr
     | 'let' '(' varDecl (',' varDecl)*  ')' 'in' body=expr 'assume' as=expr    # letExpr
     ;


forr : <assoc=right> 'for';
inr : <assoc=right> 'in';

// lexer

COMMENT
    : '/*' .*? '*/' -> channel(HIDDEN) // match anything between /* and */
    ;

LINE_COMMENT
    : '//' ~[\r\n]* '\r'? '\n' -> channel(HIDDEN)
    ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

// boolean
EQ : '=';
NEQ : '!=';
AND : '&&';
OR : '||';
NOT : '!';

// linear arithmetic
LT : '<';
LE : '<=';
GT : '>';
GE : '>=';
PLUS : '+';
MINUS : '-';

// Non linear arithmetic
MULT : '*';
DIV : '/';
MOD : '%';

// Bit-vector arithmetic
SHL : '<<';
LSHR : '>>';
ASHR : '>>>';
BAND : '&';
BOR : '|';
BXOR : '^';
BNOT : '~';

BASETYPE : 'int8_t' | 'int16_t' | 'int32_t' | 'uint8_t' | 'uint16_t' | 'uint32_t' | 'int' | 'bool' | 'float' ;

MACRO : 'out_vars' | 'out_arr_vars' | 'in_vars' | 'idx_vars' | 'vars' | 'consts' ;

ID : ([a-zA-Z]|'_') ([a-zA-Z]|[0-9]|'_')* ;

NUMBER : INT;
fragment INT :   '0' | '1'..'9' '0'..'9'* ; // no leading zeros

FLOATNUM : FLOAT;
fragment FLOAT : INT '.' '0'..'9'*;