package compiler.Parser.Expressions;

import java.util.Map;

import compiler.Constants;
import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Parser.Statements.IdentifierType;
import compiler.Parser.Statements.Statement;

public class Expression extends Statement {
    public IdentifierType cachedIdentifierType;
    public Expression() 
    {
        super();
        this.cachedIdentifierType = null;
    };

    public IdentifierType getResultantType(SymbolTable table) throws Exception
    {
        if(this.cachedIdentifierType != null) 
        {
            return this.cachedIdentifierType;
        }
        throw new UnsupportedOperationException("To be implemented in the child classes as an override.");
    }

    public IdentifierType determineCastingType(IdentifierType leftOperand, IdentifierType rightOperand) {
        if(leftOperand.value.equals("FLOAT") || rightOperand.value.equals("FLOAT")) return new IdentifierType("FLOAT", false);
        return leftOperand;
    };

    public void tryEmitCasting(EvaluationContext context, IdentifierType targetType) throws Exception 
    {
        if (this.getResultantType(null).value.equals(targetType.value)) {
            return; 
        }

        try {
            Map<String, Integer> availableCasts = Constants.CASTING_INSTRUCTIONS.get(this.getResultantType(null).value);

            Integer castInstruction = availableCasts.get(targetType.value);

            if (castInstruction == null) {
                throw new IllegalArgumentException(
                    String.format("No direct cast exists from %s to %s.", this.getResultantType(null).value, targetType.value)
                );
            }

            context.getMethodVisitor().visitInsn(castInstruction);
        } catch (NullPointerException e) {
            throw new RuntimeException(
                "Code Generation Error: The type '" + this.getResultantType(null).value + "' does not support any outbound casting.", e
            );
            
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Code Generation Error: " + e.getMessage(), e);
        }
    }

    @Override
    public void analyze(SymbolTable scope) throws Exception 
    {
        this.getResultantType(scope);
    }
}
