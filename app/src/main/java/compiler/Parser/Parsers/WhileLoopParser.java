package compiler.Parser.Parsers;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Brackets;
import compiler.Lexer.Tokens.Keyword;
import compiler.Parser.Exceptions.ParseError;
import compiler.Parser.Exceptions.UnmatchingBracketError;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.Position;
import compiler.Parser.Statements.BlockStatement;
import compiler.Parser.Statements.WhileLoopStatement;

/*
    <WHILE_LOOP> ::= 'while' '(' <EXPRESSION> ')'
*/

public class WhileLoopParser extends Parser {
    public WhileLoopParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }

    public WhileLoopStatement parse() throws Exception
    {
        Keyword whileKeyword = this.readKeyword("while");
        if(whileKeyword == null) return null;
        Brackets leftParan = this.readBracket("(");
        if(leftParan == null) 
        {
            throw new ParseError("Expected Paranthesis to enclose the expression for the while loop", whileKeyword);
        };
        ExpressionParser parser = new ExpressionParser(lexer, position);
        Expression expression = parser.parse();

        if(expression == null) 
        {
            throw new ParseError("Expected an expression for the while loop", whileKeyword);
        }

        Brackets rightParan = this.readBracket(")");

        if(rightParan == null) 
        {
            throw new UnmatchingBracketError(")", this.lexer.getAtPosition(this.position.position - 1));
        }

        BlockStatement body = this.parseBlock();

        if(body == null) 
        {
            throw new ParseError("Expected a block statement '{ ... }' after 'while' condition.", this.lexer.getAtPosition(this.position.position - 1));
        };

        return new WhileLoopStatement(expression, body);
    };
}
