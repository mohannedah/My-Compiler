package compiler.Lexer.Tokens;

import compiler.Lexer.State;
import compiler.Parser.Statements.IdentifierType;

public class ComparisonOperator extends Operator {
    public ComparisonOperator(String token) 
    {
        super(token);
    }

    public ComparisonOperator(String token, State state) 
    {
        super(token, state);
    }

    @Override
    public IdentifierType getResultantTypeAfterOperation(IdentifierType leftOperand, IdentifierType rightOperand) 
    {
        // Check if the two types are equal from the parent method or not.
        super.getResultantTypeAfterOperation(leftOperand, rightOperand);
        
        // All the types qualifies to be compared with each other. The default will be comparing the memory addresses for complex types.
        return new IdentifierType("BOOL", false);
    };
}
