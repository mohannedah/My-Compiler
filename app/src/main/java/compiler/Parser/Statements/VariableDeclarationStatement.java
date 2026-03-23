package compiler.Parser.Statements;

import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Identifier;
import compiler.Parser.Expressions.IdentifierExpression;

public class VariableDeclarationStatement extends Statement {
    public IdentifierType identifierType;
    public Identifier identifier;
    public VariableDeclarationStatement(IdentifierType identifierType, Identifier identifier) 
    {
        super();
        this.identifierType = identifierType;
        this.identifier = identifier;
        this.nodeChildren.addLast(new IdentifierExpression(identifier));
        this.nodeChildren.addLast(identifierType);
    }
    @Override
    public void analyze(SymbolTable scope) throws Exception {
        scope.insert(new IdentifierExpression(identifier), this.identifierType, false);
    };
}
