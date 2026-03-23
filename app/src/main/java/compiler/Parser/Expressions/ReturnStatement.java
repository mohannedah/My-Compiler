package compiler.Parser.Expressions;

import compiler.DataStructures.SymbolTable;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Statements.IdentifierType;
import compiler.Parser.Statements.MethodDefinitionStatement;
import compiler.Parser.Statements.Statement;

public class ReturnStatement extends Statement {
    public Expression expression;
    public MethodDefinitionStatement methodDefinitionStatement;
    public ReturnStatement(Expression expression, MethodDefinitionStatement methodDefinitionStatement) 
    {
        super();
        this.methodDefinitionStatement = methodDefinitionStatement;
        this.expression = expression;
        this.nodeChildren.addLast(expression);
    }

    @Override
    public void analyze(SymbolTable scope) throws Exception 
    {
        if (this.methodDefinitionStatement == null) {
            throw new SemanticAnalysisException("'return' statement is strictly prohibited outside of a method body.");
        }
        IdentifierType expressionType = new IdentifierType("void", null);
        if (this.expression != null) {
            expressionType = this.expression.getResultantType(scope);
        }
        if(!expressionType.equals(methodDefinitionStatement.returnType)) {
            throw new SemanticAnalysisException(String.format("Expected the Expression type to match the Method return type: Expected %s got %s", methodDefinitionStatement.returnType.value, expressionType.value));
        }
    }
};

