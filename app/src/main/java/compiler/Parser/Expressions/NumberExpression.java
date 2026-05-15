package compiler.Parser.Expressions;

import org.objectweb.asm.Opcodes;

import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.NumberToken;
import compiler.Parser.Statements.IdentifierType;

public class NumberExpression extends Expression {
    public String value;
    
    public NumberExpression(NumberToken token) 
    {
        super();
        this.value = token.token;
    }

    public String getName() 
    {
        return this.nodeType + "(" + value + ")";     
    }

    @Override
    public void analyze(SymbolTable table) throws Exception 
    {
        this.getResultantType(table);
    }

    @Override
    public IdentifierType getResultantType(SymbolTable scope) 
    {
        if(this.cachedIdentifierType != null) return this.cachedIdentifierType;

        if (this.value.contains(".")) {
            this.cachedIdentifierType = new IdentifierType("FLOAT", false);
        } else {
            this.cachedIdentifierType = new IdentifierType("INT", false);
        }

        return this.cachedIdentifierType;
    }

    @Override
    public void emit(EvaluationContext context) throws Exception 
    {
        if (this.cachedIdentifierType.value.equals("FLOAT")) {
            float floatValue = Float.parseFloat(this.value);
            
            context.getMethodVisitor().visitLdcInsn(floatValue);
        } 
        else {
            int intValue = Integer.parseInt(this.value);
            if (intValue >= -1 && intValue <= 5) {
                int opcode = Opcodes.ICONST_0 + intValue; // Since they are sequential, then this instruction is guaranteed to work.
                context.getMethodVisitor().visitInsn(opcode);
            } 
            else if (intValue >= Byte.MIN_VALUE && intValue <= Byte.MAX_VALUE) {
                context.getMethodVisitor().visitIntInsn(Opcodes.BIPUSH, intValue);
            } 
            else if (intValue >= Short.MIN_VALUE && intValue <= Short.MAX_VALUE) {
                context.getMethodVisitor().visitIntInsn(Opcodes.SIPUSH, intValue);
            } 
            else {
                context.getMethodVisitor().visitLdcInsn(intValue);
            }
        }
    }
}