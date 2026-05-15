package project;

import java.io.PushbackReader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import compiler.Lexer.CompilerLexer;

public class TestLexer {

    private CompilerLexer createLexer(String input) {
        return new CompilerLexer(new PushbackReader(new StringReader(input)));
    }

    @Test
    public void testCustomRangeOperator() throws Exception {
        CompilerLexer lexer = createLexer("i -> 10");
        
        var token1 = lexer.nextToken();
        assertEquals("i", token1.token);
        
        var token2 = lexer.nextToken();
        assertEquals("->", token2.token); 
        
        var token3 = lexer.nextToken();
        assertEquals("10", token3.token);
    }

    @Test
    public void testDeclarativeArrayAllocation() throws Exception {
        CompilerLexer lexer = createLexer("INT ARRAY [5]");
        
        assertEquals("INT", lexer.nextToken().token);
        assertEquals("ARRAY", lexer.nextToken().token);
        assertEquals("[", lexer.nextToken().token);
        assertEquals("5", lexer.nextToken().token);
        assertEquals("]", lexer.nextToken().token);
    }

    @Test
    public void testStructAndFinalKeywords() throws Exception {
        CompilerLexer lexer = createLexer("coll Point { final INT x = 0; }");
        
        assertEquals("coll", lexer.nextToken().token);
        assertEquals("Point", lexer.nextToken().token);
        assertEquals("{", lexer.nextToken().token);
        assertEquals("final", lexer.nextToken().token);
        assertEquals("INT", lexer.nextToken().token);
        assertEquals("x", lexer.nextToken().token);
        assertEquals("=", lexer.nextToken().token);
        assertEquals("0", lexer.nextToken().token);
        assertEquals(";", lexer.nextToken().token);
        assertEquals("}", lexer.nextToken().token);
    }

    @Test
    public void testStringAndNumberLiterals() throws Exception {
        CompilerLexer lexer = createLexer("\"Hello World\" 42 3.14");
        
        var strToken = lexer.nextToken();
        assertEquals("Hello World", strToken.token); 
        
        var intToken = lexer.nextToken();
        assertEquals("42", intToken.token);
        
        var floatToken = lexer.nextToken();
        assertEquals("3.14", floatToken.token);
    }

    @Test
    public void testWhitespaceAndNewlinesAreIgnored() throws Exception {
        String code = "if    ( \n \t x <= 5  ) \n\n { \r\n return ; }";
        CompilerLexer lexer = createLexer(code);
        
        assertEquals("if", lexer.nextToken().token);
        assertEquals("(", lexer.nextToken().token);
        assertEquals("x", lexer.nextToken().token);
        assertEquals("<=", lexer.nextToken().token);
        assertEquals("5", lexer.nextToken().token);
        assertEquals(")", lexer.nextToken().token);
        assertEquals("{", lexer.nextToken().token);
        assertEquals("return", lexer.nextToken().token);
        assertEquals(";", lexer.nextToken().token);
        assertEquals("}", lexer.nextToken().token);        
    }

    @Test
    public void testForLoopStructure() throws Exception {
        CompilerLexer lexer = createLexer("for (i; 0 -> Limit; i + 1)");
        
        String[] expectedTokens = {
            "for", "(", "i", ";", "0", "->", "Limit", ";", "i", "+", "1", ")"
        };
        
        for (String expected : expectedTokens) {
            var token = lexer.nextToken();
            assertNotNull("Lexer ran out of tokens.", token);
            assertEquals(expected, token.token);
        }
    }
}