package compiler.Parser.Parsers;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Identifier;
import compiler.Lexer.Tokens.Operator;
import compiler.Parser.Exceptions.ParseError;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.Expressions.IdentifierExpression;
import compiler.Parser.Position;
import compiler.Parser.Statements.VariableAssignmentStatement;
import compiler.Parser.Statements.VariableDeclarationStatement;

/*
    <DIERECT_ASSIGNMENT> ::= <FIELD> '=' <EXPRESSION>
    <FIELD> ::= <OBJECT_ACCESSOR> | <ARRAY_ACCESSOR> | <IDENTIFIER> | <FIELD>
    <VARIABLE_ASSIGNMENT_STATEMENT> ::= <VARIABLE_DECLARATION> '=' <EXPRESSION> | <DIRECT_ASSIGNMENT>
*/

public class VariableAssignmentParser extends Parser {
    public VariableAssignmentParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }

    public VariableAssignmentStatement parse() throws Exception 
    {
        int startingPosition = this.position.position;
        
        VariableAssignmentStatement assignment = this.parseRuleOne();
        if (assignment != null) return assignment;
        
        this.resetPosition(startingPosition);
        
        return this.parseRuleTwo();
    };

    public VariableAssignmentStatement parseRuleOne() throws Exception
    {
        VariableDeclarationParser parser = new VariableDeclarationParser(lexer, position);
        VariableDeclarationStatement declaration = parser.parse();
        if(declaration == null) return null;
        Operator assignmentOperator = this.readOperator("=");
        if(assignmentOperator == null) 
        {
            return null;
        }
        ExpressionParser expressionParser = new ExpressionParser(lexer, position);
        Expression expression = expressionParser.parse();
        if(expression == null) 
        {
            throw new ParseError("Expected an expression in a Variable Assignment", declaration.identifier);
        }
        return new VariableAssignmentStatement(declaration, declaration.identifier, expression);
    };
    
   private VariableAssignmentStatement parseRuleTwo() throws Exception 
    {
        Identifier identifier = this.readIdentifier();
        if(identifier == null) return null;
        Expression targetOperand = new IdentifierExpression(identifier);  
        while(true) 
        {
            int currentPosition = this.position.position;
            ObjectAccessorTailParser objectTailParser = new ObjectAccessorTailParser(lexer, position);
            Expression newExpression = objectTailParser.parse(targetOperand);
            if(newExpression != null) {
                targetOperand = newExpression;
                continue;
            }
            this.resetPosition(currentPosition);
            
            ArrayAccessorTailParser arrayTailParser = new ArrayAccessorTailParser(lexer, position);
            newExpression = arrayTailParser.parse(targetOperand);
            if(newExpression != null) 
            {
                targetOperand = newExpression;
                continue;
            }
            this.resetPosition(currentPosition);
            break;
        }
        
        Operator assignmentOperator = this.readOperator("=");
        if(assignmentOperator == null) 
        {
            return null;
        }

        ExpressionParser expressionParser = new ExpressionParser(lexer, position);
        Expression expression = expressionParser.parse();
        if(expression == null) 
        {
            throw new ParseError("Expected an expression after '='.", this.lexer.getAtPosition(this.position.position - 1));
        }

        return new VariableAssignmentStatement(null, targetOperand, expression);
    }
}
