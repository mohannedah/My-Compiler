package compiler.Parser.Expressions;

import java.util.List;

import org.objectweb.asm.Opcodes;

import compiler.Constants;
import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Parser.Exceptions.ArgumentError;
import compiler.Parser.Exceptions.ScopeError;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Helpers.BuiltInEmitter;
import compiler.Parser.Statements.IdentifierType;
import compiler.Utils;

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
    public void analyze(SymbolTable table) throws Exception 
    {
        this.getResultantType(table);
    }

    @Override
    public IdentifierType getResultantType(SymbolTable table) throws Exception
    {
        try {
            return super.getResultantType(table);
        } catch (UnsupportedOperationException e) {
            if(!(this.expression instanceof IdentifierExpression)) {
            throw new SemanticAnalysisException("Expected an identifier for a method invocation");
            }
            IdentifierExpression identifierExpression = (IdentifierExpression)this.expression;
            
            IdentifierType methodReturnType = table.findKey(identifierExpression);
            List<IdentifierType> methodArguments = table.getMethodArguments(identifierExpression);

            if(methodReturnType == null || methodArguments == null) {
                throw new ScopeError(String.format("Method '%s' is undefined in the current scope.", identifierExpression.value));
            }

            if(methodArguments.size() != this.parameters.size()) {
                throw new ArgumentError(String.format("The number of parameters are not matching for the method '%s'. Expected %d, got %d", 
                    identifierExpression.value, methodArguments.size(), this.parameters.size()));
            }

            for(int i = 0; i < parameters.size(); i++) 
            {
                IdentifierType paramType = parameters.get(i).getResultantType(table);
                IdentifierType targetType = methodArguments.get(i);
                
                if(!(paramType.equals(targetType))) {
                    throw new ArgumentError(String.format("Params type for the method '%s' are not matching: %s != %s", 
                        identifierExpression.value, paramType.value, targetType.value));
                }
            }
            this.cachedIdentifierType = methodReturnType;   
            return methodReturnType;
        }
    }

    public String getJvmDescriptor(boolean isConstructor) throws Exception
    {
        StringBuilder descriptor = new StringBuilder();
        
        descriptor.append("(");
        
        for (Expression param : this.parameters) {
            IdentifierType paramType = param.getResultantType(null); // Cached.
            descriptor.append(this.determineByteCodePrefix(paramType));
        }
        
        descriptor.append(")");
        
        if(!isConstructor) 
        {
            descriptor.append(determineByteCodePrefix(this.getResultantType(null))); // Cached as well.
        } else {
            descriptor.append("V");
        }
        
        return descriptor.toString();
    }

    @Override
    public void emit(EvaluationContext context) throws Exception 
    {
        IdentifierExpression methodIdentifier = (IdentifierExpression)this.expression;

        if(Utils.checkExists(Constants.BUILT_IN_FUNCTIONS, methodIdentifier.value)) {
            emitBuiltInOperation(methodIdentifier.value, context);
            return;
        }

        IdentifierType resultantType = this.getResultantType(null);
        boolean isConstructor = methodIdentifier.value.equals(resultantType.value);

        if(isConstructor) // If the resultant type is equal to the object Type, then we know it is a constructor call, and we need to emit the code for object creation before the method invocation.
        {
            context.getMethodVisitor().visitTypeInsn(Opcodes.NEW, methodIdentifier.value);
            context.getMethodVisitor().visitInsn(Opcodes.DUP);
        }  
        
        String methodDescriptor = this.getJvmDescriptor(isConstructor);
        int methodOpCode = isConstructor ? Opcodes.INVOKESPECIAL : Opcodes.INVOKESTATIC;
        String targetMethodName = isConstructor ? "<init>" : methodIdentifier.value;
        String ownerClass = isConstructor ? resultantType.value : "test";

        if (this.parameters != null) {
            for (Expression param : this.parameters) {
                param.emit(context); 
            }
        }
        context.getMethodVisitor().visitMethodInsn(methodOpCode, ownerClass, targetMethodName, methodDescriptor, false);
    }

    public void emitBuiltInOperation(String methodIdentifier, EvaluationContext context) throws Exception 
    {
        BuiltInEmitter methodEmitter = Constants.METHOD_EMITTER_BY_METHOD_NAME.get(methodIdentifier);
        methodEmitter.emit(context, this.parameters);  
    };
}