package compiler.Parser.Parsers;


import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Seperator;
import compiler.Parser.Exceptions.ParseError;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.Position;
import compiler.Parser.Statements.ForLoopStatement;
import compiler.Parser.Statements.MethodDefinitionStatement;
import compiler.Parser.Statements.Statement;

/*
    <STATEMENT> ::= 
        <STRUCT_DECLARATION> 
        |
        <VARIABLE_ASSIGNMENT_STATEMENT>
        |
        <CONST_ASSIGNMENT_STATEMENT>
        |
        <VARIABLE_DECLARATION>
        |
        | <FUNCTION_DECLARATION>
        | <IF_STATEMENT>
        | <FOR_LOOP>
        | <WHILE_LOOP>
        | <RETURN_STATEMENT>
*/

public class StatementParser extends Parser {
    public StatementParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }
    
    public Statement parse() throws Exception
    {
        int startingIndex = this.position.position;
        Statement parsedStatement;

        parsedStatement = this.parseRuleOne();
        if (parsedStatement != null) return parsedStatement;
        this.resetPosition(startingIndex); 

        parsedStatement = this.parseRuleTwo();
        if (parsedStatement != null) return parsedStatement;
        this.resetPosition(startingIndex);

        parsedStatement = this.parseRuleThree();
        if (parsedStatement != null) return parsedStatement;
        this.resetPosition(startingIndex);

        parsedStatement = this.parseRuleFour();
        if (parsedStatement != null) return parsedStatement;
        this.resetPosition(startingIndex);

        parsedStatement = this.parseRuleFive();
        if (parsedStatement != null) {
            return parsedStatement;
        } 
        this.resetPosition(startingIndex);

        parsedStatement = this.parseRuleSix();
        if (parsedStatement != null) return parsedStatement;
        this.resetPosition(startingIndex);

        parsedStatement = this.parseRuleSeven();
        if (parsedStatement != null) return parsedStatement;
        this.resetPosition(startingIndex);


        parsedStatement = this.parseRuleEight();
        if (parsedStatement != null) return parsedStatement;
        this.resetPosition(startingIndex);

        parsedStatement = this.parseRuleNine();
        if(parsedStatement != null) return parsedStatement;
        this.resetPosition(startingIndex);

        parsedStatement = this.parseRuleTen();
        if(parsedStatement != null) return parsedStatement;
        this.resetPosition(startingIndex);
        parsedStatement = this.parseRuleEleven();
        return parsedStatement;
    };

    public Statement parseRuleOne() throws Exception
    {
        StructParser parser = new StructParser(lexer, position);
        return parser.parse();
    };

    public Statement parseRuleTwo() throws Exception
    {
        VariableAssignmentParser parser = new VariableAssignmentParser(lexer, position); 
        Statement statement = parser.parse();
        if(statement == null) return null;
        Seperator seperator = this.readSeperator(";");
        if(seperator == null) 
        {
            throw new ParseError("Expeced a `;` at the end of a variable assignment statement.", this.lexer.getAtPosition(this.position.position - 1));
        }; 
        return statement;
    };

    public Statement parseRuleThree() throws Exception
    {
        ConstantAssignmentParser parser = new ConstantAssignmentParser(lexer, position);
        Statement statement = parser.parse();
        if(statement == null) return null;
        Seperator seperator = this.readSeperator(";");
        if(seperator == null) 
        {
            throw new ParseError("Expeced a `;` at the end of a constant assignment statement.", this.lexer.getAtPosition(this.position.position - 1));
        }; 
        return statement;
    };

    public Statement parseRuleFour() throws Exception
    {
        VariableDeclarationParser parser = new VariableDeclarationParser(lexer, position);
        Statement statement = parser.parse();
        if(statement == null) return null;
        Seperator seperator = this.readSeperator(";");
        if(seperator == null) 
        {
            throw new ParseError("Expeced a `;` at the end of a variable declaration statement.", this.lexer.getAtPosition((this.position.position - 1)));
        }; 
        return statement;
    };

    public Statement parseRuleFive() throws Exception
    {
        MethodDeclarationParser parser = new MethodDeclarationParser(lexer, position); 
        return parser.parse();
    };

    public Statement parseRuleSix() throws Exception 
    {
        IfStatementParser parser = new IfStatementParser(lexer, position);
        return parser.parse();
    };

    public ForLoopStatement parseRuleSeven() throws Exception 
    {
        ForLoopParser parser = new ForLoopParser(lexer, position);
        return parser.parse();
    };

    public Statement parseRuleEight() throws Exception 
    {
        WhileLoopParser parser = new WhileLoopParser(lexer, position);
        return parser.parse();
    };

    public Statement parseRuleNine() throws Exception
    {
        ReturnStatementParser parser = new ReturnStatementParser(lexer, position);
        Statement statement = parser.parse();
        if(statement == null) return null;
        Seperator seperator = this.readSeperator(";");
        if(seperator == null) 
        {
            throw new ParseError("Expeced a `;` at the end of a return statement.", this.lexer.getAtPosition((this.position.position - 1)));
        };
        return statement; 
    };

    public Statement parseRuleTen() throws Exception 
    {
        return this.parseComment();
    };

    public Statement parseRuleEleven() throws Exception 
    {
        ExpressionParser parser = new ExpressionParser(lexer, position);
        Expression expression = parser.parse();
        if(expression == null) return null;
        Seperator seperator = this.readSeperator(";");
        if(seperator == null) 
        {
            throw new ParseError("Expeced a `;` at the end of an expression.", this.lexer.getAtPosition((this.position.position - 1)));
        };
        return expression;
    };
}
