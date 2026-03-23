package compiler.Parser.Exceptions;

import compiler.Lexer.Tokens.Token;

public class UnmatchingBracketError extends ParseError {
    public UnmatchingBracketError(String expectedBracket, Token token) {
        super(String.format("Expected a `%s`", expectedBracket), token);
    }
}
