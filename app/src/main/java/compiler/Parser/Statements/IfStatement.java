package compiler.Parser.Statements;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Parser.ASTNode;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.MissingConditionError;

public class IfStatement extends Statement {
    public Expression condition;
    public BlockStatement thenBlock;
    public List<ElseIfBranch> elseIfBranches;
    public ElseBranch finalElseBlock;

    public IfStatement(Expression condition, BlockStatement thenBlock, 
                       List<ElseIfBranch> elseIfBranches, ElseBranch finalElseBlock2) 
    {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseIfBranches = elseIfBranches;
        this.finalElseBlock = finalElseBlock2;
        this.nodeChildren.addFirst(thenBlock);
        this.nodeChildren.addFirst(condition);
        for(ASTNode node : elseIfBranches) {
            this.nodeChildren.addLast(node);
        };
        this.nodeChildren.addLast(finalElseBlock2);
    }

    @Override
    public void analyze(SymbolTable scope) throws Exception {
        IdentifierType identifierType = this.condition.getResultantType(scope);
        if(!identifierType.value.equals("BOOL")) 
        {
            throw new MissingConditionError("Expected a Boolean Expression as a conition for the IF Statement");
        }
        thenBlock.analyze(scope);
        for(Statement statement: elseIfBranches) 
        {
            statement.analyze(scope);
        }

        if(finalElseBlock != null) {
            finalElseBlock.analyze(scope);
        }
    }

    @Override 
    public void emit(EvaluationContext context) throws Exception 
    {
        this.condition.emit(context);
        MethodVisitor mv = context.getMethodVisitor();
        Label endCurrIfStatement = new Label(), endChain = new Label(); 

        mv.visitJumpInsn(Opcodes.IFEQ, endCurrIfStatement); // If the condition is not met, go to the `endStatement`

        this.thenBlock.emit(context);

        mv.visitJumpInsn(Opcodes.GOTO, endChain);

        mv.visitLabel(endCurrIfStatement);
        
        // Try matching against other statements.
        for(ElseIfBranch elseIf : this.elseIfBranches) {
            endCurrIfStatement = new Label();
            elseIf.condition.emit(context);
            mv.visitJumpInsn(Opcodes.IFEQ, endCurrIfStatement);
            elseIf.emit(context);
            mv.visitJumpInsn(Opcodes.GOTO, endChain);
            mv.visitLabel(endCurrIfStatement);
        }

        if(this.finalElseBlock != null) {
            this.finalElseBlock.emit(context);
        }

        mv.visitLabel(endChain);
    };
}