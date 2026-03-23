package compiler.Parser.Expressions;

import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Operator;
import compiler.Parser.Statements.IdentifierType;

public class BinaryExpression extends Expression {
    public Expression leftOperand, rightOperand;
    public Operator operator;   
    public BinaryExpression(Expression leftOperand, Expression rightOperand, Operator operator) 
    {
        super();
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.operator = operator;
        this.nodeChildren.addLast(leftOperand);
        this.nodeChildren.addLast(rightOperand);
    }

    public String getName() 
    {
        return this.nodeType + "(" + this.operator.token + ")";
    }

    public IdentifierType getResultantType(SymbolTable table) throws Exception
    {
        IdentifierType leftOperandType = leftOperand.getResultantType(table), rightOperandType = rightOperand.getResultantType(table);
        IdentifierType resultantType = operator.getResultantTypeAfterOperation(leftOperandType, rightOperandType);
        return resultantType;
    }
}
