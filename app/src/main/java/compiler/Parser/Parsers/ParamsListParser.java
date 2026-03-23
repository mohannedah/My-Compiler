package compiler.Parser.Parsers;

import java.util.ArrayList;
import java.util.List;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Seperator;
import compiler.Parser.Position;
import compiler.Parser.Expressions.Expression;

/*
    <METHOD_INVOCATION_PARAMS> ::= <EXPRESSION> | <EXPRESSION> ',' <METHOD_INVOCATION_PARAMS> | <EPSILON>
*/

public class ParamsListParser extends Parser {
    public ParamsListParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }
    public List<Expression> parse() throws Exception
    {
        // Parse the parameters ->
        ExpressionParser expressionParser = new ExpressionParser(lexer, this.position);
        Expression currExpression = expressionParser.parse();
        List<Expression> params = new ArrayList<Expression>();
        while(currExpression != null) {
            params.addFirst(currExpression);
            Seperator comma = this.readSeperator(",");
            if(comma == null) break;
            currExpression = expressionParser.parse(); 
        };
        return params;
    };
}
