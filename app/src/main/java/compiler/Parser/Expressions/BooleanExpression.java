package compiler.Parser.Expressions;
import org.objectweb.asm.Opcodes;

import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Token;
import compiler.Parser.Statements.IdentifierType;

public class BooleanExpression extends Expression {
    public String value;
    public BooleanExpression(Token token) 
    {
        this.value = token.token;
    };

    public IdentifierType getResultantType(SymbolTable table) throws Exception
    {
        // No need to check the parent Expression's cachedIdentifierType field since a boolean literal it won't traverse the subtree of any other expression, so it won't be expensive to compute its type every time. 
        return new IdentifierType("BOOL", false);
    }

    @Override
    public void emit(EvaluationContext context) throws Exception 
    {
        // Since Booleans are treated the same as integers, we are going to push the iconst_1 for TRUE and iconst_0 for FALSE.
        if(this.value.equals("true")) {
            context.getMethodVisitor().visitInsn(Opcodes.ICONST_1);
        } else {
            context.getMethodVisitor().visitInsn(Opcodes.ICONST_0);
        }
    };
}
