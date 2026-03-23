package compiler.Parser.Statements;

import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Identifier;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.Expressions.IdentifierExpression;

public class VariableAssignmentStatement extends Statement {
    public VariableDeclarationStatement declaration;
    public Expression identifier;
    public Expression expression;
    
    public VariableAssignmentStatement(VariableDeclarationStatement declaration, Identifier identifier, Expression expression) 
    {
        super();
        this.declaration = declaration;
        this.identifier = new IdentifierExpression(identifier);
        this.expression = expression;
        this.nodeChildren.addLast(declaration);
        this.nodeChildren.addLast(new IdentifierExpression(identifier));
        this.nodeChildren.addLast(expression);
    };

    public VariableAssignmentStatement(VariableDeclarationStatement declaration, Expression identifier, Expression expression) 
    {
        this.declaration = declaration;
        this.identifier = identifier;
        this.expression = expression;
    };

    public String getName() 
    {
        return this.nodeType + "(=" + ")";
    }

    @Override
    public void analyze(SymbolTable scope) throws Exception 
    {
        if (this.declaration != null) {
            this.declaration.analyze(scope);
        } 
        
        IdentifierType leftType = this.identifier.getResultantType(scope);
        IdentifierType rightType = this.expression.getResultantType(scope);
        
        if(this.identifier instanceof IdentifierExpression) {
            boolean isConstant = scope.isConstant((IdentifierExpression)this.identifier);
            if(isConstant && this.declaration == null) {
                 throw new SemanticAnalysisException(String.format("Constant variables can not be re-assigned"));
            }
        };
        if(!leftType.equals(rightType)) {
            throw new SemanticAnalysisException(String.format("In an assignment statement, the type of the variable to be assigned is exprcted to ewual the return type of the assignment expression: Expected %s got %s", leftType.value, rightType.value));
        }
    };
}
