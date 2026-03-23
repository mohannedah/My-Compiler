package compiler.Parser.Statements;

import java.util.List;

import compiler.DataStructures.SymbolTable; 

public class BlockStatement extends Statement {
    public List<Statement> statements;
    
    public BlockStatement(List<Statement> statements) 
    {
        super();
        this.statements = statements;
        
        if (statements != null) {
            for(Statement statement : statements) 
            {
                nodeChildren.addLast(statement);
            }
        }
    }

    public void analyze(SymbolTable parentScope) throws Exception 
    {
        SymbolTable localScope = new SymbolTable(parentScope);

        if (this.statements != null) {
            for (Statement statement : this.statements) 
            {
                statement.analyze(localScope); 
            }
        }
    }

}