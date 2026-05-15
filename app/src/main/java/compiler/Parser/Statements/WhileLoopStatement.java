package compiler.Parser.Statements;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.MissingConditionError;

public class WhileLoopStatement extends Statement {
    public Expression expression;
    public BlockStatement body;
    public WhileLoopStatement(Expression expression, BlockStatement body) 
    {
        this.expression = expression;
        this.nodeChildren.addFirst(expression);
        this.body = body;
    }

    @Override
    public void analyze(SymbolTable scope) throws Exception 
    {
        if (this.expression == null) {
            throw new SemanticAnalysisException("While loop condition cannot be empty.");
        }

        IdentifierType expressionType = this.expression.getResultantType(scope);
        
        if (expressionType == null) {
            throw new SemanticAnalysisException("While loop condition evaluates to an unknown type.");
        }

        IdentifierType booleanType = new IdentifierType("BOOL", false);
        
        if (!(expressionType.equals(booleanType))) {
            throw new MissingConditionError(
                String.format(
                    "Semantic Error: A while loop condition must evaluate to a boolean. Expected 'boolean', but got '%s'.", 
                    expressionType.value
                )
            );
        }

        if (this.body != null) {
            SymbolTable loopScope = new SymbolTable(scope);
            this.body.analyze(loopScope);
        }
    }

    @Override
    public void emit(EvaluationContext context) throws Exception 
    {
        MethodVisitor mv = context.getMethodVisitor();

        Label loopStartLabel = new Label();
        Label loopEndLabel = new Label();

        mv.visitLabel(loopStartLabel);

        this.expression.emit(context);
        
        mv.visitJumpInsn(Opcodes.IFEQ, loopEndLabel);

        this.body.emit(context);

        mv.visitJumpInsn(Opcodes.GOTO, loopStartLabel);

        mv.visitLabel(loopEndLabel);
    }
}
