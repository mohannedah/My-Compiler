package compiler.Parser.Expressions;

import compiler.Lexer.Tokens.ArithmeticOperator;

public class ArithmeticExpression extends BinaryExpression {
    public ArithmeticExpression(ArithmeticExpression leftOperand, ArithmeticExpression rightOperand, ArithmeticOperator operator) 
    {
        super(leftOperand, rightOperand, operator);
    };
}
