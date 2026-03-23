package compiler.Parser.Expressions;

import compiler.DataStructures.SymbolTable;
import compiler.Parser.Statements.IdentifierType;
import compiler.Parser.Statements.Statement;

public class Expression extends Statement {
    public Expression() 
    {
        super();
    };

    public IdentifierType getResultantType(SymbolTable table) throws Exception
    {
        throw new UnsupportedOperationException("To be implemented in the child classes as an override.");
    }

    @Override
    public void analyze(SymbolTable scope) throws Exception {
    }
}
