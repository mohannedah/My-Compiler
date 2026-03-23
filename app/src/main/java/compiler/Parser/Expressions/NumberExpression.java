package compiler.Parser.Expressions;

import compiler.Lexer.Tokens.NumberToken;

public class NumberExpression extends Expression {
    public String value;
    public NumberExpression(NumberToken token) 
    {
        super();
        this.value = token.token;
    };

    public String getName() 
    {
        return this.nodeType + "(" + value + ")";     
    };
}
