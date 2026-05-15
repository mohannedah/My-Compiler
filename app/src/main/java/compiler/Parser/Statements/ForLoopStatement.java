package compiler.Parser.Statements;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import compiler.DataStructures.EvaluationContext;
import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Identifier;
import compiler.Parser.Exceptions.SemanticAnalysisException;
import compiler.Parser.Expressions.Expression;
import compiler.Parser.Expressions.IdentifierExpression;
import compiler.Parser.Expressions.RangeExpression;

public class ForLoopStatement extends Statement {
    public Identifier loopIdentifier;
    public RangeExpression rangeExpression;
    public Expression updateExpression;
    public BlockStatement body;
    public ForLoopStatement(Identifier identifier, RangeExpression rangeExpression, Expression updateExpression,
            BlockStatement body) {
        this.loopIdentifier = identifier;
        this.rangeExpression = rangeExpression;
        this.updateExpression = updateExpression;
        this.body = body;
        this.nodeChildren.addFirst(updateExpression);
        this.nodeChildren.addFirst(rangeExpression);
        this.nodeChildren.addFirst(new IdentifierExpression(loopIdentifier));
        this.nodeChildren.addLast(body);
    }
    
    @Override
    public void analyze(SymbolTable scope) throws Exception {
        SymbolTable loopHeaderScope = new SymbolTable(scope);

        IdentifierExpression loopVarExpr = new IdentifierExpression(this.loopIdentifier);
        IdentifierType loopVarType = new IdentifierType("INT", false); 
        boolean inserted = loopHeaderScope.insert(loopVarExpr, loopVarType, false);
        if (!inserted) {
            throw new SemanticAnalysisException("Failed to initialize loop variable: " + loopIdentifier.token);
        }

        if (this.rangeExpression != null) {
            this.rangeExpression.getResultantType(loopHeaderScope);
        }

        if (this.updateExpression != null) {
            IdentifierType type = this.updateExpression.getResultantType(loopHeaderScope);
            if(type.value != "INT") {
                throw new SemanticAnalysisException("The update expression must be return an Integer.");
            }
        }

        if (this.body != null) {
            this.body.analyze(loopHeaderScope);
        }
    }

   @Override
    public void emit(EvaluationContext context) throws Exception 
    {
        /*
            int i = 0;
            Limit = 100;
            START_LABEL:
            if Limit <= i goto END_LABEL
            {
                # STATEMENT_1
                # STATEMENT_2
            }
            i++;
            goto START_LABEL
            END_LABEL:
            POP Limit
        */

        MethodVisitor mv = context.getMethodVisitor();

        Label loopStartLabel = new Label();
        Label loopEndLabel = new Label();
        
        VariableDeclarationStatement declarationStatement = new VariableDeclarationStatement(
            new IdentifierType("INT", false), 
            this.loopIdentifier
        );

        declarationStatement.emit(context);

        int loopVarIndex = context.getLocalVariableIndex(this.loopIdentifier.token);
        this.rangeExpression.leftOperand.emit(context);
        mv.visitVarInsn(Opcodes.ISTORE, loopVarIndex);

        this.rangeExpression.rightOperand.emit(context);
        
        mv.visitLabel(loopStartLabel);
        
        mv.visitInsn(Opcodes.DUP); 

        mv.visitVarInsn(Opcodes.ILOAD, loopVarIndex);
        
        mv.visitJumpInsn(Opcodes.IF_ICMPLE, loopEndLabel);        

        if(this.body != null) {
            this.body.emit(context);
        }

        this.updateExpression.emit(context);
        mv.visitVarInsn(Opcodes.ISTORE, loopVarIndex);

        mv.visitJumpInsn(Opcodes.GOTO, loopStartLabel);

        mv.visitLabel(loopEndLabel);
        
        mv.visitInsn(Opcodes.POP); 
    }
}
