package compiler.Parser.Statements;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Identifier;
import compiler.Parser.Exceptions.CollectionError;
import compiler.Parser.Expressions.IdentifierExpression;

public class StructStatement extends Statement {
    public List<StructProperty> structProperties;
    public Identifier identifier;

    public StructStatement(Identifier identifier, List<StructProperty> structProperties) 
    {
        this.identifier = identifier;
        this.structProperties = structProperties;
        for (StructProperty property : structProperties) 
        {
            this.nodeChildren.add(property);
        }
    };

    public String getName() 
    {
        return this.nodeType + "(" + this.identifier + ")";
    }

   @Override
    public void analyze(SymbolTable scope) throws Exception 
    {
        IdentifierExpression structIdExpr = new IdentifierExpression(this.identifier);
        
        // Assuming your IdentifierType constructor takes the string name of the type
        IdentifierType structType = new IdentifierType(this.identifier.token, false);

        boolean inserted = scope.insert(structIdExpr, structType, false);
        if (!inserted) {
            throw new CollectionError(
                "Semantic Error: A struct or variable named '" + this.identifier.token + "' is already declared in this scope."
            );
        }

        if (this.structProperties != null) {
            scope.insertStruct(structType, structProperties);
        }
        
        List<IdentifierType> arguments = new ArrayList<>();

        for(StructProperty property : structProperties) 
        {
            arguments.addLast(property.propertyType);
        }

        // Adding the constructor method.
        scope.insertMethod(structIdExpr, arguments);
    }

    @Override
    public void emit(EvaluationContext context) throws Exception 
    {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, this.identifier.token, null, "java/lang/Object", null);

        StringBuilder constructorDescriptor = new StringBuilder("(");

        for(StructProperty property: this.structProperties) 
        {
            String fieldName = property.identifier.token;
            String fieldDescriptor = this.determineByteCodePrefix(property.propertyType);
            constructorDescriptor.append(fieldDescriptor);
            
            cw.visitField(Opcodes.ACC_PUBLIC, fieldName, fieldDescriptor, null, null).visitEnd();
        }
        
        constructorDescriptor.append(")V"); // Building the init-method.

        this.emitConstructor(cw, constructorDescriptor.toString());

        cw.visitEnd();

        byte[] structByteCode = cw.toByteArray();

        context.registerClass(this.identifier.token, structByteCode);
    }

    public void emitConstructor(ClassWriter cw, String constructorDescriptor) throws Exception 
    {  
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", constructorDescriptor, null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0); // Loading the this keyword.
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);

        int currVarIndex = 1;

        for(StructProperty property: this.structProperties) 
        {
            String fieldDescriptor = this.determineByteCodePrefix(property.propertyType);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            
            if(fieldDescriptor.equals("I"))
            {
                mv.visitVarInsn(Opcodes.ILOAD, currVarIndex);
            } else if(fieldDescriptor.equals("F")) {
                mv.visitVarInsn(Opcodes.FLOAD, currVarIndex);
            } else {
                mv.visitVarInsn(Opcodes.ALOAD, currVarIndex);
            }
            currVarIndex += 1;
            mv.visitFieldInsn(Opcodes.PUTFIELD, this.identifier.token, property.identifier.token, fieldDescriptor);
        }
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    };
}
