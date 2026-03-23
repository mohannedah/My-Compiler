package compiler.Lexer.Tokens;

import compiler.Lexer.State;
import compiler.Parser.Statements.IdentifierType;

public class Operator extends Token {
    public Operator(String token) 
    {
        super(token);
    };

    public Operator(String token, State state) 
    {
        super(token, state);
    }

    public IdentifierType getResultantTypeAfterOperation(IdentifierType leftOperand, IdentifierType rightOperand) 
    {
        if(!leftOperand.equals(rightOperand)) {
            throw new UnsupportedOperationException("Types should match to be able to apply the operation"); 
        }
        return null;
    }
}
