package compiler.Lexer;

public class State {
    public int lineNumber;
    public int colNumber;

    public State(int lineNumber, int colNumber) {
        this.lineNumber = lineNumber;
        this.colNumber = colNumber;
    }
}
