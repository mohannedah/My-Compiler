package compiler.Parser.Expressions;

import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Identifier;
import compiler.Parser.Statements.IdentifierType;

public class IdentifierExpression extends Expression {
    public String value;
    public IdentifierType identifierType;

    public IdentifierExpression(Identifier token) 
    {
        super();
        this.value = token.token;
    };

    public String getName() 
    {
        return this.nodeType + "(" + value + ")";
    };

    @Override
    public IdentifierType getResultantType(SymbolTable table) throws Exception
    {
        return table.findKey(this);
    };
}