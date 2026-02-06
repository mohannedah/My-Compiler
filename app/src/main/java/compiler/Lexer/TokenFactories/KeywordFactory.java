package compiler.Lexer.TokenFactories;
import compiler.Lexer.Keyword;
import compiler.Lexer.CompilerLexer.*;

public class KeywordFactory implements TokenFactory {
    @Override
    public Keyword create(String token) {
        return new Keyword(token);
    }
}
