package compiler.Parser.Expressions;

import java.util.List;

import org.objectweb.asm.Opcodes;

import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Operator;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Statements.IdentifierType;
import compiler.Parser.Statements.StructProperty;

public class ObjectAccessorExpression extends BinaryExpression 
{

    public ObjectAccessorExpression(Expression leftOperand, Expression rightOperand, Operator operator) {
        super(leftOperand, rightOperand, operator);
    }

    @Override
    public void analyze(SymbolTable scope) throws Exception 
    {
        this.getResultantType(scope);
    };

    @Override
    public IdentifierType getResultantType(SymbolTable table) throws Exception 
    {
        if(this.cachedIdentifierType != null) 
        {
            return this.cachedIdentifierType;
        }

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

        SymbolTable newSymbolTable = new SymbolTable(table); // Creating a new symbol table for the chaining, to store the new struct property.
        newSymbolTable.insert((IdentifierExpression)rightOperand, foundProperty.propertyType, false);
        IdentifierType rightOperandType = rightOperand.getResultantType(newSymbolTable);
        this.cachedIdentifierType = rightOperandType;
        return this.cachedIdentifierType;
    }

    @Override
    public void emit(EvaluationContext context) throws Exception 
    {   
        this.leftOperand.emit(context);
        
        String className = this.leftOperand.getResultantType(null).value;
        String fieldName = ((IdentifierExpression)this.rightOperand).value;
        
        String fieldDescriptor = this.determineByteCodePrefix(this.getResultantType(null));

        context.getMethodVisitor().visitFieldInsn(
            Opcodes.GETFIELD, 
            className, 
            fieldName, 
            fieldDescriptor
        );
    };
}
