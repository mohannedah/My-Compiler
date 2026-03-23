package compiler.Parser.Statements;

import compiler.DataStructures.SymbolTable;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Expressions.Expression;

public class ElseIfBranch extends Statement {
    public Expression condition;
    public BlockStatement body;

    public ElseIfBranch(Expression condition, BlockStatement body) {
        this.condition = condition;
        this.body = body;
        this.nodeChildren.addLast(condition);
        this.nodeChildren.addLast(body);
    }

    @Override
    public void analyze(SymbolTable scope) throws Exception {
        IdentifierType identifierType = this.condition.getResultantType(scope);
        if(!identifierType.value.equals("BOOL")) 
        {
            throw new SemanticAnalysisException("Expected a Boolean Expression as a conition for the Else-iF Statement");
        }
    }
}