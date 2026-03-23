package compiler.DataStructures;

import java.util.Hashtable;
import java.util.List;

import compiler.Lexer.Tokens.Identifier;
import compiler.Parser.Expressions.IdentifierExpression;
import compiler.Parser.Statements.IdentifierType;
import compiler.Parser.Statements.StructProperty;
import compiler.Parser.Exceptions.SemanticAnalysisException; 

class IdentifierData {
    public IdentifierType identifierType;
    public boolean isConstant;

    public IdentifierData(IdentifierType identifierType, Boolean isConstant) 
    {
        this.identifierType = identifierType;
        this.isConstant = isConstant;
    }
}

public class SymbolTable {  
    public Hashtable<Identifier, IdentifierData> hashTable = new Hashtable<Identifier, IdentifierData>();
    public Hashtable<Identifier, List<IdentifierType>> hashTableForMethodArguments = new Hashtable<Identifier, List<IdentifierType>>();
    public Hashtable<Identifier, List<StructProperty>> hashTableForStructs = new Hashtable<Identifier, List<StructProperty>>();
    public SymbolTable parentScope;

    public SymbolTable(SymbolTable parentScope) 
    {
        this.parentScope = parentScope;
    }

    public boolean contains(IdentifierExpression identifierExpression) 
    {
        Identifier identifier = new Identifier(identifierExpression.value);
        return hashTable.containsKey(identifier);
    }

    public boolean containsMethod(IdentifierExpression identifierExpression) 
    {
        Identifier identifier = new Identifier(identifierExpression.value);
        return hashTableForMethodArguments.containsKey(identifier);
    }

    public boolean insert(IdentifierExpression identifierExpression, IdentifierType identifierType, Boolean isConstant) 
    {
        Identifier identifier = new Identifier(identifierExpression.value);

        if(hashTable.containsKey(identifier)) 
        {
            return hashTable.get(identifier).equals(identifierType);
        }

        hashTable.put(identifier, new IdentifierData(identifierType, isConstant));
        return true;
    }

    public boolean insertMethod(IdentifierExpression identifierExpression, List<IdentifierType> arguments) 
    {
        Identifier identifier = new Identifier(identifierExpression.value);

        if(hashTableForMethodArguments.containsKey(identifier)) 
        {
            return hashTableForMethodArguments.get(identifier).equals(arguments);
        }

        hashTableForMethodArguments.put(identifier, arguments);
        return true;
    }

    public boolean insertStruct(IdentifierType structType, List<StructProperty> properties) 
    {
        Identifier identifier = new Identifier(structType.value);

        if(hashTableForStructs.containsKey(identifier)) 
        {
            return false;
        }

        hashTableForStructs.put(identifier, properties);
        return true;
    }

    public IdentifierType findKey(IdentifierExpression identifierExpression) throws SemanticAnalysisException
    {
        if(this.contains(identifierExpression)) 
        {
            return this.hashTable.get(new Identifier(identifierExpression.value)).identifierType;
        }
        
        if(this.parentScope == null) {
            throw new SemanticAnalysisException(String.format("Variable '%s' is not defined in the current scope.", identifierExpression.value));
        }
        
        return this.parentScope.findKey(identifierExpression);
    }

    public boolean isConstant(IdentifierExpression identifierExpression) throws SemanticAnalysisException {
        if(this.contains(identifierExpression)) 
        {
            return this.hashTable.get(new Identifier(identifierExpression.value)).isConstant;
        }
        
        if(this.parentScope == null) {
            throw new SemanticAnalysisException(String.format("Variable '%s' is not defined in the current scope.", identifierExpression.value));
        }
        
        return this.parentScope.isConstant(identifierExpression);
    };

    public List<IdentifierType> getMethodArguments(IdentifierExpression identifierExpression) throws SemanticAnalysisException
    {
        if(this.containsMethod(identifierExpression)) 
        {
            return this.hashTableForMethodArguments.get(new Identifier(identifierExpression.value));
        }
        
        if(this.parentScope == null) {
            throw new SemanticAnalysisException(String.format("Method '%s' is not defined.", identifierExpression.value));
        }
        
        return this.parentScope.getMethodArguments(identifierExpression);
    }

    public List<StructProperty> getStructProperties(IdentifierType structType) throws SemanticAnalysisException
    {
        Identifier identifier = new Identifier(structType.value);
        
        if(this.hashTableForStructs.containsKey(identifier)) 
        {
            return this.hashTableForStructs.get(identifier);
        }
        
        if(this.parentScope == null) {
            throw new SemanticAnalysisException(String.format("Struct type '%s' is not defined.", structType.value));
        }
        
        return this.parentScope.getStructProperties(structType);
    }
}