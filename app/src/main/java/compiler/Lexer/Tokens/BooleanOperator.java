package compiler.Lexer.Tokens;

import org.objectweb.asm.Opcodes;

import compiler.Constants;
import compiler.DataStructures.EvaluationContext;
import compiler.Lexer.State;
import compiler.Parser.Exceptions.OperatorError;
import compiler.Parser.Statements.IdentifierType;
import compiler.Utils;

public class BooleanOperator extends Operator {
    public BooleanOperator(String token) 
    {
        super(token);
    }

    public BooleanOperator(String token, State state) 
    {
        super(token, state);
    }

    @Override
    public IdentifierType getResultantTypeAfterOperation(IdentifierType leftOperand, IdentifierType rightOperand) throws Exception
    {
        // Check if the two types are equal from the parent method or not.
        super.getResultantTypeAfterOperation(leftOperand, rightOperand);

        // At this point we know that both the types are matching, we just need to know whether an operation is possible on this type or not.
        if (!Utils.checkExists(Constants.TYPES_WITH_BOOLEAN_OPERATIONS, leftOperand.value)) {
            throw new OperatorError(String.format("Boolean operation is not supported on a type of {}", leftOperand.value));
        }
        return new IdentifierType("BOOL", false);
    };

    @Override
    public void emitOperation(EvaluationContext context, IdentifierType resultantType) throws Exception
    {
        int opCode = token.equals("||") ? Opcodes.IOR : Opcodes.IAND;
        context.getMethodVisitor().visitInsn(opCode); 
    }
}
