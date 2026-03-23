package compiler.Parser.Parsers;


import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Brackets;
import compiler.Parser.Position;
import compiler.Parser.Exceptions.ParseError;
import compiler.Parser.Exceptions.UnmatchingBracketError;
import compiler.Parser.Expressions.ArrayAccessorExpression;
import compiler.Parser.Expressions.Expression;

/*
    <CANDIDATE> ::= <OBJECT_ACCESSOR> | <METHOD_INVOCATION> | <INDEXING_EXPRESSION> | <IDENTIFIER>  
    <INDEXING_EXPRESSION> ::= <CANDIDATE> '[' <EXPRESSION> ']'
*/

public class ArrayAccessorTailParser extends Parser {
    public ArrayAccessorTailParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }

    public ArrayAccessorExpression parse(Expression leftNode) throws Exception
    {
        Brackets leftBracket = this.readBracket("[");
        if(leftBracket == null) 
        {
            return null;
        };
        ExpressionParser expressionParser = new ExpressionParser(lexer, this.position);
        Expression parsedExpression = expressionParser.parse();

        if(parsedExpression == null) 
        {
            throw new ParseError("Expected an Expression as an Array Index.", this.lexer.getAtPosition(this.position.position - 1));
        }
        Brackets rightBracket = this.readBracket("]");

        if(rightBracket == null) 
        {
            throw new UnmatchingBracketError(")", this.lexer.getAtPosition(this.position.position - 1));
        };
        return new ArrayAccessorExpression(leftNode, parsedExpression);
    };
}
