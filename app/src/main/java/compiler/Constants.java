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
        "ARRAY"
    };

    public static final String[] TYPES = new String[] 
    {
        "INT",
        "FLOAT",
        "BOOL",
        "STRING",
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
        "-"
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
        '-'
    };

    public static final char OBJECT_ACCESSOR = '.';

    public static final int END_OF_INPUT = 65535;
}
