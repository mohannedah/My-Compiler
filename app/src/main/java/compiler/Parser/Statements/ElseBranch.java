package compiler.Parser.Statements;

import compiler.DataStructures.SymbolTable;

public class ElseBranch extends Statement {
    public BlockStatement body;
    public ElseBranch(BlockStatement statement) 
    {
        this.body = statement;
        this.nodeChildren.addLast(statement);
    }
    @Override
    public void analyze(SymbolTable scope) throws Exception {
        this.body.analyze(scope);
    };
}
