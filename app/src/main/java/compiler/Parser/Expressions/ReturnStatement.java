package compiler.Parser.Expressions;

import org.objectweb.asm.Opcodes;

import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Parser.Exceptions.ReturnError;
import compiler.Parser.Statements.IdentifierType;
import compiler.Parser.Statements.MethodDefinitionStatement;
import compiler.Parser.Statements.Statement;

public class ReturnStatement extends Statement {
    public Expression expression;
    public ReturnStatement(Expression expression) 
    {
        super();
        this.expression = expression;
        this.nodeChildren.addLast(expression);
    }

    @Override
    public void analyze(SymbolTable scope) throws Exception 
    {
        MethodDefinitionStatement lastMethodDefinition = scope.getLastMethodDefinition();
        if (lastMethodDefinition == null) {
            throw new ReturnError("'return' statement is strictly prohibited outside of a method body.");
        }
        IdentifierType expressionType = new IdentifierType("void", null);
        if (this.expression != null) {
            expressionType = this.expression.getResultantType(scope);
        }
        if(!expressionType.equals(lastMethodDefinition.returnType)) {
            throw new ReturnError(String.format("Expected the Expression type to match the Method return type: Expected %s got %s", lastMethodDefinition.returnType.value, expressionType.value));
        }
    }

    @Override
    public void emit(EvaluationContext context) throws Exception 
    {
        if(this.expression != null) {
            this.expression.emit(context);
        }
        IdentifierType resultantType = expression != null ? this.expression.getResultantType(null) : new IdentifierType("void", null);
        int opCode = this.getReturnOpcode(resultantType);
        context.getMethodVisitor().visitInsn(opCode);
    };

    private int getReturnOpcode(IdentifierType identifierType) 
    {
        String typeStr = identifierType.value.toUpperCase();
        switch(typeStr) {
            case "VOID":
                return Opcodes.RETURN;
            case "INT":
            case "BOOL":
                return Opcodes.IRETURN;
            case "FLOAT":
                return Opcodes.FRETURN;
            default:
                return Opcodes.ARETURN;
        }
    };
};

