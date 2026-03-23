package compiler.Parser.Statements;

import compiler.DataStructures.SymbolTable;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Expressions.Expression;

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
            throw new SemanticAnalysisException(
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
}
