package compiler.Lexer.Tokens;

import org.objectweb.asm.Opcodes;

import compiler.Constants;
import compiler.DataStructures.EvaluationContext;
import compiler.Lexer.State;
import compiler.Parser.Exceptions.OperatorError;
import compiler.Parser.Statements.IdentifierType;
import compiler.Utils;

public class MultiplyOperator extends Operator {
    public MultiplyOperator(String token) 
    {
        super(token);
    }

    public MultiplyOperator(String token, State state) 
    {
        super(token, state);
    }

    @Override
    public IdentifierType getResultantTypeAfterOperation(IdentifierType leftOperand, IdentifierType rightOperand) throws Exception
    {
        // Check if the two types are equal from the parent method or not.
        IdentifierType superType = super.getResultantTypeAfterOperation(leftOperand, rightOperand);

        // At this point we know that both the types are matching, we just need to know whether an operation is possible on this type or not.
        if (!Utils.checkExists(Constants.TYPES_WITH_SUBTRACTION_OPERATION, leftOperand.value)) {
            throw new OperatorError(String.format("Multiplication operation is not supported on a type of %s", leftOperand.value));
        }

        if(superType != null) return superType;

        return leftOperand;
    };

    @Override
    public void emitOperation(EvaluationContext context, IdentifierType resultantType) throws Exception
    {
        // At this point we know we are dealing with a candidate resultant type for the operation, now we need to know what kind of operation we will use.
        switch (resultantType.value) {
            case "INT":
                context.getMethodVisitor().visitInsn(Opcodes.IMUL); // Pops the first two items from the stack and apply the operation of multiply on both of them.
                break;
            case "FLOAT":
                context.getMethodVisitor().visitInsn(Opcodes.FMUL);
                break;
            default:
                throw new OperatorError(String.format("Multiplication operation is not supported on a type of %s", resultantType.value));
        } 
    }
}
