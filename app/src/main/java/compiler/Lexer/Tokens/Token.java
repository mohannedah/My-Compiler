package compiler.Lexer.Tokens;

import compiler.Lexer.State;

public class Token {
    public String token;
    public State state;
    
    public Token(String token) 
    {
        this.token = token;
    };

    public String getToken() 
    {
        return this.token;
    };
}