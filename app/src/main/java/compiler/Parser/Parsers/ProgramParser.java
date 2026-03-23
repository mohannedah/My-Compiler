package compiler.Parser.Parsers;

import java.util.List;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Token;
import compiler.Parser.ASTNode;
import compiler.Parser.Position;
import compiler.Parser.Exceptions.ParseError;
import compiler.Parser.Statements.Program;
import compiler.Parser.Statements.Statement;

/*
    <PROGRAM> ::= <STATEMENT_LIST> EOF
*/

public class ProgramParser extends Parser {
    
    public ProgramParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }

    public Program parse() throws Exception 
    {
        StatementListParser listParser = new StatementListParser(lexer, this.position);
        List<Statement> statements = listParser.parse(); 
        Token leftoverToken = this.getCurrToken();
        if (leftoverToken != null) {
            throw new ParseError(
                "Unexpected token at the top level. Could not parse as a valid statement.", 
                leftoverToken
            );
        }
        return new Program(statements);
    }
}