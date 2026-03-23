package compiler.Lexer.TokenFactories;

import compiler.Constants;
import compiler.Lexer.Tokens.AdditionOperator;
import compiler.Lexer.Tokens.BooleanOperator;
import compiler.Lexer.Tokens.ComparisonOperator;
import compiler.Lexer.Tokens.DivisionOperator;
import compiler.Lexer.Tokens.MultiplyOperator;
import compiler.Utils;
import compiler.Lexer.Tokens.Operator;
import compiler.Lexer.Tokens.RangeOperator;
import compiler.Lexer.Tokens.RemainderOperator;
import compiler.Lexer.Tokens.SubtractionOperator;
import compiler.Lexer.Tokens.Token;

public class OperatorFactory implements TokenFactory {
    @Override
    public Token create(String token) {
        if(token.equals("+")) {
            return new AdditionOperator(token);
        } else if(token.equals("-")){
            return new SubtractionOperator("-");
        } else if (token.equals("/")) {
            return new DivisionOperator("/");
        } else if(token.equals("*")) {
            return new MultiplyOperator(token);
        } else if(token.equals("%")) {
            return new RemainderOperator(token);
        } else if(Utils.checkExists(Constants.COMPARISON_OPERATORS, token)) {
            return new ComparisonOperator(token);
        } else if(Utils.checkExists(Constants.BOOLEAN_OPERATORS, token)) {
            return new BooleanOperator(token);
        } 
        return new RangeOperator(token);
    }
}
