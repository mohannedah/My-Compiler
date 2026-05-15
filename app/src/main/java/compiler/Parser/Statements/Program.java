package compiler.Parser.Statements;

import java.util.List;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import compiler.Constants;
import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Parser.ASTNode;
import compiler.Parser.Exceptions.SemanticAnalysisException;

public class Program extends Statement {
    public List<Statement> statements;

    public Program(List<Statement> statements) {
        super();
        this.statements = statements;
        for(ASTNode statement : statements) 
        {
            this.nodeChildren.addLast(statement);
        }
    }

    @Override
    public void analyze(SymbolTable scope) throws Exception {
        boolean foundMain = false, normalStatement = false; 
        for(Statement statement : this.statements) {
            boolean isGlobal = false;
            for (Class<?> allowedClass : Constants.ALLOWED_GLOBAL_STATEMENTS) {
                if (allowedClass.isInstance(statement)) {
                    isGlobal = true; 
                    break;
                }
            }

            if(!isGlobal) {
                throw new SemanticAnalysisException(
                    "Executable statements (like 'if', 'while', or reassignments) " +
                    "cannot exist in the global scope. Please move them inside a method."
                );
            }
            
            if(statement instanceof CommentStatement) continue;
           
            if(statement instanceof ConstantAssignment) {
                if(normalStatement) {
                    throw new SemanticAnalysisException(
                    "Constants must be defined at the very start of the file"
                );
                }
                statement.analyze(scope);
                continue;
            }
            
            if (statement instanceof MethodDefinitionStatement methodDef) {
                if (methodDef.identifier.token.equals("main")) {
                    foundMain = true;
                }
            }

            statement.analyze(scope);
            normalStatement = true;
        }

        if (!foundMain) {
            throw new SemanticAnalysisException("Program must contain a 'main' method as the entry point.");
        }
    }

    @Override
    public void emit(EvaluationContext context) throws Exception 
    {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "test", null, "java/lang/Object", null);

        context.setClassWriter(cw);
        MethodVisitor clinitMv = cw.visitMethod(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
        clinitMv.visitCode();
        context.setMethodVisitor(clinitMv, "<clinit>");
        for(Statement statement : this.statements) {
            if(statement instanceof VariableDeclarationStatement varDecl) {
                String descriptor = this.determineByteCodePrefix(varDecl.identifierType);
                cw.visitField(
                    Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, 
                    varDecl.identifier.token, 
                    descriptor, 
                    null, 
                    null
                ).visitEnd();
            } else if(statement instanceof MethodDefinitionStatement && ((MethodDefinitionStatement)statement).identifier.token.equals("main")) {
                // Handle the main method differently.
                MethodVisitor mainMv = cw.visitMethod(
                    Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, 
                    "main", 
                    "([Ljava/lang/String;)V", // Descriptor for String[] args
                    null, 
                    null
                );
                context.setMethodVisitor(mainMv, "main");
                MethodDefinitionStatement methodStatement = (MethodDefinitionStatement)statement;
                context.allocateLocalVariable("args");
                methodStatement.body.emit(context);
                mainMv.visitInsn(Opcodes.RETURN);
                mainMv.visitMaxs(0, 0);
                mainMv.visitEnd();
            } else if(statement instanceof ConstantAssignment) {
                statement.emit(context);
            } else if(statement instanceof VariableAssignmentStatement) {
                statement.emit(context);
            } else {
                statement.emit(context);
            }
        }
        clinitMv.visitInsn(Opcodes.RETURN);
        clinitMv.visitMaxs(0, 0);
        clinitMv.visitEnd();
        cw.visitEnd();
        context.registerClass("test", cw.toByteArray());
    }
}
