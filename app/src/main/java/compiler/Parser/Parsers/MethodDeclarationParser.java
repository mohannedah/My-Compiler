package compiler.Parser.Parsers;

import java.util.ArrayList;
import java.util.List;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Brackets;
import compiler.Lexer.Tokens.Identifier;
import compiler.Lexer.Tokens.Keyword;
import compiler.Lexer.Tokens.Seperator;
import compiler.Parser.Position;
import compiler.Parser.Exceptions.ParseError;
import compiler.Parser.Exceptions.UnmatchingBracketError;
import compiler.Parser.Statements.BlockStatement;
import compiler.Parser.Statements.IdentifierType;
import compiler.Parser.Statements.MethodDefinitionStatement;
import compiler.Parser.Statements.VariableDeclarationStatement;

/*
    <METHOD_PARAMS> ::= <METHOD_PARAM> ',' <METHOD_PARAMS> | <METHOD_PARAM>
    <METHOD_DEFINITION> ::= def <TYPE> <IDENTIFIER> '(' <METHOD_PARAMS> ')' <BLOCK_SCOPE> | def <IDENTIFIER> '(' <METHOD_PARAMS> ')' <BLOCK_SCOPE> 
*/

public class MethodDeclarationParser extends Parser {
    public MethodDeclarationParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }

    public MethodDefinitionStatement parse() throws Exception
    {
        int startingPosition = this.position.position;
        MethodDefinitionStatement statement = this.parseRuleOne();
        if(statement != null) return statement;
        this.resetPosition(startingPosition);
        statement = this.parseRuleTwo();
        return statement;
    };

    public List<VariableDeclarationStatement> parseParams() throws Exception 
    {
        ArrayList<VariableDeclarationStatement> params = new ArrayList<>();
        VariableDeclarationParser parser = new VariableDeclarationParser(lexer, position);
        VariableDeclarationStatement statement = parser.parse();

        while(statement != null) 
        {
            params.addLast(statement);
            Seperator sep = this.readSeperator(",");
            if(sep == null) break;
            statement = parser.parse();
        };
        return params;
    };

    public MethodDefinitionStatement parseRuleOne() throws Exception 
    {

        Keyword defKeyword = this.readKeyword("def");

        if(defKeyword == null) return null;

        TypeParser typeParser = new TypeParser(lexer, position);
        IdentifierType returnType = typeParser.parse();

        if(returnType == null) 
        {
            return null;
        };

        Identifier identifier = this.readIdentifier();

        if(identifier == null) return null;

        Brackets leftParan = this.readBracket("(");

        if(leftParan == null )
        {
            throw new ParseError("Expected a `(` to list the method paramaters", identifier);
        }

        List<VariableDeclarationStatement> params = this.parseParams();

        Brackets rightParan = this.readBracket(")");

        if(rightParan == null) 
        {
            throw new UnmatchingBracketError(")", this.lexer.getAtPosition(this.position.position - 1));
        };
        BlockStatementParser blockParser = new BlockStatementParser(lexer, this.position);
        BlockStatement body = blockParser.parse();
        if (body == null) {
            throw new ParseError("Expected block '{...}' after for method definition.", this.lexer.getAtPosition(this.position.position - 1));
        }
        return new MethodDefinitionStatement(identifier, returnType, params, body);
    };

    public MethodDefinitionStatement parseRuleTwo() throws Exception 
    {
        Keyword defKeyword = this.readKeyword("def");

        if(defKeyword == null) return null;

        Identifier identifier = this.readIdentifier();

        if(identifier == null) 
        {
            throw new ParseError("Expected an identifer for the new Method", defKeyword);
        };

        Brackets leftParan = this.readBracket("(");

        if(leftParan == null )
        {
            throw new ParseError("Expected a `(` to list the method paramaters", identifier);
        }

        List<VariableDeclarationStatement> params = this.parseParams();

        Brackets rightParan = this.readBracket(")");

        if(rightParan == null) 
        {
            throw new UnmatchingBracketError(")", this.lexer.getAtPosition(this.position.position - 1));
        };
        BlockStatement body = this.parseBlock();
        if (body == null) {
            throw new ParseError("Expected block '{...}' after for method definition.", this.lexer.getAtPosition(this.position.position - 1));
        }
        return new MethodDefinitionStatement(identifier, new IdentifierType("void", false), params, body);
    };
}
