package compiler.Parser.Expressions;

import org.objectweb.asm.Opcodes;

import compiler.DataStructures.EvaluationContext;
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
        try {
            // Try getting the field from the cache. If it doesn't exist, it will throw an UnsupportedOperationException, and then we will compute the type and cache it for future calls.
            return super.getResultantType(table);
        } catch (Exception e) {
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
            IdentifierType resultantType = new IdentifierType(leftOperandType.value, false);
            if (resultantType == null) {
                throw new SemanticAnalysisException(String.format(
                    "Cannot apply indexing operator '[]' to type: %s", leftOperandType.value
                ));
            }
            this.cachedIdentifierType = resultantType;
            return resultantType;
        }
    }

    @Override 
    public void emit(EvaluationContext context) throws Exception  
    {
        this.leftOperand.emit(context);
        this.rightOperand.emit(context);

        // At this point we know that the `rightOperand` has already been evaluated in the stack of operations and we can safely emit the instructions for array accessing.
        context.getMethodVisitor().visitInsn(getArrayLoadOpcode(this.getResultantType(null)));
    };

    private int getArrayLoadOpcode(IdentifierType identifierType) {
        String typeStr = identifierType.value.toUpperCase();
        switch (typeStr) {
            case "INT":
                return Opcodes.IALOAD;
            case "FLOAT":
                return Opcodes.FALOAD;
            case "BOOL":
                return Opcodes.BALOAD; 
            case "DOUBLE":
                return Opcodes.DALOAD;
            case "LONG":
                return Opcodes.LALOAD;
            default:
                return Opcodes.AALOAD;
        }
    }
}
