package compiler.Lexer.TokenFactories;
import compiler.Lexer.CompilerLexer.*;
import compiler.Lexer.Tokens.Keyword;

public class KeywordFactory implements TokenFactory {
    @Override
    public Keyword create(String token) {
        return new Keyword(token);
    }
}
