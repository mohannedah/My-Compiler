package compiler.Parser.Parsers;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Identifier;
import compiler.Lexer.Tokens.Seperator;
import compiler.Parser.Position;
import compiler.Parser.Exceptions.ParseError;
import compiler.Parser.Statements.IdentifierType;
import compiler.Parser.Statements.VariableDeclarationStatement;

/*
    <VARIABLE_DECLARATION> ::= <TYPE> <IDENTIFIER>
*/

public class VariableDeclarationParser extends Parser {
    public VariableDeclarationParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }

    public VariableDeclarationStatement parse() throws Exception
    {
        TypeParser typeParser = new TypeParser(lexer, position);
        IdentifierType identifierType = typeParser.parse();
        if(identifierType == null) return null;
        Identifier identifier = this.readIdentifier();
        if(identifier == null) return null;
        return new VariableDeclarationStatement(identifierType, identifier);
    };
}
