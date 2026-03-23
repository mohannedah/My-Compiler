package compiler.Parser.Expressions;

import java.util.List;

import compiler.DataStructures.SymbolTable;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Statements.IdentifierType;

public class MethodInvocationExpression extends Expression {
    public Expression expression;
    public List<Expression> parameters;
    
    public MethodInvocationExpression(Expression expression, List<Expression> parameters) 
    {
        super();
        this.expression = expression;
        this.parameters = parameters;
        this.nodeChildren.addLast(this.expression);
        for(Expression param : parameters) 
        {
            this.nodeChildren.addLast(param);
        }
    }

    @Override
    public IdentifierType getResultantType(SymbolTable table) throws Exception
    {
        if(!(this.expression instanceof IdentifierExpression)) {
            throw new SemanticAnalysisException("Expected an identifier for a method invocation");
        }
        IdentifierExpression identifierExpression = (IdentifierExpression)this.expression;
        
        IdentifierType methodReturnType = table.findKey(identifierExpression);
        List<IdentifierType> methodArguments = table.getMethodArguments(identifierExpression);

        if(methodReturnType == null || methodArguments == null) {
            throw new SemanticAnalysisException(String.format("Method '%s' is undefined in the current scope.", identifierExpression.value));
        }

        if(methodArguments.size() != this.parameters.size()) {
            throw new SemanticAnalysisException(String.format("The number of parameters are not matching for the method '%s'. Expected %d, got %d", 
                identifierExpression.value, methodArguments.size(), this.parameters.size()));
        }

        for(int i = 0; i < parameters.size(); i++) 
        {
            IdentifierType paramType = parameters.get(i).getResultantType(table);
            IdentifierType targetType = methodArguments.get(i);
            
            if(!(paramType.equals(targetType))) {
                throw new SemanticAnalysisException(String.format("Params type for the method '%s' are not matching: %s != %s", 
                    identifierExpression.value, paramType.value, targetType.value));
            }
        }
        
        return methodReturnType;
    }
}