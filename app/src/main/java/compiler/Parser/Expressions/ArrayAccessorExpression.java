package compiler.Parser.Expressions;

import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.IndexingOperator;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Statements.IdentifierType;

public class ArrayAccessorExpression extends BinaryExpression {
    public ArrayAccessorExpression(Expression leftOperand, Expression rightOperand) 
    {
        super(leftOperand, rightOperand, new IndexingOperator("[]"));
    };

    @Override
    public IdentifierType getResultantType(SymbolTable table) throws Exception
    {
        IdentifierType leftOperandType = leftOperand.getResultantType(table);
        IdentifierType rightOperandType = rightOperand.getResultantType(table);   
        if (!rightOperandType.value.equals("INT")) {
            throw new SemanticAnalysisException(String.format(
                "Array index must evaluate to an integer type. Got: %s", rightOperandType.value
            ));
        }
        if(!leftOperandType.isArray) {
            throw new SemanticAnalysisException(String.format(
                "Expected an array to be indexed. Got: %s", leftOperandType.value
            ));
        }
        IdentifierType resultantType = leftOperandType;
        if (resultantType == null) {
            throw new SemanticAnalysisException(String.format(
                "Cannot apply indexing operator '[]' to type: %s", leftOperandType.value
            ));
        }
        return resultantType;
    }
}
