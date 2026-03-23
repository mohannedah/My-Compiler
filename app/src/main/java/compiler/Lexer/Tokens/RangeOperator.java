package compiler.Lexer.Tokens;

import compiler.Lexer.State;

public class RangeOperator extends Operator {
    public RangeOperator(String token) {
        super(token);
    }

    public RangeOperator(String token, State state) 
    {
        super(token, state);
    };
}
