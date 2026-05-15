package compiler.Parser;

import java.util.ArrayList;
import java.util.List;

import compiler.DataStructures.EvaluationContext;
import compiler.Parser.Statements.IdentifierType;

public class ASTNode {
    public String nodeType;
    public List<ASTNode> nodeChildren;

    public ASTNode() 
    {
        this.nodeType = this.getClass().getSimpleName();
        this.nodeChildren = new ArrayList<>();
    };

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buildTreeString(buffer, "", "");
        return buffer.toString();
    }

    protected String getName() 
    {
        return this.nodeType;
    };

    public void emit(EvaluationContext context) throws Exception
    {
        // By default, do nothing. Only override this if the node can actually emit code (e.g. expressions and statements)
        throw new UnsupportedOperationException("emit() not implemented for the base ASTNode. Consider calling it from one of its subclasses." );
    };

    public String determineByteCodePrefix(IdentifierType identifierType) 
    {
        if (identifierType.isArray) return "[" + determineByteCodePrefix(new IdentifierType(identifierType.value, false));
        
        switch (identifierType.value.toUpperCase()) 
        {
            case "INT": return "I";
            case "FLOAT": return "F";
            case "BOOL": return "I";
            case "STRING": return "Ljava/lang/String;";
            case "VOID": return "V";
            default: return "L" + identifierType.value + ";"; 
        }
    };

    private void buildTreeString(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(this.getName());
        buffer.append('\n');

        List<ASTNode> children = this.nodeChildren;
        if (children == null || children.isEmpty()) {
            return;
        }

        for (int i = 0; i < children.size(); i++) {
            ASTNode child = children.get(i);
            if (child == null) continue;
            if (i == children.size() - 1) {
                child.buildTreeString(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            } else {
                child.buildTreeString(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            }
        }
    }

    public boolean equals(ASTNode node) {
        return this == node;
    };
}

