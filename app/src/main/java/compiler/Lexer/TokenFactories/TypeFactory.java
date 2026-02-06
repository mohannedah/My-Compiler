package compiler.Lexer.TokenFactories;
import compiler.Lexer.Tokens.Type;

public class TypeFactory implements TokenFactory {
    @Override
    public Type create(String token) {
        return new Type(token);
    }
}
