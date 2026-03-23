package compiler.Parser.Parsers;

import java.util.ArrayList;
import java.util.List;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Identifier;
import compiler.Lexer.Tokens.Seperator;
import compiler.Parser.Exceptions.ParseError;
import compiler.Parser.Position;
import compiler.Parser.Statements.IdentifierType;
import compiler.Parser.Statements.StructProperty;

/* 
    <STRUCT_PROPERTY> ::= <TYPE> <IDENTIFIER> ';'
    <STRUCT_PROPERTIES> ::= <STRUCT_PROPERTY> <STRUCT_PROPERTIES> | <EPSILON>
*/

public class StructPropertiesParser extends Parser {
    public StructPropertiesParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }
    public List<StructProperty> parse() throws Exception
    {
        List<StructProperty> structProperties = new ArrayList<>();
        StructProperty property = this.parseProperty();
        while(property != null) 
        {
            structProperties.addLast(property);
            property = this.parseProperty();
        }
        return structProperties;
    };

    public StructProperty parseProperty() throws Exception
    {
        TypeParser typeParser = new TypeParser(lexer, position);
        IdentifierType identifierType = typeParser.parse();
        if(identifierType == null) return null;
        Identifier identifier = this.readIdentifier();
        if(identifier == null) {
            throw new ParseError("Expected an `Identifier` in a struct property definition.", this.lexer.getAtPosition(this.position.position - 1));
        };
        Seperator seperator = this.readSeperator(";");
        if(seperator == null) 
        {
            throw new ParseError("Expected a `;` in the end of a property definition.", identifier);
        };
        return new StructProperty(identifier, identifierType);
    };
}
