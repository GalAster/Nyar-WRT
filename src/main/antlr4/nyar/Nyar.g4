grammar Nyar;
import NyarOperators, NyarKeywords;
// $antlr-format useTab false ;reflowComments false;
// $antlr-format alignColons hanging;
program: statement* EOF;
//elementList: elision? expression ( ',' elision? expression)*;
statement
    : block
    | emptyStatement
    | expressionStatement
    | assignStatement
    | ifStatement;
/*====================================================================================================================*/
block: '{' statement+? '}';
expr_block: block | expression;
/*====================================================================================================================*/
emptyStatement: eos;
eos: Semicolon;
/*====================================================================================================================*/
expressionStatement: expression (',' expression)* eos?;
expression // High computing priority in the front
    : op = prefix_ops expression                                        # PrefixExpression
    | left = expression op = bit_ops right = expression                 # Binary_Like
    | left = expression op = logic_ops right = expression               # Logic_Like
    | <assoc = right> left = expression op = pow_ops right = expression # Power_Like
    | left = expression op = mul_ops right = expression                 # Multiply_Like
    | left = expression op = add_ops right = expression                 # Plus_Like
    | left = expression op = list_ops right = expression                # List_Like
    | <assoc = right> id = SYMBOL mod = assign_ops expr = expression    # OperatorAssignExpression
    | atom = STRING                                                     # String
    | atom = NUMBER                                                     # Number
    | atom = SYMBOL                                                     # Symbol
    | '(' expression ')'                                                # PriorityExpression;
prefix_ops: Plus | Minus | Bang;
bit_ops: LeftShift | RightShift;
logic_ops
    : Equal
    | NotEqual
    | Equivalent
    | NotEquivalent
    | Grater
    | GraterEqual
    | Less
    | LessEqual;
pow_ops: Power | Root;
mul_ops: Divide | Times | Multiply | Kronecker | TensorProduct;
add_ops: Plus | Minus;
list_ops: Concat;
/*====================================================================================================================*/
assignStatement
    : mod = assign_mods id = SYMBOL expr = expression eos? # ModifierAssignExpression
    | mod = assign_mods id = SYMBOL expr = block eos?      # ModifierAssignBlock;
assign_ops
    : Assign
    | PlusTo
    | MinusFrom
    | LetAssign
    | FinalAssign;
lazy_assign: DelayedAssign;
assign_mods: Let | Final;
/*====================================================================================================================*/
ifStatement: If condition elseifStatement (Else expr_block)?  eos?;
elseifStatement: (Else If condition)*;
condition: '('? expression expr_block ')'?;
/*====================================================================================================================*/
// literalSatement: arrayLiteral; arrayLiteral: '[' elementList? ','? elision? ']'; elision: ','+;

// $antlr-format alignColons trailing;
//list : LM (expression Comma?)* RM; record : LL (keyValue Comma?)* RL; keyValue : key = SYMBOL
// Colon value = expression; mathAlias : alias = MathConstant;