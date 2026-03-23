package compiler.Parser.Statements;

import compiler.DataStructures.SymbolTable;
import compiler.Lexer.Tokens.Comment;

public class CommentStatement extends Statement {
    public String value;
    public CommentStatement(Comment comment) 
    {
        super();
        this.value = comment.token;
    }
    @Override
    public void analyze(SymbolTable scope) throws Exception {
        // No checking needs to be done here.
    };
}
