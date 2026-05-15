package compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import compiler.Parser.Expressions.Expression;
import compiler.Parser.Helpers.BuiltInEmitter;
import compiler.Parser.Statements.CommentStatement;
import compiler.Parser.Statements.ConstantAssignment;
import compiler.Parser.Statements.IdentifierType;
import compiler.Parser.Statements.MethodDefinitionStatement;
import compiler.Parser.Statements.StructStatement;
import compiler.Parser.Statements.VariableAssignmentStatement;
import compiler.Parser.Statements.VariableDeclarationStatement;

public class Constants {
    public static final String[] KEYWORDS = new String[] 
    {
        "coll",
        "final",
        "def",
        "while",
        "if",
        "else",
        "elseif",
        "not",
        "return",
        "ARRAY",
        "for"
    };

    public static final String[] BUILT_IN_FUNCTIONS = new String[] 
    {
        "read_INT",
        "read_FLOAT",
        "read_STRING",
        "print_INT",
        "print_FLOAT",
        "print",
        "println"
    };

    public static final String[] TYPES = new String[] 
    {
        "INT",
        "FLOAT",
        "BOOL",
        "STRING",
    };

    public static final String[] TYPES_WITH_ADDITION_OPERATION = new String[] 
    {
        "INT",
        "FLOAT",
        "STRING",
    };

    public static final String[] TYPES_WITH_SUBTRACTION_OPERATION = new String[] 
    {
        "INT",
        "FLOAT",
    };

    public static final String[] TYPES_WITH_BOOLEAN_OPERATIONS = new String[] 
    {
        "BOOL"
    };

    public static final String[] OPERATORS = new String[]
    {
        "*",
        "/",
        "%",
        "+",
        "-",
        "=",
        "<",
        ">",
        "==",
        "<=",
        ">=",
        "&&",
        "||",
        "->",
        "=/=",
    };

    public static final String[] BOOLEAN_OPERATORS = new String[] 
    {
        "&&",
        "||",
    };

    public static final String[] UNARY_OPERATORS = new String[] 
    {
        "+",
        "-"
    };

    public static final String[] COMPARISON_OPERATORS = new String[] 
    {
        ">",
        "<",
        "==",
        "<=",
        ">=",
        "=/="
    };

    public static final String[] ARITHMETIC_OPERATORS = new String[] 
    {
        "*",
        "/",
        "%",
        "+",
        "-",
    };

    public static final char[] SEPERATORS = new char[] 
    {
        ';',
        ',',
    };

    public static final char[] BRACKETS = new char[] 
    {
        '(',
        ')',
        '[',
        ']',
        '{',
        '}',
    };

    public static final char[] WHITE_SPACE_CHARACTERS = new char[] 
    {
        '\t',
        '\n',
        ' ',
        '\r',
    };

    public static final char[] SPECIAL_CHARACTERS = new char[] 
    {
        '*',
        '/',
        '%',
        '+',
        '-',
        '=',
        '<',
        '>',
        '-',
        '|',
        '&'
    };

    public static final Class<?>[] ALLOWED_GLOBAL_STATEMENTS = 
    {
        MethodDefinitionStatement.class,
        VariableDeclarationStatement.class,
        VariableAssignmentStatement.class,
        ConstantAssignment.class,
        CommentStatement.class,
        StructStatement.class,
    };

    public static final Map<String, Map<String, Integer>> CASTING_INSTRUCTIONS;
    
    public static final Map<String, BuiltInEmitter> METHOD_EMITTER_BY_METHOD_NAME;

    static {
        Map<String, BuiltInEmitter> tempMap = new HashMap<>();

        tempMap.put("print_INT", (context, parameters) -> {
            MethodVisitor mv = context.getMethodVisitor();
            mv.visitFieldInsn(
            Opcodes.GETSTATIC, 
            "java/lang/System", 
            "out", 
            "Ljava/io/PrintStream;"
            );

            if (parameters != null) {
                for (Expression param : parameters) {
                    param.emit(context); 
                }
            }    

            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
        });

        tempMap.put("print_FLOAT", (context, parameters) -> {
            MethodVisitor mv = context.getMethodVisitor();
            mv.visitFieldInsn(
            Opcodes.GETSTATIC, 
            "java/lang/System", 
            "out", 
            "Ljava/io/PrintStream;"
            );
            if (parameters != null) {
                for (Expression param : parameters) {
                    param.emit(context); 
                }
            }    
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(F)V", false);
        });

        tempMap.put("print", (context, parameters) -> {
            MethodVisitor mv = context.getMethodVisitor();
            mv.visitFieldInsn(
            Opcodes.GETSTATIC, 
            "java/lang/System", 
            "out", 
            "Ljava/io/PrintStream;"
            );
            if (parameters != null) {
                for (Expression param : parameters) {
                    param.emit(context); 
                }
            }    
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
        });

        tempMap.put("println", (context, parameters) -> {
            MethodVisitor mv = context.getMethodVisitor();
            mv.visitFieldInsn(
            Opcodes.GETSTATIC, 
            "java/lang/System", 
            "out", 
            "Ljava/io/PrintStream;"
            );
            if (parameters != null) {
                for (Expression param : parameters) {
                    param.emit(context); 
                }
            }
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        });
        
        tempMap.put("read_INT", (context, parameters) -> {
            MethodVisitor mv = context.getMethodVisitor();
            mv.visitTypeInsn(Opcodes.NEW, "java/util/Scanner");
            mv.visitInsn(Opcodes.DUP);
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V", false);
            if (parameters != null) {
                for (Expression param : parameters) {
                    param.emit(context); 
                }
            }    
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Scanner", "nextInt", "()I", false);
        });

        tempMap.put("read_FLOAT", (context, parameters) -> {
            MethodVisitor mv = context.getMethodVisitor();
            mv.visitTypeInsn(Opcodes.NEW, "java/util/Scanner");
            mv.visitInsn(Opcodes.DUP);
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V", false);
            if (parameters != null) {
                for (Expression param : parameters) {
                    param.emit(context); 
                }
            }    
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Scanner", "nextFloat", "()F", false);
        });

        tempMap.put("read_STRING", (context, parameters) -> {
            MethodVisitor mv = context.getMethodVisitor();
            mv.visitTypeInsn(Opcodes.NEW, "java/util/Scanner");
            mv.visitInsn(Opcodes.DUP);
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V", false);
            if (parameters != null) {
                for (Expression param : parameters) {
                    param.emit(context); 
                }
            }    
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Scanner", "nextLine", "()Ljava/lang/String;", false);
        });

        METHOD_EMITTER_BY_METHOD_NAME = Collections.unmodifiableMap(tempMap);
    }

    static 
    {
        Map<String, Map<String, Integer>> tempMap = new HashMap<>();

        Map<String, Integer> intCasts = new HashMap<>();
        intCasts.put("FLOAT", Opcodes.I2F);

        tempMap.put("INT", intCasts);

        Map<String, Integer> floatCasts = new HashMap<>();
        floatCasts.put("INT", Opcodes.F2I);
        tempMap.put("FLOAT", floatCasts);

        CASTING_INSTRUCTIONS = Collections.unmodifiableMap(tempMap);
    }

   

    public static Map<String, List<IdentifierType>> getBuiltInMethodsArgumentMapping() {
        Map<String, List<IdentifierType>> argMap = new HashMap<>();

        
        argMap.put("read_INT", new ArrayList<>());
        argMap.put("read_FLOAT", new ArrayList<>());
        argMap.put("read_STRING", new ArrayList<>());

        argMap.put("print_INT", Arrays.asList(new IdentifierType("INT", false)));
        argMap.put("print_FLOAT", Arrays.asList(new IdentifierType("FLOAT", false)));
        
        argMap.put("print", Arrays.asList(new IdentifierType("STRING", false)));
        argMap.put("println", Arrays.asList(new IdentifierType("STRING", false)));

        argMap.put("not", Arrays.asList(new IdentifierType("BOOL", false))); 
        argMap.put("str", Arrays.asList(new IdentifierType("INT", false)));

        argMap.put("floor", Arrays.asList(new IdentifierType("FLOAT", false)));
        argMap.put("ceil", Arrays.asList(new IdentifierType("FLOAT", false)));

        argMap.put("length", Arrays.asList(new IdentifierType("ARRAY", false)));

        return argMap;
    }

    public static Map<String, IdentifierType> getReturnTypeMapping() {
        Map<String, IdentifierType> returnMap = new HashMap<>();

        returnMap.put("read_INT", new IdentifierType("INT", false));
        returnMap.put("read_FLOAT", new IdentifierType("FLOAT", false));
        returnMap.put("read_STRING", new IdentifierType("STRING", false));

        IdentifierType voidType = new IdentifierType("void", false);
        returnMap.put("print_INT", voidType);
        returnMap.put("print_FLOAT", voidType);
        returnMap.put("print", voidType);
        returnMap.put("println", voidType);
 
        returnMap.put("not", new IdentifierType("BOOL", false));
        returnMap.put("str", new IdentifierType("STRING", false));

        returnMap.put("floor", new IdentifierType("INT", false));
        returnMap.put("ceil", new IdentifierType("INT", false));

        returnMap.put("length", new IdentifierType("INT", false));

        return returnMap;
    }

    public static final char OBJECT_ACCESSOR = '.';
    public static final int END_OF_INPUT = 65535;
}
