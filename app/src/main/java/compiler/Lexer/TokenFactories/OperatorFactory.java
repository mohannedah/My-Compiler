package compiler.Lexer.TokenFactories;

import compiler.Lexer.Tokens.Operator;
import compiler.Lexer.Tokens.Token;

public class OperatorFactory implements TokenFactory {
    @Override
    public Token create(String token) {
        return new Operator(token);
    }
}
