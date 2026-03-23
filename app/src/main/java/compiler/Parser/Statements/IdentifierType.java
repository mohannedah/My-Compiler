package compiler.Parser.Statements;
import java.util.Objects;

import compiler.Parser.ASTNode;

public class IdentifierType extends ASTNode {
    public String value;
    public Boolean isArray = false;
    public IdentifierType(String type, Boolean isArray) 
    {
        super();
        this.value = type;
        this.isArray = isArray;
    };

    public String getName() 
    {
        return this.nodeType + "(" + value + ")";
    };

    public Boolean equals(IdentifierType otherType) 
    {
        return this.value.equals(otherType.value) && Objects.equals(this.isArray, otherType.isArray);
    };
}
