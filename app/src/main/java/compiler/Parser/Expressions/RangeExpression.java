package compiler.Parser.Expressions;
import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Operator;
import compiler.Parser.Exceptions.TypeError;
import compiler.Parser.Statements.IdentifierType;

public class RangeExpression extends BinaryExpression {
    public RangeExpression(Expression leftOperand, Expression rightOperand, Operator operator) 
    {
        super(leftOperand, rightOperand, operator);
    };

    @Override
    public IdentifierType getResultantType(SymbolTable table) throws Exception
    {
        if(this.cachedIdentifierType != null) 
        {
            return this.cachedIdentifierType;
        } 

        if(!(leftOperand instanceof NumberExpression)) 
        {
            throw new TypeError(
                "Left Operand in a range Expression should be an Integer."
            );    
        }

        IdentifierType leftOperandType = leftOperand.getResultantType(table);
        IdentifierType rightOperandType = rightOperand.getResultantType(table);
        if (!leftOperandType.value.equals("INT") || !rightOperandType.value.equals("INT")) {
            throw new TypeError(String.format(
                "Both operands of a range expression must be integers. Got left: %s, right: %s", 
                leftOperandType.value, rightOperandType.value
            ));
        }      
        IdentifierType resultantType = operator.getResultantTypeAfterOperation(leftOperandType, rightOperandType);     
        if (resultantType == null) {
            throw new TypeError(String.format(
                "Cannot apply range operator '%s' to types: %s and %s", 
                operator.token, leftOperandType.value, rightOperandType.value
            ));
        }
        this.cachedIdentifierType = resultantType;
        return resultantType;
    }

    @Override
    public void emit(EvaluationContext context) throws Exception 
    {
        throw new UnsupportedOperationException("The `emit` operation is not supported in this expression. Consider calling it inside a For-Loop");
    }
}
 

