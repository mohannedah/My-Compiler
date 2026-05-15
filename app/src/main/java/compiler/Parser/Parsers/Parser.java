package compiler.Parser.Parsers;

import java.util.NoSuchElementException;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Brackets;
import compiler.Lexer.Tokens.Comment;
import compiler.Lexer.Tokens.Identifier;
import compiler.Lexer.Tokens.Keyword;
import compiler.Lexer.Tokens.ObjectAccessor;
import compiler.Lexer.Tokens.Operator;
import compiler.Lexer.Tokens.Seperator;
import compiler.Lexer.Tokens.StringToken;
import compiler.Lexer.Tokens.Token;
import compiler.Lexer.Tokens.Type;
import compiler.Parser.Expressions.BooleanExpression;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.Expressions.IdentifierExpression;
import compiler.Parser.Expressions.StringExpression;
import compiler.Parser.Position;
import compiler.Parser.Statements.BlockStatement;
import compiler.Parser.Statements.CommentStatement;

public class Parser {
    public CompilerLexer lexer;
    public Position position;
    
    public Parser(CompilerLexer lexer, Position position) {
        this.lexer = lexer;
        this.position = position;
    }

    protected Token getCurrToken() throws Exception 
    {
        try {
            return this.lexer.getAtPosition(this.position.position);    
        } catch (NoSuchElementException e) {
        }
        return null;
    };

    protected Token consumeToken() throws Exception 
    {
        Token token = this.getCurrToken();
        this.position.position += 1;
        return token;
    }

    protected Expression parseIdentifier() throws Exception 
    {
        Token token = this.getCurrToken();
        if(token == null) return null; 
        if(token.getClass().getSimpleName().equals(Identifier.class.getSimpleName())) 
        {
            if(token.token.equals("true") || token.token.equals("false")) {
                return new BooleanExpression(this.consumeToken());
            }
            Identifier identifier = (Identifier)this.consumeToken();
            return new IdentifierExpression(identifier);
        }
        return null;
    };

    protected StringExpression parseString() throws Exception
    {
        Token token = this.getCurrToken();
        if(token == null) return null; 
        if(token.getClass().getSimpleName().equals(StringToken.class.getSimpleName())) 
        {
            StringToken stringToken = (StringToken)this.consumeToken();
            return new StringExpression(stringToken);
        };
        return null;
    };

    protected Brackets readBracket(String expectedBracket) throws Exception 
    {
        Token token = this.getCurrToken();

        if(token == null) return null;

        if(token.token.equals(expectedBracket)) 
        {
            Brackets bracket = (Brackets)this.consumeToken();
            return bracket;
        };
        return null;
    };

    protected Seperator readSeperator(String exprectedSeperator) throws Exception 
    {
        Token token = this.getCurrToken();
        if(token != null && token.token.equals(exprectedSeperator)) 
        {
            Seperator sep = (Seperator)this.consumeToken();
            return sep;
        };
        return null;
    };

    protected ObjectAccessor readAccessor() throws Exception 
    {
        Token token = this.getCurrToken();
        if(token instanceof ObjectAccessor) 
        {
            consumeToken();
            return (ObjectAccessor)token;
        }
        return null;
    };

    protected Token readType() throws Exception 
    {
        Token token = this.getCurrToken();
        if(token == null) return null;
        if(token instanceof Type || token instanceof Identifier) 
        {
            return consumeToken();
        };
        return null;
    };

    protected Keyword readKeyword(String keyword) throws Exception 
    {
        Token token = this.getCurrToken();
        if(token == null) return null;
        if(token instanceof Keyword && token.token.equals(keyword)) 
        {
            return (Keyword)consumeToken();
        };
        return null;
    };

    protected Identifier readIdentifier() throws Exception 
    {
        Token token = this.getCurrToken();
        if(token == null) return null; 
        if(token.getClass().getSimpleName().equals(Identifier.class.getSimpleName())) 
        {
            Identifier identifier = (Identifier)this.consumeToken();
            return identifier;
        }
        return null;
    };

    protected Operator readOperator(String expectedOperator) throws Exception 
    {
        Token token = this.getCurrToken();
        if(token == null) return null; 
        if(token instanceof Operator) 
        {
            Operator operator = (Operator)this.consumeToken();
            return operator;
        }
        return null;
    };

    protected BlockStatement parseBlock() throws Exception 
    {
        BlockStatementParser blockParser = new BlockStatementParser(lexer, this.position);
        BlockStatement body = blockParser.parse();
        return body;
    };

    protected CommentStatement parseComment() throws Exception 
    {
        Token token = this.getCurrToken();
        if(token != null && token instanceof Comment) 
        {
            return new CommentStatement((Comment)consumeToken());
        };
        return null;
    };

    public void resetPosition(int newPosition) 
    {
        this.position.position = newPosition;
    };
}
