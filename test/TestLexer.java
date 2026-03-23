import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import java.io.StringReader;
import compiler.Lexer.Lexer;

public class TestLexer {
    
    @Test
    public void test() {
        StringReader sourceCode = new StringReader("# Good luck\n" + //
                        "\n" + //
                        "final INT i = 3;\n" + //
                        "final FLOAT j = 3.2*5.0;\n" + //
                        "final INT k = i*3;\n" + //
                        "final STRING message = \"Hello\";\n" + //
                        "final BOOL isEmpty  = true;\n" + //
                        "\n" + //
                        "coll Point {\n" + //
                        "    INT x;\n" + //
                        "    INT y;\n" + //
                        "}\n" + //
                        "coll Person {\n" + //
                        "    STRING name;\n" + //
                        "    Point location;\n" + //
                        "    INT[] history;\n" + //
                        "}\n" + //
                        "\n" + //
                        "INT a = 3;\n" + //
                        "INT[] c  = INT ARRAY [5] ;  # new ARRAY of length 5\n" + //
                        "Person d = Person(\"me\", Point(3,7), INT ARRAY [i*2] ); # new person\n" + //
                        "\n" + //
                        "def INT square(INT v) {\n" + //
                        "    return v*v;\n" + //
                        "}\n" + //
                        "\n" + //
                        "def Point copyPoints(Point[] p) {\n" + //
                        "    return Point(p[0].x+p[1].x, p[0].y+p[1].y);\n" + //
                        "}\n" + //
                        "\n" + //
                        "def main() {\n" + //
                        "    INT value = read_INT();\n" + //
                        "    println(square(value));\n" + //
                        "    INT i;\n" + //
                        "    for (i; 1 -> 100; i+1) {\n" + //
                        "        while (value=/=3) {\n" + //
                        "            if (i > 10){\n" + //
                        "                value = value - 1;\n" + //
                        "            } else {\n" + //
                        "                write(message);\n" + //
                        "            }\n" + //
                        "        }\n" + //
                        "    }\n" + //
                        "\n" + //
                        "    i = (i+2)*2;\n" + //
                        "}");

        PushbackReader reader = new PushbackReader(sourceCode, 1);

        CompilerLexer lexer = new CompilerLexer();

        LinkedList<Token> tokens = new LinkedList<>();
        try {
            Token currToken = lexer.nextToken();
            while(true) {
                tokens.addLast(currToken);
                currToken = lexer.nextToken();
            }
        } catch (NoSuchElementException e) {
            // TODO: handle exception
        }

        System.out.println(tokens);
    }

}
