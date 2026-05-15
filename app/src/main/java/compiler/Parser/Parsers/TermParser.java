package compiler.Parser.Parsers;

import java.util.NoSuchElementException;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Brackets;
import compiler.Lexer.Tokens.Keyword;
import compiler.Lexer.Tokens.NumberToken;
import compiler.Lexer.Tokens.Token;
import compiler.Parser.Exceptions.ParseError;
import compiler.Parser.Exceptions.UnmatchingBracketError;
import compiler.Parser.Expressions.ArrayDeclarationExpression;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.Expressions.NumberExpression;
import compiler.Parser.Position;
import compiler.Parser.Statements.IdentifierType;

/*
    <TERM> ::= 
          <NUMBER> 
        | <STRING>
        | <TYPE> 'ARRAY' '[' <EXPRESSION> ']'
        '(' <EXPRESSION> ')'
        | 
        | <METHOD_INVOCATION> 
        | <ARRAY_ACCESSOR> 
        | <OBJECT_ACCESSOR> 
        | <IDENTIFIER>  
*/

public class TermParser extends ExpressionParser  {

    public TermParser(CompilerLexer lexer, Position position) 
    {
        super(lexer, position);
    };

    public Expression parse() throws Exception 
    {
        int startingPosition = this.position.position;
        Expression leftOperand = null;

        leftOperand = this.parseRuleOne();
        
        if (leftOperand == null) {
            this.resetPosition(startingPosition);
            leftOperand = this.parseRuleTwo();
        }
        
        if (leftOperand == null) {
            this.resetPosition(startingPosition);
            leftOperand = this.parseRuleThree();
        }
        
        if(leftOperand == null) {
            this.resetPosition(startingPosition);
            leftOperand = this.parseRuleFour();
        };

        if (leftOperand == null) {
            this.resetPosition(startingPosition);
            leftOperand = this.parseIdentifier();
        }

        if (leftOperand == null) {
            return null;
        }

        try {
            while (this.validateToken(this.getCurrToken())) 
            {
                Token currToken = this.getCurrToken();
                if (currToken.token.equals("(")) {
                    MethodInvocationTailParser methodParser = new MethodInvocationTailParser(lexer, this.position);
                    leftOperand = methodParser.parse(leftOperand);
                } 
                else if (currToken.token.equals("[")) {
                    ArrayAccessorTailParser arrayParser = new ArrayAccessorTailParser(lexer, this.position);
                    leftOperand = arrayParser.parse(leftOperand);
                } 
                else if (currToken.token.equals(".")) {
                    ObjectAccessorTailParser objectParser = new ObjectAccessorTailParser(lexer, this.position);
                    leftOperand = objectParser.parse(leftOperand);
                }
            }   
        } catch (NoSuchElementException e) {
            
        }    
        return leftOperand;
    }

    public boolean validateToken(Token token) 
    {
        if(token == null) return false;
        return token.token.equals("(") || token.token.equals("[") || token.token.equals(".");
    };

    public Expression parseRuleOne() throws Exception
    {
        if(this.getCurrToken() instanceof NumberToken) 
        {
            NumberToken number = (NumberToken)this.consumeToken();
            return new NumberExpression(number);
        };
        return null;
    };

    public Expression parseRuleTwo() throws Exception
    {
        return this.parseString();
    };

    public Expression parseRuleThree() throws Exception 
    {
        Brackets paran = this.readBracket("(");
        if(paran == null) return null;
        ExpressionParser expressionParser = new ExpressionParser(lexer, this.position);
        Expression expression = expressionParser.parse();
        if(expression == null) return null;
        paran = this.readBracket(")");
        if(paran == null)  {
            throw new UnmatchingBracketError(")", this.lexer.getAtPosition(this.position.position - 1));
        };
        return expression;
    };

    public Expression parseRuleFour() throws Exception 
    {
        Token token = this.readType();

        if(token == null) return null;

        Keyword keyword = this.readKeyword("ARRAY");

        if(keyword == null) {
            return null;
        }

        Brackets leftParan = this.readBracket("[");

        if(leftParan == null) 
        {
            throw new ParseError("Expected a `[` in the ARRAY declaration.", token);
        }

        ExpressionParser expressionParser = new ExpressionParser(lexer, this.position);
        
        Expression expression = expressionParser.parse();

        if(expression == null) 
        {
            throw new ParseError("Expected an `Expression` inside the Brackets", leftParan);
        };

        Brackets rightParan = this.readBracket("]");

        if(rightParan == null) 
        {
            throw new UnmatchingBracketError("]", this.lexer.getAtPosition(this.position.position - 1));
        };

        return new ArrayDeclarationExpression(expression, new IdentifierType(token.token, false));
    };
}
