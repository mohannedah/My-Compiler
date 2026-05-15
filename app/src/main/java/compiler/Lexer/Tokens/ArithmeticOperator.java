package compiler.Lexer.Tokens;

import compiler.Lexer.State;

public class ArithmeticOperator extends Operator {
    public ArithmeticOperator(String token) {
        super(token);
    }

    public ArithmeticOperator(String token, State state) 
    {
        super(token, state);
    };
}
