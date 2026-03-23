package compiler.Parser.Parsers;
import java.util.List;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Brackets;
import compiler.Parser.Position;
import compiler.Parser.Exceptions.UnmatchingBracketError;
import compiler.Parser.Statements.BlockStatement;
import compiler.Parser.Statements.Statement;

/*
    <BLOCK_STATEMENT> ::= '{' <STATEMENT_LIST> '}'
*/


public class BlockStatementParser extends Parser {
    public BlockStatementParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }
    public BlockStatement parse() throws Exception {
        Brackets leftBraces = this.readBracket("{");
        if(leftBraces == null) return null;
        StatementListParser parser = new StatementListParser(lexer, position);
        List<Statement> statments = parser.parse();
        Brackets rightBraces = this.readBracket("}");
        if(rightBraces == null) 
        {
            throw new UnmatchingBracketError("}", this.lexer.getAtPosition(this.position.position - 1));
        };
        return new BlockStatement(statments);     
    };  
}
