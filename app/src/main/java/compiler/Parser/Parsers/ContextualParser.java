package compiler.Parser.Parsers;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Stack;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Token;
import compiler.Parser.Exceptions.NoMoreTokensException;

public class ContextualParser {
    protected Stack<Token> readTokens;
    private CompilerLexer lexer;

    public ContextualParser(CompilerLexer lexer) 
    {
        this.lexer = lexer;
        this.readTokens = new Stack<Token>();
    };

    public ContextualParser(CompilerLexer lexer, Stack<Token> readTokens) 
    {
       this.lexer = lexer;
       this.readTokens = readTokens;  
    };

    public Token nextToken() throws Exception
    {
        try {
            Token currToken = this.lexer.nextToken();
            readTokens.push(currToken);
            return currToken;
        } catch (NoSuchElementException e) {
            throw new NoMoreTokensException();
        }        
    };

    public void rollback() throws IOException
    {
        while(!readTokens.isEmpty()) 
        {
            Token currToken = readTokens.pop();
            this.lexer.resetToken(currToken);
        };
    };
}
