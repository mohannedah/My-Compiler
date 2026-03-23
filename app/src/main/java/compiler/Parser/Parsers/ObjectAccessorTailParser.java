package compiler.Parser.Parsers;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.ObjectAccessor;
import compiler.Parser.Position;
import compiler.Parser.Exceptions.ParseError;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.Expressions.IdentifierExpression;
import compiler.Parser.Expressions.ObjectAccessorExpression;

/*
    <FIELD> ::= <METHOD_INVOCATION_EXPRESSION> | <IDENTIFIER>

    <FIELD_ACCESSOR> ::= <FIELD> <FIELD_TAIL>

    <FIELD_TAIL> ::= '.' <FIELD> <FIELD_TAIL> 
                | <epsilon> 
*/

public class ObjectAccessorTailParser extends Parser {
    public ObjectAccessorTailParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }

    public Expression parse(Expression leftOperand) throws Exception
    {
        ObjectAccessor token = this.readAccessor();
        if(token == null) return null;
        IdentifierExpression identifier = this.parseIdentifier();
        if(identifier == null) 
        {
            throw new ParseError("Expected an `Identifier` after the Object Accessor.", this.lexer.getAtPosition(this.position.position - 1));
        }
        return new ObjectAccessorExpression(leftOperand, identifier, token);
    };
}
