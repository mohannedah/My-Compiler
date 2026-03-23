package compiler.Lexer.Tokens;

import compiler.Constants;
import compiler.Lexer.State;
import compiler.Parser.Statements.IdentifierType;
import compiler.Utils;

public class RemainderOperator extends Operator {
    public RemainderOperator(String token) 
    {
        super(token);
    }

    public RemainderOperator(String token, State state) 
    {
        super(token, state);
    }

    @Override
    public IdentifierType getResultantTypeAfterOperation(IdentifierType leftOperand, IdentifierType rightOperand) 
    {
        // Check if the two types are equal from the parent method or not.
        super.getResultantTypeAfterOperation(leftOperand, rightOperand);

        // At this point we know that both the types are matching, we just need to know whether an operation is possible on this type or not.
        if (!Utils.checkExists(Constants.TYPES_WITH_SUBTRACTION_OPERATION, token)) {
            throw new UnsupportedOperationException(String.format("Addition operation is not supported on a type of %s", leftOperand.value));
        }
        return leftOperand;
    };
}
