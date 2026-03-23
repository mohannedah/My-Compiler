package compiler.Parser.Parsers;

import java.util.ArrayList;
import java.util.List;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Brackets;
import compiler.Lexer.Tokens.Keyword;
import compiler.Lexer.Tokens.Token;
import compiler.Parser.Position;
import compiler.Parser.Exceptions.ParseError;
import compiler.Parser.Exceptions.UnmatchingBracketError;
import compiler.Parser.Statements.IfStatement;
import compiler.Parser.Statements.Statement;
import compiler.Parser.Statements.BlockStatement;
import compiler.Parser.Statements.ElseBranch;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.Statements.ElseIfBranch;

public class IfStatementParser extends Parser {
    
    public IfStatementParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }

    public IfStatement parse() throws Exception 
    {
        Keyword ifToken = this.readKeyword("if");
        if (ifToken == null) return null; 

        Brackets leftParen = this.readBracket("(");
        if (leftParen == null) {
             throw new ParseError("Expected '(' after 'if' keyword.", ifToken);
        }

        ExpressionParser exprParser = new ExpressionParser(lexer, this.position);
        Expression mainCondition = exprParser.parse();
        if (mainCondition == null) throw new ParseError("Expected an expression for the if-condition.", this.lexer.getAtPosition(this.position.position - 1));
        
        Brackets rightParen = this.readBracket(")");
        if (rightParen == null) {
            throw new UnmatchingBracketError(")", this.lexer.getAtPosition(this.position.position - 1));
        }

        BlockStatementParser blockParser = new BlockStatementParser(lexer, this.position);
        BlockStatement mainThenBlock = blockParser.parse();
        if (mainThenBlock == null) throw new ParseError("Expected block '{...}' after 'if'.", this.lexer.getAtPosition(this.position.position - 1));

        List<ElseIfBranch> elseIfBranches = new ArrayList<>();
        ElseBranch finalElseBlock = null;
        while (true) 
        {
            int startingPosition = this.position.position;
            ElseIfBranch elseIfBranch = (ElseIfBranch)this.parseElseIf();
            if (elseIfBranch != null) {
                elseIfBranches.add(elseIfBranch);
                continue;
            }
            this.resetPosition(startingPosition);
            ElseBranch elseBranch = (ElseBranch)this.parseElse();
            if (elseBranch != null) {
                finalElseBlock = elseBranch;
                break;
            }
            break;
        }

        return new IfStatement(mainCondition, mainThenBlock, elseIfBranches, finalElseBlock);
    }
    private Statement parseElseIf() throws Exception
    {
        Keyword elseIfKeyword = this.readKeyword("elseif");
        if(elseIfKeyword == null) return null;
        Brackets leftParan = this.readBracket("(");
        if(leftParan == null) {
            throw new ParseError("Expected a `(` to enclose the expression in an IF condition", elseIfKeyword);
        }
        ExpressionParser parser = new ExpressionParser(lexer, position);
        Expression expression = parser.parse();
        if(expression == null) 
        {
            throw new ParseError("Expected an expression for the else-if-condition", this.lexer.getAtPosition(this.position.position - 1));  
        };
        Brackets rightParan = this.readBracket(")");
        if(rightParan == null) {
            throw new UnmatchingBracketError(")", this.lexer.getAtPosition(this.position.position - 1));
        };
        BlockStatementParser blockStatementParser = new BlockStatementParser(lexer, position);
        BlockStatement statement = blockStatementParser.parse();
        return new ElseIfBranch(expression, statement);
    };

    private Statement parseElse() throws Exception 
    {
        Keyword elseKeyword = this.readKeyword("else");
        if(elseKeyword == null) return null;
        BlockStatementParser blockStatementParser = new BlockStatementParser(lexer, position);
        BlockStatement statement = blockStatementParser.parse();
        return new ElseBranch(statement);
    };
}