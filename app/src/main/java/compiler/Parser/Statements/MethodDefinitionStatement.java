package compiler.Parser.Statements;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Identifier;
import compiler.Parser.Expressions.IdentifierExpression;

public class MethodDefinitionStatement extends Statement {
    public Identifier identifier;
    public List<VariableDeclarationStatement> params;
    public IdentifierType returnType;
    public BlockStatement body;
    public MethodDefinitionStatement(Identifier identifier, IdentifierType returnType, List<VariableDeclarationStatement> params, BlockStatement body) {
        this.returnType = returnType;
        this.returnType.nodeType = "ReturnType";
        this.identifier = identifier;
        this.params = params;
        this.body = body;
        this.nodeChildren.addFirst(returnType);
        for(VariableDeclarationStatement statement : params) 
        {
            this.nodeChildren.addFirst(statement);
        }
        this.nodeChildren.addFirst(new IdentifierExpression(identifier));
        this.nodeChildren.add(body);
    }

    @Override
    public void analyze(SymbolTable scope) throws Exception {
        SymbolTable newScope = new SymbolTable(scope);
        newScope.lastMethodDefinition = this;
        List<IdentifierType> methodParamTypes = new ArrayList<IdentifierType>();
        for(VariableDeclarationStatement param : params) 
        {
            methodParamTypes.addLast(param.identifierType);
            param.analyze(newScope);
        }
        scope.insertMethod(new IdentifierExpression(identifier), methodParamTypes);
        scope.insert(new IdentifierExpression(identifier), returnType, false);
        body.analyze(newScope);
        // Reset as we are leaving the scope.
        newScope.lastMethodDefinition = null;
    }

    @Override
    public void emit(EvaluationContext context) throws Exception {
        StringBuilder descriptor = new StringBuilder("(");
        for (VariableDeclarationStatement param : params) {
            descriptor.append(this.determineByteCodePrefix(param.identifierType)); 
        }
        descriptor.append(")");
        descriptor.append(this.determineByteCodePrefix(this.returnType));

        MethodVisitor mv = context.getClassWriter().visitMethod(
            Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, 
            this.identifier.token, 
            descriptor.toString(), 
            null, 
            null
        );

        context.setMethodVisitor(mv, identifier.token);
        
        mv.visitCode();

        for (VariableDeclarationStatement param : params) {
            param.emit(context);
        }

        this.body.emit(context);

        if (this.returnType.value.equals("void")) {
            mv.visitInsn(Opcodes.RETURN);
        }

        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }
}
