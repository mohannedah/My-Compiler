package compiler.Lexer.Tokens;

import compiler.Lexer.State;

public class Token {
    public String token;
    public State state;
    
    public Token(String token) 
    {
        this.token = token;
    };

    public Token(String token, State state) 
    {
        this.token = token;
        this.state = state;
    };

    public String getToken() 
    {
        return this.token;
    };

    @Override
    public String toString() 
    {
        return "Token[" + this.getClass().getSimpleName() + " | '" 
        + this.token + "' @ " + this.state.lineNumber + ":" + this.state.colNumber + "]";
    };
}