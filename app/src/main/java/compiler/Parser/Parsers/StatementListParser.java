package compiler.Parser.Parsers;

import java.util.ArrayList;
import java.util.List;

import compiler.Lexer.CompilerLexer;
import compiler.Parser.Position;
import compiler.Parser.Statements.Statement;

/*
    <StatementListParser> ::= <StatementListParser> | <Statement> | EPSILON
*/

public class StatementListParser extends Parser {
    public StatementListParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }

    public List<Statement> parse() throws Exception 
    {
        List<Statement> statements = new ArrayList<>();
        StatementParser statementParser = new StatementParser(lexer, this.position);
        Statement parsedStatement = statementParser.parse();
        while (parsedStatement != null) 
        {
            int startingPosition = this.position.position;
            statements.addLast(parsedStatement);
            parsedStatement = statementParser.parse();
            if(parsedStatement == null) {
                this.resetPosition(startingPosition);
            }
        }
        return statements;
    }
}