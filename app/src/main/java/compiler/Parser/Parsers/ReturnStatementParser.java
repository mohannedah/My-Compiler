package compiler.Parser.Parsers;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Keyword;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.Expressions.ReturnStatement;
import compiler.Parser.Position;

/*
    <RETURN_STATEMENT> ::= 'return' <EXPRESSION> | 'return'
*/

public class ReturnStatementParser extends Parser {
    public ReturnStatementParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }

    public ReturnStatement parse() throws Exception
    {
        int startingPosition = this.position.position;
        ReturnStatement statement = this.parseRuleOne();
        if(statement != null) return statement;
        this.resetPosition(startingPosition);
        return this.parseRuleTwo();    
    };

    public ReturnStatement parseRuleOne() throws Exception
    {
        Keyword returnKeyword = this.readKeyword("return");
        if(returnKeyword == null) return null;
        ExpressionParser parser = new ExpressionParser(lexer, position);
        Expression expression = parser.parse();
        if(expression == null) return null;
        return new ReturnStatement(expression);
    };

    public ReturnStatement parseRuleTwo() throws Exception
    {
        Keyword returnKeyword = this.readKeyword("return");
        if(returnKeyword == null) return null;
        return new ReturnStatement(null);
    };
}
