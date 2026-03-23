package compiler.Parser.Statements;

import java.util.List;

import compiler.DataStructures.SymbolTable;
import compiler.Parser.ASTNode;

public class Program extends Statement {
    public List<Statement> statements;

    public Program(List<Statement> statements) {
        super();
        this.statements = statements;
        for(ASTNode statement : statements) 
        {
            this.nodeChildren.addLast(statement);
        };
    }

    @Override
    public void analyze(SymbolTable scope) throws Exception {
        for(Statement statement : this.statements) {
            statement.analyze(scope);
        }
    }
}
