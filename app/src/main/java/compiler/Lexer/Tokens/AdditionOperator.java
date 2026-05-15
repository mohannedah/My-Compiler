package compiler.Lexer.Tokens;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import compiler.Constants;
import compiler.DataStructures.EvaluationContext;
import compiler.Lexer.State;
import compiler.Parser.Exceptions.OperatorError;
import compiler.Parser.Statements.IdentifierType;
import compiler.Utils;

public class AdditionOperator extends Operator {
    public AdditionOperator(String token) 
    {
        super(token);
    }

    public AdditionOperator(String token, State state) 
    {
        super(token, state);
    }

    @Override
    public IdentifierType getResultantTypeAfterOperation(IdentifierType leftOperand, IdentifierType rightOperand) throws Exception
    {
        // Check if the two types are equal from the parent method or not and check if the parent method enforces a type or not.
        IdentifierType superType = super.getResultantTypeAfterOperation(leftOperand, rightOperand);

        // At this point we know that both the types are matching, we just need to know whether an operation is possible on this type or not.
        if (!Utils.checkExists(Constants.TYPES_WITH_ADDITION_OPERATION, leftOperand.value)) {
            throw new OperatorError(String.format("Addition operation is not supported on a type of %s", leftOperand.value));
        }

        if(superType != null) return superType;

        return leftOperand;
    };

    @Override
    public void emitOperation(EvaluationContext context, IdentifierType resultantType) throws Exception
    {
        switch (resultantType.value) {
            case "INT":
                context.getMethodVisitor().visitInsn(Opcodes.IADD);
                break;
            case "FLOAT":
                context.getMethodVisitor().visitInsn(Opcodes.FADD);
                break;
            case "STRING":
                this.emitStringConcat(context);
                break;
            default:
                throw new OperatorError(String.format("Addition operation is not supported on a type of %s", resultantType.value));
        } 
    }

    private void emitStringConcat(EvaluationContext context) 
    {
        /*
            STRING_A + STRING_B is the expected output.

            Java-implementation of the string concatenation. Entering this method, the stack will look something like this ->
            
                [STRING_B, STRING_A] where STRING_B is on top of the stack.
            
            We will do the following sequence of operations ->
                - Swap STRING_A with STRING_B -> [STRING_A, STRING_B]
                - Create a StringBuilder object -> [STRING_BUILDER, STRING_A, STRING_B]
                - Swap the STRING_BUILDER with STRING_A to apply the append operation. -> [STRING_A, STRING_BUILDER, STRING_B]
                - Apply Append -> [STRING_BUILDER, STRING_B]
                - Swap the STRING_BUILDER with STRING_B to apply the append operation. -> [STRING_B, STRING_BUILDER]
                - Apply Append -> [STRING_BUILDER]
                - Apply toString() on the object STRING_BUILDER -> [RES_STRING]
            
            The top of the stack will contain the result of the concatenation.
        */

        MethodVisitor mv = context.getMethodVisitor();
        
        mv.visitInsn(Opcodes.SWAP); 

        mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);

        mv.visitInsn(Opcodes.SWAP);

        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitInsn(Opcodes.SWAP);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
    };
}
