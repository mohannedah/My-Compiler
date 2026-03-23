package compiler.Parser.Statements;

import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Identifier;

public class StructProperty extends Statement {
    public IdentifierType propertyType;
    public Identifier identifier;

    public StructProperty(Identifier identifier, IdentifierType propertyType) 
    {
        this.propertyType = propertyType;
        this.identifier = identifier;
    }

    @Override
    public void analyze(SymbolTable scope) throws Exception {
        
    };
}
