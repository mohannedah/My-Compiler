package compiler.Parser.Parsers;

import java.util.List;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Brackets;
import compiler.Lexer.Tokens.Identifier;
import compiler.Lexer.Tokens.Keyword;
import compiler.Parser.Exceptions.ParseError;
import compiler.Parser.Exceptions.UnmatchingBracketError;
import compiler.Parser.Position;
import compiler.Parser.Statements.StructProperty;
import compiler.Parser.Statements.StructStatement;

/*
    <STRUCT_DEFINITION> ::= coll <IDENTIFIER> '{' <STRUCT_PROPERTIES> '}'
    <STRUCT_PROPERTY> ::= <TYPE> <IDENTIFIER> ';'
    <STRUCT_PROPERTIES> ::= <STRUCT_PROPERTY> <STRUCT_PROPERTIES> | <EPSILON>
*/

public class StructParser extends Parser {
    public StructParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }

    public StructStatement parse() throws Exception
    {
        Keyword collKeword = this.readKeyword("coll");
        if(collKeword == null) 
        {
            return null;
        }

        Identifier identifier = this.readIdentifier();

        if(identifier == null) 
        {
            throw new ParseError("Expected an `Identifier` in a struct Statement", collKeword);
        }
        
        Brackets leftBraces = this.readBracket("{");

        if(leftBraces == null) 
        {
            throw new ParseError("Expected an opening Braces in a struct Statement", identifier);
        }

        StructPropertiesParser structPropertiesParser = new StructPropertiesParser(this.lexer, this.position);
        
        List<StructProperty> structProperties = structPropertiesParser.parse();

        Brackets rigthBraces = this.readBracket("}");

        if(rigthBraces == null) 
        {
            throw new UnmatchingBracketError("}", identifier);
        }

        return new StructStatement(identifier, structProperties);
    };
}
