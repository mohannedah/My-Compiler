package compiler.Parser.Exceptions;

public class NoMoreTokensException extends Exception {
    public NoMoreTokensException() 
    {
        super("No more tokens");
    };
}
