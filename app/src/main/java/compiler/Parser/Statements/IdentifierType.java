package compiler.Parser.Statements;
import java.util.Objects;

import compiler.Constants;
import compiler.Parser.ASTNode;
import compiler.Utils;

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
        if(this.isArray) {
            return this.nodeType + "(" + value + "[]" + ")";
        }
        return this.nodeType + "(" + value + ")";
    };

    public Boolean equals(IdentifierType otherType) 
    {
        return checkNumber(otherType) || this.value.equals(otherType.value) && Objects.equals(this.isArray, otherType.isArray);
    };

    public Boolean checkNumber(IdentifierType otherType) 
    {
        if(this.isArray || otherType.isArray) return false;

        return Utils.checkExists(Constants.TYPES_WITH_SUBTRACTION_OPERATION, otherType.value) && Utils.checkExists(Constants.TYPES_WITH_SUBTRACTION_OPERATION, this.value);
    };
}
