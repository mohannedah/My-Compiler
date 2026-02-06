package compiler.Lexer.TokenFactories;

import compiler.Lexer.Tokens.Token;

public interface TokenFactory {
    public Token create(String token);
}
