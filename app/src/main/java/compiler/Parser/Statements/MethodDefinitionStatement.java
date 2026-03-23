package compiler.Parser.Statements;

import java.util.ArrayList;
import java.util.List;

import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Identifier;
import compiler.Parser.Expressions.IdentifierExpression;

public class MethodDefinitionStatement extends Statement {
    public Identifier identifier;
    public List<VariableDeclarationStatement> params;
    public IdentifierType returnType;
    public BlockStatement body;
    public MethodDefinitionStatement(Identifier identifier, IdentifierType returnType, List<VariableDeclarationStatement> params, BlockStatement body) {
        this.returnType = returnType;
        this.returnType.nodeType = "ReturnType";
        this.identifier = identifier;
        this.params = params;
        this.body = body;
        this.nodeChildren.addFirst(returnType);
        for(VariableDeclarationStatement statement : params) 
        {
            this.nodeChildren.addFirst(statement);
        }
        this.nodeChildren.addFirst(new IdentifierExpression(identifier));
        this.nodeChildren.add(body);
    }

    @Override
    public void analyze(SymbolTable scope) throws Exception {
        SymbolTable newScope = new SymbolTable(scope);
        List<IdentifierType> methodParamTypes = new ArrayList<IdentifierType>();
        for(VariableDeclarationStatement param : params) 
        {
            methodParamTypes.addLast(param.identifierType);
        }
        newScope.insertMethod(new IdentifierExpression(identifier), methodParamTypes);
        newScope.insert(new IdentifierExpression(identifier), returnType, false);
        body.analyze(newScope);
    }
}
