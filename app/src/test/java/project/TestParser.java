package project;

import java.io.PushbackReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import compiler.Lexer.CompilerLexer;
import compiler.Parser.Expressions.BinaryExpression;
import compiler.Parser.Expressions.NumberExpression;
import compiler.Parser.Parsers.ProgramParser;
import compiler.Parser.Position;
import compiler.Parser.Statements.BlockStatement;
import compiler.Parser.Statements.ForLoopStatement;
import compiler.Parser.Statements.Program;
import compiler.Parser.Statements.Statement;
import compiler.Parser.Statements.StructStatement;
import compiler.Parser.Statements.VariableAssignmentStatement;

public class TestParser {

    private Program parseCode(String code) throws Exception {
        CompilerLexer lexer = new CompilerLexer(new PushbackReader(new StringReader(code)));
        ProgramParser programParser = new ProgramParser(lexer, new Position()); 
        return programParser.parse(); 
    }

    @Test
    public void testVariableDeclarationTree() throws Exception {
        Program root = parseCode("INT myVar = 100;");
        
        assertEquals(1, root.statements.size(), "Program should have 1 statement");
        
        Statement firstStmt = root.statements.get(0);
        assertTrue(firstStmt instanceof VariableAssignmentStatement);
        
        VariableAssignmentStatement varAss = (VariableAssignmentStatement) firstStmt;
        
        assertEquals("myVar", varAss.declaration.identifier.token);
        assertEquals("INT", varAss.declaration.identifierType.value);
        
        assertTrue(varAss.expression instanceof NumberExpression);
        NumberExpression numExpr = (NumberExpression) varAss.expression;
        assertEquals("100", numExpr.value);
    }

    @Test
    public void testCustomForLoopTree() throws Exception {
        Program root = parseCode("for (i; 0 -> 10; i + 1) { INT x = 5; }");
        
        Statement firstStmt = root.statements.get(0);
        assertTrue(firstStmt instanceof ForLoopStatement);
        
        ForLoopStatement forLoop = (ForLoopStatement) firstStmt;
        
        assertEquals("i", forLoop.loopIdentifier.token);
        
        assertNotNull(forLoop.rangeExpression);
        assertTrue(forLoop.rangeExpression.leftOperand instanceof NumberExpression);
        assertTrue(forLoop.rangeExpression.rightOperand instanceof NumberExpression);
        
        assertNotNull(forLoop.body);
        assertTrue(forLoop.body instanceof BlockStatement);
        BlockStatement block = (BlockStatement) forLoop.body;
        assertEquals(1, block.statements.size());
    }

    @Test
    public void testOperatorPrecedence() throws Exception {
        Program root = parseCode("INT result = 5 + 10 * 2;");
        
        VariableAssignmentStatement varAss = (VariableAssignmentStatement) root.statements.get(0);
        
        
        assertTrue(varAss.expression instanceof BinaryExpression);
        BinaryExpression rootExpr = (BinaryExpression) varAss.expression;
        assertEquals("+", rootExpr.operator.token);
        
        assertTrue(rootExpr.leftOperand instanceof NumberExpression);
        
        assertTrue(rootExpr.rightOperand instanceof BinaryExpression);
        BinaryExpression rightExpr = (BinaryExpression) rootExpr.rightOperand;
        assertEquals("*", rightExpr.operator.token);
    }

    @Test
    public void testStructParsing() throws Exception {
        Program root = parseCode("coll Vector { INT x; INT y; }");
        
        Statement firstStmt = root.statements.get(0);
        assertTrue(firstStmt instanceof StructStatement);
        
        StructStatement structStmt = (StructStatement) firstStmt;
        assertEquals("Vector", structStmt.identifier.token);
        
        assertEquals(2, structStmt.structProperties.size());
    }
}