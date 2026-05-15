package compiler.Parser.Statements;

import org.objectweb.asm.Opcodes;

import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Identifier;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Exceptions.TypeError;
import compiler.Parser.Expressions.ArrayAccessorExpression;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.Expressions.IdentifierExpression;
import compiler.Parser.Expressions.ObjectAccessorExpression;

public class VariableAssignmentStatement extends Statement {
    public VariableDeclarationStatement declaration;
    public Expression leftOperand;
    public Expression expression;
    
    public VariableAssignmentStatement(VariableDeclarationStatement declaration, Identifier identifier, Expression expression) 
    {
        super();
        this.declaration = declaration;
        this.leftOperand = new IdentifierExpression(identifier);
        this.expression = expression;
        this.nodeChildren.addLast(declaration);
        this.nodeChildren.addLast(leftOperand);
        this.nodeChildren.addLast(expression);
    };

    public VariableAssignmentStatement(VariableDeclarationStatement declaration, Expression leftOperand, Expression expression) 
    {
        this.declaration = declaration;
        this.leftOperand = leftOperand;
        this.expression = expression;
        this.nodeChildren.addLast(declaration);
        this.nodeChildren.addLast(leftOperand);
        this.nodeChildren.addLast(expression);
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
        
        IdentifierType leftType = this.leftOperand.getResultantType(scope);
        IdentifierType rightType = this.expression.getResultantType(scope);

        if(!(this.leftOperand instanceof IdentifierExpression || this.leftOperand instanceof ObjectAccessorExpression || this.leftOperand instanceof ArrayAccessorExpression)) {
            throw new SemanticAnalysisException("Variable to be assigned must be either a defined variable or a field inside an Object or a position in an Array.");
        }

        if(this.leftOperand instanceof IdentifierExpression identifierExpression) {
            boolean isConstant = scope.isConstant(identifierExpression);
            if(isConstant && this.declaration == null) {
                 throw new SemanticAnalysisException(String.format("Constant variables can not be re-assigned"));
            }
        } 

        if(!leftType.equals(rightType)) {
            throw new TypeError(String.format("In an assignment statement, the type of the variable to be assigned is expected to equal the return type of the assignment expression: Expected %s got %s", leftType.value, rightType.value));
        }
    };

    @Override
    public void emit(EvaluationContext context) throws Exception 
    {
        if(leftOperand instanceof ObjectAccessorExpression) {
            ObjectAccessorExpression currLeftOperand = (ObjectAccessorExpression)leftOperand;
            currLeftOperand.leftOperand.emit(context);
        
            this.expression.emit(context);
            this.expression.tryEmitCasting(context, currLeftOperand.getResultantType(null));

            IdentifierExpression fieldIdentifier = (IdentifierExpression)currLeftOperand.rightOperand;
            String fieldDescriptor = this.determineByteCodePrefix(this.expression.getResultantType(null));
            String structName = currLeftOperand.leftOperand.getResultantType(null).value;
            context.getMethodVisitor().visitFieldInsn(Opcodes.PUTFIELD, structName, fieldIdentifier.value, fieldDescriptor);   
        } else if (leftOperand instanceof ArrayAccessorExpression currLeftOperand) {
            currLeftOperand.leftOperand.emit(context);
            
            currLeftOperand.rightOperand.emit(context);
            
            this.expression.emit(context);
            
            IdentifierType targetType = currLeftOperand.getResultantType(null);
            this.expression.tryEmitCasting(context, targetType);

            int arrayStoreOpcode = getArrayStoreOpcode(targetType.value);
            context.getMethodVisitor().visitInsn(arrayStoreOpcode);
        } else {
            if(this.declaration != null) {
                this.declaration.emit(context); // Emit the declaration to either populate the local variable and assign an index to it or create a static field of the identifier;
            }
            IdentifierExpression currLeftOperand = (IdentifierExpression) leftOperand;
            this.expression.emit(context);
            IdentifierType targetType = currLeftOperand.getResultantType(null);
            this.expression.tryEmitCasting(context, targetType);
            try {
                int localVariableIndex = context.getLocalVariableIndex(currLeftOperand.value);
                int localStoreOpcode = getLocalStoreOpcode(targetType.value, targetType.isArray);
                context.getMethodVisitor().visitVarInsn(localStoreOpcode, localVariableIndex);    
            } catch (RuntimeException e) {
                // In this case, the `leftOperand` should be a static field.
                context.getMethodVisitor().visitFieldInsn(Opcodes.PUTSTATIC, "test", currLeftOperand.value, this.determineByteCodePrefix(targetType));
            }
        }
    };

    private int getArrayStoreOpcode(String typeValue) throws Exception {
        switch (typeValue.toUpperCase()) {
            case "INT": return Opcodes.IASTORE;
            case "FLOAT": return Opcodes.FASTORE;
            case "BOOL": return Opcodes.BASTORE;
            case "STRING": return Opcodes.AASTORE; 
            default: return Opcodes.AASTORE;
        }
    }

    private int getLocalStoreOpcode(String typeValue, boolean isArray) throws Exception {
        if (isArray) {
            return Opcodes.ASTORE; 
        }

        switch (typeValue.toUpperCase()) 
        {
            case "INT": 
            case "BOOL": return Opcodes.ISTORE; 
            case "FLOAT": return Opcodes.FSTORE;
            case "STRING": return Opcodes.ASTORE; 
            default: return Opcodes.ASTORE;
        }
    }
}
