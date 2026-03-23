package compiler.Parser.Expressions;
import compiler.Lexer.Tokens.BooleanOperator;

public class BooleanExpression extends BinaryExpression {
    public BooleanExpression(Expression leftOperand, Expression rightOperand, BooleanOperator operator) 
    {
        super(leftOperand, rightOperand, operator);
    };
}
