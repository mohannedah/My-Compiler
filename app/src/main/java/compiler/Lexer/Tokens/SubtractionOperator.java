package compiler.Lexer.Tokens;

import org.objectweb.asm.Opcodes;

import compiler.Constants;
import compiler.DataStructures.EvaluationContext;
import compiler.Lexer.State;
import compiler.Parser.Exceptions.OperatorError;
import compiler.Parser.Statements.IdentifierType;
import compiler.Utils;

public class SubtractionOperator extends Operator {
    public SubtractionOperator(String token) 
    {
        super(token);
    }

    public SubtractionOperator(String token, State state) 
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
            throw new OperatorError(String.format("Subtraction operation is not supported on a type of %s", leftOperand.value));
        }

        if(superType != null) return superType;
        return leftOperand;
    };

    @Override
    public void emitOperation(EvaluationContext context, IdentifierType resultantType) throws Exception
    {
        switch (resultantType.value) {
            case "INT":
                context.getMethodVisitor().visitInsn(Opcodes.ISUB);                break;
            case "FLOAT":
                context.getMethodVisitor().visitInsn(Opcodes.FSUB);
                break;
            default:
                throw new OperatorError(String.format("Subtraction operation is not supported on a type of %s", resultantType.value));
        } 
    }
}
