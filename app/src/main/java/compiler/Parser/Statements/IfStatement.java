package compiler.Parser.Statements;

import java.util.List;

import compiler.DataStructures.SymbolTable;
import compiler.Parser.ASTNode;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Expressions.Expression;

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
            throw new SemanticAnalysisException("Expected a Boolean Expression as a conition for the IF Statement");
        }
        thenBlock.analyze(scope);
        for(Statement statement: elseIfBranches) 
        {
            statement.analyze(scope);
        }
        finalElseBlock.analyze(scope);
    }
}