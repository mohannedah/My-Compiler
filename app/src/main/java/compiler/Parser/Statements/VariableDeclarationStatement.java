package compiler.Parser.Statements;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Identifier;
import compiler.Parser.Expressions.BinaryExpression;
import compiler.Parser.Expressions.IdentifierExpression;

public class VariableDeclarationStatement extends Statement {
    public IdentifierType identifierType;
    public Identifier identifier;
    public BinaryExpression expression;
    public VariableDeclarationStatement(IdentifierType identifierType, Identifier identifier) 
    {
        super();
        this.identifierType = identifierType;
        this.identifier = identifier;
        this.nodeChildren.addLast(new IdentifierExpression(identifier));
        this.nodeChildren.addLast(identifierType);
    }

    @Override
    public void analyze(SymbolTable scope) throws Exception {
        scope.insert(new IdentifierExpression(identifier), this.identifierType, false);
    };

    @Override
    public void emit(EvaluationContext context) throws Exception 
    {
        MethodVisitor mv = context.getMethodVisitor();
        if(context.currMethodName.equals("<clinit>")) 
        {
            context.addStaticField(identifier.token);
            this.determineStoreOpCode(context);
            context.getClassWriter().visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, identifier.token, this.determineByteCodePrefix(identifierType), null, null).visitEnd();
        } else {
            String variableName = identifier.token;
            int varIndex = context.allocateLocalVariable(variableName);
            mv.visitVarInsn(determineStoreOpCode(context), varIndex);
        }
    };

    public int determineStoreOpCode(EvaluationContext context) {
        switch(identifierType.value.toUpperCase()) {
            case "INT":
            case "BOOL":
                context.getMethodVisitor().visitInsn(Opcodes.ICONST_0);
                return Opcodes.ISTORE;
            case "FLOAT":
                context.getMethodVisitor().visitInsn(Opcodes.FCONST_0);
                return Opcodes.FSTORE;
            default:
                context.getMethodVisitor().visitInsn(Opcodes.ACONST_NULL);
                return Opcodes.ASTORE;
        }
    };
}
