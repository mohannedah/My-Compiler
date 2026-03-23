package compiler.Parser.Statements;

import java.util.List;

import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Identifier;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Expressions.IdentifierExpression;

public class StructStatement extends Statement {
    public List<StructProperty> structProperties;
    public Identifier identifier;

    public StructStatement(Identifier identifier, List<StructProperty> structProperties) 
    {
        this.identifier = identifier;
        this.structProperties = structProperties;
        for (StructProperty property : structProperties) 
        {
            this.nodeChildren.add(property);
        }
    };

    public String getName() 
    {
        return this.nodeType + "(" + this.identifier + ")";
    }

   @Override
    public void analyze(SymbolTable scope) throws Exception 
    {
        IdentifierExpression structIdExpr = new IdentifierExpression(this.identifier);
        
        // Assuming your IdentifierType constructor takes the string name of the type
        IdentifierType structType = new IdentifierType(this.identifier.token, false);

        boolean inserted = scope.insert(structIdExpr, structType, false);
        if (!inserted) {
            throw new SemanticAnalysisException(
                "Semantic Error: A struct or variable named '" + this.identifier.token + "' is already declared in this scope."
            );
        }

        if (this.structProperties != null) {
            scope.insertStruct(structType, structProperties);
        }
    }
}
