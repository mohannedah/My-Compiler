package compiler.Parser.Statements;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Identifier;
import compiler.Parser.Exceptions.TypeError;
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
        
        IdentifierType leftType = this.leftOperand.getResultantType(scope);
        IdentifierType rightType = this.expression.getResultantType(scope);
        
        if(!leftType.equals(rightType)) {
            throw new TypeError(String.format("In an assignment statement, the type of the variable to be assigned is expected to equal the return type of the assignment expression: Expected %s got %s", leftType.value, rightType.value));
        }
    };

    @Override
    public void emit(EvaluationContext context) throws Exception 
    {
        Identifier constantName = this.declaration.identifier;
        ClassWriter cw = context.getClassWriter();
        int modifiers = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL; // Add the constant as a static field in the class.
        cw.visitField(modifiers, constantName.token, this.determineByteCodePrefix(this.declaration.identifierType), null, null);
        if(this.expression != null) {
            this.expression.emit(context);
            String className = "test";
            context.getMethodVisitor().visitFieldInsn(Opcodes.PUTSTATIC, className, constantName.token, this.determineByteCodePrefix(this.declaration.identifierType));
        }
    };
}
