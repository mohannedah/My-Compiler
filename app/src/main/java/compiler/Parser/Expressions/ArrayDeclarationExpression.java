package compiler.Parser.Expressions;

public class ArrayDeclarationExpression extends Expression {
    public Expression enclosedSize;
    public ArrayDeclarationExpression(Expression enclosedSize) 
    {
        super();
        this.enclosedSize = enclosedSize;
    };
}
