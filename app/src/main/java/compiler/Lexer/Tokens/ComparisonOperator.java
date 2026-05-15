package compiler.Lexer.Tokens;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import compiler.DataStructures.EvaluationContext;
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
    public IdentifierType getResultantTypeAfterOperation(IdentifierType leftOperand, IdentifierType rightOperand) throws Exception
    {
        // Check if the two types are equal from the parent method or not.
        super.getResultantTypeAfterOperation(leftOperand, rightOperand);
        
        // All the types qualifies to be compared with each other. The default will be comparing the memory addresses for complex types.
        return new IdentifierType("BOOL", false);
    };

    @Override
    public void emitOperation(EvaluationContext context, IdentifierType operandType) throws Exception
    {
        MethodVisitor mv = context.getMethodVisitor();
        if(operandType.value.equals("FLOAT")) {
            emitFloatValue(context, operandType);
            return;
        }
        
        Label trueLabel = new Label();
        Label endLabel = new Label();

        int jumpOpcode = this.getJumpOpcode(this.token, operandType);

        mv.visitJumpInsn(jumpOpcode, trueLabel);

        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitJumpInsn(Opcodes.GOTO, endLabel);

        mv.visitLabel(trueLabel);
        mv.visitInsn(Opcodes.ICONST_1);

        mv.visitLabel(endLabel);
    }

    public void emitFloatValue(EvaluationContext context, IdentifierType operandType) throws Exception 
    {   
       MethodVisitor mv = context.getMethodVisitor();
        

        mv.visitInsn(Opcodes.FCMPG); 

        Label trueLabel = new Label();
        Label endLabel = new Label();

        int jumpOpcode = this.getJumpOpcode(this.token, operandType);

        mv.visitJumpInsn(jumpOpcode, trueLabel);

        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitJumpInsn(Opcodes.GOTO, endLabel); 

        mv.visitLabel(trueLabel);
        mv.visitInsn(Opcodes.ICONST_1);

        mv.visitLabel(endLabel);
    };

    private int getJumpOpcode(String operator, IdentifierType operandType) throws Exception 
    {
        if(operandType.value.equals("FLOAT")) {
            switch (operator) {
                case "==" -> {
                    return Opcodes.IFEQ;
                }
                case "!=", "=/=" -> {
                    return Opcodes.IFNE;
                }
                case "<" -> {
                    return Opcodes.IFLT;
                }
                case "<=" -> {
                    return Opcodes.IFLE;
                }
                case ">" -> {
                    return Opcodes.IFGT;
                }
                case ">=" -> {
                    return Opcodes.IFGE;
                }
                default -> throw new RuntimeException("Unknown float comparison operator: " + operator);
            }       
         }

        boolean isObject = operandType.isArray || operandType.value.equals("STRING");

        if (isObject) {
            // Can only compare the memory addresses for equality operations.
            switch (operator) {
                case "==" -> {
                    return Opcodes.IF_ACMPEQ;
                }
                case "=/=" -> {
                    return Opcodes.IF_ACMPNE;
                }
                default -> throw new RuntimeException("Cannot use '" + operator + "' on object types. Only '==' and '!=' are supported.");
            }
        } 
        
        switch (operator) {
            case "==" -> {
                return Opcodes.IF_ICMPEQ;
            }
            case "=/=" -> {
                return Opcodes.IF_ICMPNE;
            }
            case "<" -> {
                return Opcodes.IF_ICMPLT;
            }
            case "<=" -> {
                return Opcodes.IF_ICMPLE;
            }
            case ">" -> {
                return Opcodes.IF_ICMPGT;
            }
            case ">=" -> {
                return Opcodes.IF_ICMPGE;
            }
            default -> throw new RuntimeException("Unknown comparison operator: " + operator);
        }
    }
}
