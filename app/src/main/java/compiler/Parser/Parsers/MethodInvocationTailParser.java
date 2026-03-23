package compiler.Parser.Parsers;

import java.util.List;

import compiler.Lexer.CompilerLexer;
import compiler.Lexer.Tokens.Brackets;
import compiler.Parser.Exceptions.UnmatchingBracketError;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.Expressions.MethodInvocationExpression;
import compiler.Parser.Position;

/*
    <METHOD_INVOCATION_EXPRESSION> ::= <IDENTIFIER>'('<METHOD_INVOCATION_PARAMS>')'
*/

public class MethodInvocationTailParser extends Parser {
    public MethodInvocationTailParser(CompilerLexer lexer, Position position) {
        super(lexer, position);
    }

    public MethodInvocationExpression parse(Expression leftOperand) throws Exception
    {
        Brackets paran = this.readBracket("(");
        if(paran == null) return null;
        ParamsListParser paramsListParser = new ParamsListParser(this.lexer, this.position);
        List<Expression> paramsList = paramsListParser.parse();
        paran = this.readBracket(")");
        if(paran == null) 
        {
            throw new UnmatchingBracketError(")", paran);
        }
        return new MethodInvocationExpression(leftOperand, paramsList);
    };
}
