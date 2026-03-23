package compiler.Parser.Expressions;

import java.util.List;

import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Operator;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Statements.IdentifierType;
import compiler.Parser.Statements.StructProperty;

public class ObjectAccessorExpression extends BinaryExpression {
    public ObjectAccessorExpression(Expression leftOperand, Expression rightOperand, Operator operator) {
        super(leftOperand, rightOperand, operator);
    }

    @Override
    public IdentifierType getResultantType(SymbolTable table) throws Exception 
    {
        IdentifierType leftOperandType = leftOperand.getResultantType(table);

        if (!(rightOperand instanceof IdentifierExpression)) {
            throw new SemanticAnalysisException("The right side of an object accessor ('.') must be an identifier.");
        }

        String propertyName = ((IdentifierExpression) rightOperand).value;

        List<StructProperty> structProperties = table.getStructProperties(leftOperandType);

        StructProperty foundProperty = null;
        for(StructProperty property: structProperties) 
        {
            if(property.identifier.token.equals(propertyName)) {
                foundProperty = property;
                break;
            }
        }

        if(foundProperty == null) {
            throw new SemanticAnalysisException(String.format(
                "Cannot resolve property '%s' on object of type '%s'", 
                propertyName, leftOperandType.value
            ));
        }

        return rightOperand.getResultantType(table);
    }
}
