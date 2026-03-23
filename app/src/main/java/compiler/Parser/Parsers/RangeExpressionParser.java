package compiler.Parser.Parsers;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Operator;
import compiler.Lexer.Tokens.RangeOperator;
import compiler.Lexer.Tokens.Token;
import compiler.Parser.Position;
import compiler.Parser.Exceptions.ParseError;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.Expressions.IdentifierExpression;
import compiler.Parser.Expressions.RangeExpression;

public class RangeExpressionParser extends Parser {
    public RangeExpressionParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }

    public Operator readRangeOperator() throws Exception
    {
        Token token = this.consumeToken();
        if(token == null) return null;
        if(token.token.equals("->")) return (Operator)token;
        return null;
    };

    public RangeExpression parse() throws Exception 
    {
        ExpressionParser expressionParser = new ExpressionParser(lexer, this.position);
        Expression expressionOne = expressionParser.parse();
        if(expressionOne == null) return null;
        Operator operator = this.readRangeOperator();
        if(operator == null) return null;
        Expression rightOperand = expressionParser.parse();
        if(rightOperand == null) {
            throw new ParseError("Expected an expression after the `range` operator", operator);
        }
        return new RangeExpression(expressionOne, rightOperand, operator);
    };
}
