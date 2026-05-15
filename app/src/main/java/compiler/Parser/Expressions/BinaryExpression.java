package compiler.Parser.Expressions;

import compiler.Constants;
import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.NumberToken;
import compiler.Lexer.Tokens.Operator;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Statements.IdentifierType;
import compiler.Utils;

public class BinaryExpression extends Expression {
    public Expression leftOperand, rightOperand;
    public Operator operator;   
    public BinaryExpression(Expression leftOperand, Expression rightOperand, Operator operator) 
    {
        super();
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.operator = operator;
        this.nodeChildren.addLast(leftOperand);
        this.nodeChildren.addLast(rightOperand);
    }

    public String getName() 
    {
        return this.nodeType + "(" + this.operator.token + ")";
    }

    public IdentifierType getResultantType(SymbolTable table) throws Exception
    {
        try {
            // Try getting the field from the cache. If it doesn't exist, it will throw an UnsupportedOperationException, and then we will compute the type and cache it for future calls.
            return super.getResultantType(table);
        } catch (UnsupportedOperationException e) {
            if(rightOperand == null) 
            {

            }

            if(leftOperand == null) {
                if(!Utils.checkExists(Constants.UNARY_OPERATORS, operator.token)) {
                    throw new SemanticAnalysisException("Expected a Unary Operator for expressions with no left operands.");
                }
                // We assume that there is a '0' to ease the calculation afterwards.
                this.leftOperand = new NumberExpression(new NumberToken("0"));
            }
            IdentifierType leftOperandType = leftOperand.getResultantType(table), rightOperandType = rightOperand.getResultantType(table);
            IdentifierType resultantType = operator.getResultantTypeAfterOperation(leftOperandType, rightOperandType);
            this.cachedIdentifierType = resultantType;
            return resultantType;
        }
    }

    @Override
    public void emit(EvaluationContext context) throws Exception 
    {
        IdentifierType castingValue = this.determineCastingType(this.leftOperand.getResultantType(null), this.rightOperand.getResultantType(null));

        leftOperand.emit(context);

        leftOperand.tryEmitCasting(context, castingValue);

        rightOperand.emit(context);

        rightOperand.tryEmitCasting(context, castingValue);

        operator.emitOperation(context, castingValue);       
    };
}
