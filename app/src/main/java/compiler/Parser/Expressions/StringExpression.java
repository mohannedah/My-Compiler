package compiler.Parser.Expressions;

import compiler.Lexer.Tokens.StringToken;

public class StringExpression extends Expression {
    public String value;
    public StringExpression(StringToken token) 
    {
        super();
        this.value = token.token;
    };
}
