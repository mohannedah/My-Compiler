package compiler.Parser.Expressions;

import org.objectweb.asm.Opcodes;

import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Statements.IdentifierType;

public class ArrayDeclarationExpression extends Expression {
    public Expression enclosedSize;
    public IdentifierType objectType;
    public ArrayDeclarationExpression(Expression enclosedSize, IdentifierType objectType) 
    {
        super();
        this.enclosedSize = enclosedSize;
        this.objectType = objectType;
    };

    @Override
    public IdentifierType getResultantType(SymbolTable table) throws Exception
    {
        try {
            // Try getting the field from the cache. If it doesn't exist, it will throw an UnsupportedOperationException, and then we will compute the type and cache it for future calls.
            return super.getResultantType(table);
        } catch (UnsupportedOperationException e) {
            IdentifierType enclosedSizeType = this.enclosedSize.getResultantType(table);

            if(!enclosedSizeType.value.equals("INT")) {
                throw new SemanticAnalysisException("Expected the size of the array to be an INT");
            }

            IdentifierType type =  new IdentifierType(objectType.value, true);
            this.cachedIdentifierType = type;
            return type;
        }
    }

    @Override
    public void emit(EvaluationContext context) throws Exception 
    {
        this.enclosedSize.emit(context); 
        
        String typeStr = this.objectType.value.toUpperCase();

        switch(typeStr) {
            case "INT":
                context.getMethodVisitor().visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
                break;
            case "FLOAT":
                context.getMethodVisitor().visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_FLOAT);
                break;
            case "BOOL": 
                context.getMethodVisitor().visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_BOOLEAN);
                break;
            case "STRING":
                context.getMethodVisitor().visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/String");
                break;
            default:
                context.getMethodVisitor().visitTypeInsn(Opcodes.ANEWARRAY, this.objectType.value);
                break;
        }
    }
}
