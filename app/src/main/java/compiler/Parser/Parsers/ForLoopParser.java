package compiler.Parser.Parsers;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Brackets;
import compiler.Lexer.Tokens.Identifier;
import compiler.Lexer.Tokens.Keyword;
import compiler.Lexer.Tokens.Seperator;
import compiler.Parser.Position;
import compiler.Parser.Exceptions.ParseError;
import compiler.Parser.Exceptions.UnmatchingBracketError;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.Expressions.RangeExpression;
import compiler.Parser.Statements.BlockStatement;
import compiler.Parser.Statements.ForLoopStatement;

/*
    <FOR_STATEMENT> ::= 'for' '(' <IDENTIFIER> ';' <RANGE_EXPRESSION> ';' <EXPRESSION> ')' <BLOCK_STATEMENT>
*/

public class ForLoopParser extends Parser {
    public ForLoopParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }

    public ForLoopStatement parse() throws Exception 
    {
        Keyword forKeyword = this.readKeyword("for");
        if (forKeyword == null) return null;

        Brackets leftParen = this.readBracket("(");
        if (leftParen == null) {
            throw new ParseError("Expected '(' after 'for' keyword.", forKeyword);
        }

        ExpressionParser exprParser = new ExpressionParser(lexer, this.position);
        Identifier identifier = this.readIdentifier();
        if (identifier == null) {
            throw new ParseError("Expected initialization expression in for loop.", this.lexer.getAtPosition(this.position.position - 1));
        }

        Seperator firstSemi = this.readSeperator(";");
        if (firstSemi == null) {
            throw new ParseError("Expected ';' after initialization expression.", this.lexer.getAtPosition(this.position.position - 1));
        }

        RangeExpressionParser rangeParser = new RangeExpressionParser(lexer, this.position);
        RangeExpression rangeExpression = rangeParser.parse();
        if (rangeExpression == null) {
            throw new ParseError("Expected a range expression (e.g., i -> 10) in for loop condition.", firstSemi);
        }

        Seperator secondSemi = this.readSeperator(";");
        if (secondSemi == null) {
            throw new ParseError("Expected ';' after range expression.", this.lexer.getAtPosition(this.position.position - 1));
        }

        Expression updateExpression = exprParser.parse();
        if (updateExpression == null) {
            throw new ParseError("Expected update expression in for loop.", secondSemi);
        }

        Brackets rightParen = this.readBracket(")");
        if (rightParen == null) {
            throw new UnmatchingBracketError(")", this.lexer.getAtPosition(this.position.position - 1));
        }

        BlockStatementParser blockParser = new BlockStatementParser(lexer, this.position);
        BlockStatement body = blockParser.parse();
        if (body == null) {
            throw new ParseError("Expected block '{...}' after for loop declaration.", rightParen);
        }

        return new ForLoopStatement(identifier, rangeExpression, updateExpression, body);
    }
}
