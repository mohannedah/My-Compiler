package compiler.Parser.Expressions;
import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Operator;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Statements.IdentifierType;

public class RangeExpression extends BinaryExpression {
    public RangeExpression(Expression leftOperand, Expression rightOperand, Operator operator) 
    {
        super(leftOperand, rightOperand, operator);
    };

    @Override
    public IdentifierType getResultantType(SymbolTable table) throws Exception
    {
        if (!(leftOperand instanceof IdentifierExpression)) {
            throw new SemanticAnalysisException("The left operand of a range expression must be an identifier.");
        }
        IdentifierType leftOperandType = leftOperand.getResultantType(table);
        IdentifierType rightOperandType = rightOperand.getResultantType(table);
        if (!leftOperandType.value.equalsIgnoreCase("int") || !rightOperandType.value.equalsIgnoreCase("int")) {
            throw new SemanticAnalysisException(String.format(
                "Both operands of a range expression must be integers. Got left: %s, right: %s", 
                leftOperandType.value, rightOperandType.value
            ));
        }      
        IdentifierType resultantType = operator.getResultantTypeAfterOperation(leftOperandType, rightOperandType);     
        if (resultantType == null) {
            throw new SemanticAnalysisException(String.format(
                "Cannot apply range operator '%s' to types: %s and %s", 
                operator.token, leftOperandType.value, rightOperandType.value
            ));
        }
        return resultantType;
    }
}
