package compiler;

public class Constants {
    public static final String[] KEYWORDS = new String[] 
    {
        "var",
        "const",
        "for",
        "while",
        "if",
        "else",
        "elseif",
        "record",
        "def",
        "return",
        "delete"
    };

    public static final String[] TYPES = new String[] 
    {
        "int",
        "string",
        "bool",
        "char",
        "real"
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
        "<>",
        "<=",
        ">=",
        "and",
        "or",
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
    };

    public static final char OBJECT_ACCESSOR = '.';

    public static final int END_OF_INPUT = 65535;
}
