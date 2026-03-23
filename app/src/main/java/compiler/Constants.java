package compiler;

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

    public static final char OBJECT_ACCESSOR = '.';

    public static final int END_OF_INPUT = 65535;
}
