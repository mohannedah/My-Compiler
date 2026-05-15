package compiler.Parser.Exceptions;

public class ScopeError extends SemanticAnalysisException {
    public ScopeError(String message) {
        super(message);
    }
}
