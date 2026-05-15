package compiler.DataStructures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class EvaluationContext {
    public Map<String, byte[]> compiledClasses = new HashMap<>();
    public Set<String> definedStaticFields = new HashSet<String>();
    public MethodVisitor currMethodVisitor; 
    public String currMethodName;
    public ClassWriter classWriter;

    private final Map<String, Integer> localVariables = new HashMap<>();
    private int nextFreeIndex = 0;

    public EvaluationContext()
    {
         
    }

    EvaluationContext(Map<String, byte[]> compiledClasses)
    {
        this.compiledClasses = compiledClasses;
    }

    public EvaluationContext createNestedContext() {
        return new EvaluationContext(this.compiledClasses);
    };

    public int allocateLocalVariable(String name) {
        int index = nextFreeIndex++;
        localVariables.put(name, index);
        return index;
    }

    public int getLocalVariableIndex(String name) {
        if (!localVariables.containsKey(name)) {
            throw new RuntimeException("Compiler Bug: Variable " + name + " not found during CodeGen!");
        }
        return localVariables.get(name);
    }
    
    public MethodVisitor getMethodVisitor() {
        return this.currMethodVisitor;
    }

    public ClassWriter getClassWriter() {
        return this.classWriter;
    };

    public void setMethodVisitor(MethodVisitor mv, String methodName) 
    {
        this.currMethodVisitor = mv;
        this.currMethodName = methodName;
    };

    public void setClassWriter(ClassWriter cw) {
        this.classWriter = cw;  
    };

    public void registerClass(String className, byte[] byteCode) 
    {
        this.compiledClasses.put(className, byteCode);
    };

    public void addStaticField(String staticField) 
    {
        if(this.definedStaticFields.contains(staticField)) {
            throw new RuntimeException(String.format("Compiler Bug: Static field %s is already defined!", staticField));
        }
        this.definedStaticFields.add(staticField);
    };

    public boolean staticFieldExists(String staticField) 
    {
        return this.definedStaticFields.contains(staticField);
    }
}