package compiler.Parser.Helpers;
 
import java.util.List;

import compiler.DataStructures.EvaluationContext;
import compiler.Parser.Expressions.Expression;

@FunctionalInterface
public interface BuiltInEmitter {
    void emit(EvaluationContext context, List<Expression> paramaters) throws Exception;
}