package compiler.Parser.Exceptions;

import compiler.Lexer.Tokens.Token;

public class ParseError extends Exception {
    public ParseError(String message, Token token) 
    {
        super(String.format("\n[Syntax Error] Line %d, Col %d: %s\n  -> Near token: '%s'\n", 
              token.state.lineNumber, 
              token.state.colNumber, 
              message, 
              token.token));
    };
}
