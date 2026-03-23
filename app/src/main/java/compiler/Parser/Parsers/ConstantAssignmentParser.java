package compiler.Parser.Parsers;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Keyword;
import compiler.Parser.Exceptions.ParseError;
import compiler.Parser.Expressions.IdentifierExpression;
import compiler.Parser.Position;
import compiler.Parser.Statements.ConstantAssignment;
import compiler.Parser.Statements.VariableAssignmentStatement;

/*
    <CONST_ASSIGNMENT_STATEMENT> ::= final <VARIABLE_ASSIGNMENT_EXPRESSION>
*/

public class ConstantAssignmentParser extends Parser {
    public ConstantAssignmentParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }

    public ConstantAssignment parse() throws Exception  {
        Keyword finalKeyword = this.readKeyword("final");

        if(finalKeyword == null) return null;

        VariableAssignmentParser parser = new VariableAssignmentParser(lexer, position);
        VariableAssignmentStatement statement = parser.parse();
        if(statement == null) 
        {
            throw new ParseError("Expected an Assignment Expression in a constant assignment", finalKeyword);
        }

        if(!(statement.identifier instanceof IdentifierExpression)) 
        {
            throw new ParseError("Expected an Identfier as a constant name", finalKeyword);
        };

        return new ConstantAssignment(statement.declaration, (IdentifierExpression)statement.identifier, statement.expression);
    };
}
