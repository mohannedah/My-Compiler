package compiler.Parser.Parsers;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Brackets;
import compiler.Lexer.Tokens.Token;
import compiler.Parser.Position;
import compiler.Parser.Statements.IdentifierType;

/*
    <TYPE> ::= <IDENTIFIER>'[]' | <IDENTIFIER>
*/

public class TypeParser extends Parser {
    public TypeParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }
    public IdentifierType parse() throws Exception
    {
        int startingPosition = this.position.position;
        IdentifierType res = this.parseRuleOne();
        if(res != null) return res;
        this.resetPosition(startingPosition);
        res = this.parseRuleTwo();
        return res;
    };

    public IdentifierType parseRuleOne() throws Exception
    {
        Token identifier = this.readType();
        if(identifier == null) return null;
        Brackets leftBracket = this.readBracket("[");
        if(leftBracket == null) return null;
        Brackets righBracket = this.readBracket("]");
        if(righBracket == null); 
        return new IdentifierType(identifier.token, true);
    };

    public IdentifierType parseRuleTwo() throws Exception 
    {
        Token identifier = this.readType();
        if(identifier == null) return null;
        return new IdentifierType(identifier.token, false);
    };
}
