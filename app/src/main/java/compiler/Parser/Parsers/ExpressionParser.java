package compiler.Parser.Parsers;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Operator;
import compiler.Lexer.Tokens.Token;
import compiler.Parser.Position;
import compiler.Parser.Exceptions.NoMoreTokensException;
import compiler.Parser.Expressions.BinaryExpression;
import compiler.Parser.Expressions.Expression;

/*
    <TERM> ::= <ARRAY_ACCESSOR> 
        | <OBJECT_ACCESSOR> 
        | <METHOD_INVOCATION> 
        | <IDENTIFIER> 
        | <NUMBER> 
        | <STRING> | 
        '(' <EXPRESSION> ')' 

    <EXPRESSION_TAIL> ::= <OPERATOR> <TERM> <EXPRESSION_TAIL> | <EPSILON>

    <EXPRESSION> ::= <TERM> <EXPRESSION_TAIL>

    Operators Precedence -> 
                            (), [], . (Grouping, Array Indexing, Field Access)

                            *, /, % (Multiplicative)

                            +, - (Additive)

                            <, >, <=, >= (Relational)

                            ==, =/= (Equality)

                            && (Logical AND)

                            || (Logical OR)
*/

public class ExpressionParser extends Parser {
    public ExpressionParser(CompilerLexer lexer, Position position) 
    {
        super(lexer, position);
    };

    private boolean isRelational(Token token) {
        if (token == null) return false;
        String str = token.token;
        return str.equals("<") || str.equals(">") || str.equals("<=") || str.equals(">=");
    }

    private boolean isAddition(Token token) {
        if (token == null) return false;
        String str = token.token;
        return str.equals("+") || str.equals("-");
    }

    private boolean isMultiplication(Token token) {
        if (token == null) return false;
        String str = token.token;
        return str.equals("*") || str.equals("/") || str.equals("%");
    }

    private boolean isEquality(Token token) 
    {
        if(token == null) return false;
        return token.token.equals("==") || token.token.equals("=/=");
    };

    public Expression parse() throws NoMoreTokensException, Exception
    {
        // Try to match the first rule.
        int prevPosition = this.position.position;
        Expression firstExpression = this.parseRuleOne();

        if(firstExpression != null) return firstExpression;
        
        this.resetPosition(prevPosition);

        Expression secondExpression = this.parseRuleTwo();

        return secondExpression;
    };

    private Expression parseRuleOne() throws Exception
    {
        return tryParseOr();
    }

    private Expression parseRuleTwo() throws Exception 
    {
        TermParser termParser = new TermParser(lexer, this.position);
        return termParser.parse();
    };

    Expression tryParseOr() throws Exception
    {
        Expression leftOperand = this.tryParseAnd();
        while(this.getCurrToken() != null && this.getCurrToken().token.equals("||")) {
            Operator operator = (Operator)this.consumeToken();
            Expression rightOperand = this.tryParseAnd();
            Expression newNode = new BinaryExpression(leftOperand, rightOperand, operator);
            leftOperand = newNode;
        };
        return leftOperand;
    };

    Expression tryParseAnd() throws Exception
    {
        Expression leftOperand = this.tryParseEquality();
        while(this.getCurrToken() != null && this.getCurrToken().token.equals("&&")) {
            Operator operator = (Operator)this.consumeToken();
            Expression rightOperand = this.tryParseEquality();
            Expression newNode = new BinaryExpression(leftOperand, rightOperand, operator);
            leftOperand = newNode;             
        };
        return leftOperand;
    }

    Expression tryParseEquality() throws Exception 
    {
        Expression leftOperand = this.tryParseRelational();
        while(isEquality(this.getCurrToken())) 
        {
            Operator operator = (Operator)this.consumeToken();
            Expression rightOperand = this.tryParseRelational();
            Expression newNode = new BinaryExpression(leftOperand, rightOperand, operator);
            leftOperand = newNode;
        };
        return leftOperand;
    }

    Expression tryParseRelational() throws Exception 
    {
        Expression leftOperand = this.tryParseAddition();
        while(this.isRelational(getCurrToken())) 
        {
            Operator operator = (Operator)this.consumeToken();
            Expression rightOperand = this.tryParseAddition();
            Expression newNode = new BinaryExpression(leftOperand, rightOperand, operator);
            leftOperand = newNode;
        };
        return leftOperand;
    };

    Expression tryParseAddition() throws Exception 
    {
        Expression leftOperand = this.tryParseMultiplication();
        while(this.isAddition(getCurrToken())) 
        {
            Operator operator = (Operator)this.consumeToken();
            Expression rightOperand = this.tryParseMultiplication();
            Expression newNode = new BinaryExpression(leftOperand, rightOperand, operator);
            leftOperand = newNode;
        };
        return leftOperand;
    }

    Expression tryParseMultiplication() throws Exception 
    {
        TermParser termParser = new TermParser(lexer, this.position);
        Expression leftOperand = termParser.parse();
        while(this.isMultiplication(getCurrToken())) 
        {
            Operator operator = (Operator)this.consumeToken();
            Expression rightOperand = termParser.parse();
            Expression newNode = new BinaryExpression(leftOperand, rightOperand, operator);
            leftOperand = newNode;
        };
        return leftOperand;
    }
}
