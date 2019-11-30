grammar ICSS;

//--- LEXER: ---
// IF support:
IF: 'if';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;

//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//Operations
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';

//--- PARSER: ---

// Style
stylesheet: variableAssignment* stylerule* EOF;
stylerule: selector OPEN_BRACE declaration* CLOSE_BRACE;

// Variable
variableAssignment: variableReference ASSIGNMENT_OPERATOR literal SEMICOLON;
variableReference: CAPITAL_IDENT;

// Operation
operation: literal (operationSymbol literal)+;
operationSymbol: addOperation | subtractOperation | multiplyOperation;
addOperation: PLUS;
subtractOperation: MIN;
multiplyOperation: MUL;

// Selector
selector: classSelector | (idSelector | tagSelector);
classSelector: CLASS_IDENT;
idSelector: ID_IDENT;
tagSelector: LOWER_IDENT;

// Declaration
declaration: propertyName COLON (literal | operation) SEMICOLON;
literal: pixelLiteral | percentageLiteral | colorLiteral | scalarLiteral | boolLiteral | variableReference;
propertyName: LOWER_IDENT;
pixelLiteral: PIXELSIZE;
percentageLiteral: PERCENTAGE;
colorLiteral: COLOR;
scalarLiteral: SCALAR;
boolLiteral: TRUE | FALSE;
