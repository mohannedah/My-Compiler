package compiler.Lexer.Tokens;

import compiler.Lexer.State;
import compiler.Parser.Statements.IdentifierType;

public class RangeOperator extends Operator {
    public RangeOperator(String token) {
        super(token);
    }

    public RangeOperator(String token, State state) 
    {
        super(token, state);
    };

    public IdentifierType getResultantTypeAfterOperation(IdentifierType leftOperandType, IdentifierType rightOperandType) {
        return new IdentifierType("void", false);
    };
}
