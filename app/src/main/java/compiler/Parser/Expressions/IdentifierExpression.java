package compiler.Parser.Expressions;

import org.objectweb.asm.Opcodes;

import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Identifier;
import compiler.Parser.Statements.IdentifierType;

public class IdentifierExpression extends Expression {
    public String value;
    public IdentifierType identifierType;

    public IdentifierExpression(Identifier token) 
    {
        super();
        this.value = token.token;
    };

    public String getName() 
    {
        return this.nodeType + "(" + value + ")";
    };

    @Override
    public IdentifierType getResultantType(SymbolTable table) throws Exception
    {
        try {
            return super.getResultantType(table); // Try getting the cached value.  
        } catch (UnsupportedOperationException e) {
            IdentifierType type = table.findKey(this);
            this.cachedIdentifierType = type;
            return this.cachedIdentifierType;
        }
    };

    @Override
    public void emit(EvaluationContext context) throws Exception
    {
        // In this case, we will be loading local variables. We need to know what local variables we will be loading according to its index.
        // We will utilize the function called `getLocalVariableIndex` inside the EvaluationContext `context` to determine the index.

        IdentifierType resultantType = this.getResultantType(null);

        try {
            int index = context.getLocalVariableIndex(this.value); 
            int loadByteCode = this.getLoadByteCode(resultantType);
            context.getMethodVisitor().visitVarInsn(loadByteCode, index);
        } catch(RuntimeException e) {
            String descriptor = this.determineByteCodePrefix(resultantType); 
            String ownerClass = "test"; 
            context.getMethodVisitor().visitFieldInsn(
                Opcodes.GETSTATIC, 
                ownerClass, 
                this.value, 
                descriptor
            );
        }
        
    };

    private int getLoadByteCode(IdentifierType identifierType) throws Exception 
    {    
        if(identifierType.isArray) {
            return Opcodes.ALOAD;
        }

        String typeStr = identifierType.value.toUpperCase();

        switch(typeStr) {
            case "INT":
            case "BOOL":
                return Opcodes.ILOAD;
            case "FLOAT":
                return Opcodes.FLOAD;
            case "STRING":
                return Opcodes.ALOAD;
            default:
                return Opcodes.ALOAD;
        }
    };
}