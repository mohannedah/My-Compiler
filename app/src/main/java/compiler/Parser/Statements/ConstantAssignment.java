package compiler.Parser.Statements;

import compiler.DataStructures.SymbolTable;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.Expressions.IdentifierExpression;

public class ConstantAssignment extends VariableAssignmentStatement {
    public ConstantAssignment(VariableDeclarationStatement declaration, IdentifierExpression identifier, Expression expression) {
        super(declaration, identifier, expression);
    }

    @Override
    public void analyze(SymbolTable scope) throws Exception 
    {
        scope.insert(new IdentifierExpression(declaration.identifier), declaration.identifierType, true);
        
        IdentifierType leftType = this.identifier.getResultantType(scope);
        IdentifierType rightType = this.expression.getResultantType(scope);
        
        if(!leftType.equals(rightType)) {
            throw new SemanticAnalysisException(String.format("In an assignment statement, the type of the variable to be assigned is exprcted to ewual the return type of the assignment expression: Expected %s got %s", leftType.value, rightType.value));
        }
    };
}
