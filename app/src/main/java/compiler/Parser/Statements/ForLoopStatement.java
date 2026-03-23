package compiler.Parser.Statements;

import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Identifier;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.Expressions.IdentifierExpression;
import compiler.Parser.Expressions.RangeExpression;

public class ForLoopStatement extends Statement {
    public Identifier loopIdentifier;
    public RangeExpression rangeExpression;
    public Expression updateExpression;
    public BlockStatement body;
    public ForLoopStatement(Identifier identifier, RangeExpression rangeExpression, Expression updateExpression,
            BlockStatement body) {
        this.loopIdentifier = identifier;
        this.rangeExpression = rangeExpression;
        this.updateExpression = updateExpression;
        this.body = body;
        this.nodeChildren.addFirst(updateExpression);
        this.nodeChildren.addFirst(rangeExpression);
        this.nodeChildren.addFirst(new IdentifierExpression(loopIdentifier));
        this.nodeChildren.addLast(body);
    }
    
    @Override
    public void analyze(SymbolTable scope) throws Exception {
        SymbolTable loopHeaderScope = new SymbolTable(scope);

        IdentifierExpression loopVarExpr = new IdentifierExpression(this.loopIdentifier);
        IdentifierType loopVarType = new IdentifierType("INT", false); 
        boolean inserted = loopHeaderScope.insert(loopVarExpr, loopVarType, false);
        if (!inserted) {
            throw new SemanticAnalysisException("Failed to initialize loop variable: " + loopIdentifier.token);
        }

        if (this.rangeExpression != null) {
            this.rangeExpression.getResultantType(loopHeaderScope);
        }

        if (this.updateExpression != null) {
            IdentifierType type = this.updateExpression.getResultantType(loopHeaderScope);
            if(type.value != "INT") {
                throw new SemanticAnalysisException("The update expression must be return an Integer.");
            }
        }

        if (this.body != null) {
            this.body.analyze(loopHeaderScope);
        }
    }
}
