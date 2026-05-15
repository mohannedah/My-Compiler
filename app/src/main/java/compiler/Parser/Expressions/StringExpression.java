package compiler.Parser.Expressions;

import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.StringToken;
import compiler.Parser.Statements.IdentifierType;

public class StringExpression extends Expression {
    public String value;
    public StringExpression(StringToken token) 
    {
        super();
        this.value = token.token;
    };

    public String getName() 
    {
        return this.nodeType + "(" + value + ")";     
    }
    
    @Override   
    public void analyze(SymbolTable scope) 
    {
        this.getResultantType(scope);
    };

    @Override
    public IdentifierType getResultantType(SymbolTable scope) 
    {
        return new IdentifierType("STRING", false);
    };

    @Override 
    public void emit(EvaluationContext context) 
    {
        // The JVM uses the ldc (Load Constant) instruction to push a String reference
        // from the Constant Pool onto the Operand Stack.
        context.getMethodVisitor().visitLdcInsn(this.value);
    }
}
