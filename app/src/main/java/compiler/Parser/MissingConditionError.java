package compiler.Parser;

import compiler.Parser.Exceptions.SemanticAnalysisException;

public class MissingConditionError extends SemanticAnalysisException {
    public MissingConditionError(String message) {
        super(message);
    }
}
