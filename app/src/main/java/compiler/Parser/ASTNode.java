package compiler.Parser;

import java.util.ArrayList;
import java.util.List;

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

