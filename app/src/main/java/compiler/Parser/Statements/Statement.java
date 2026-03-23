package compiler.Parser.Statements;

import compiler.DataStructures.SymbolTable;
import compiler.Parser.ASTNode;

public abstract class Statement extends ASTNode {
    public Statement() 
    {
        super();
    };

    public abstract void analyze(SymbolTable scope) throws Exception;
}
