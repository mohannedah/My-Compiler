package compiler.Lexer.Tokens;

import compiler.DataStructures.EvaluationContext;
import compiler.Lexer.State;
import compiler.Parser.Exceptions.OperatorError;
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

    public IdentifierType getResultantTypeAfterOperation(IdentifierType leftOperand, IdentifierType rightOperand) throws Exception
    {
        if(!leftOperand.equals(rightOperand)) {
            throw new OperatorError("Types should match to be able to apply the operation"); 
        }

        if(leftOperand.value.equals("FLOAT") || rightOperand.value.equals("FLOAT")) return new IdentifierType("FLOAT", false);

        return null;
    }

    public void emitOperation(EvaluationContext context, IdentifierType resultantType) throws Exception
    {
        throw new UnsupportedOperationException(String.format("Consider calling the operation on the child-classes"));
    }

    public void emitOperation(EvaluationContext context) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'emitOperation'");
    }
}
