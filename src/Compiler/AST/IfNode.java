package Compiler.AST;

public class IfNode extends AstNode {
    public AstNode condition;
    public AstNode thenStmt;
    public AstNode elseStmt;

    public IfNode(AstNode condition, AstNode thenStmt, AstNode elseStmt) {
        this.condition = condition;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }

    @Override
    public String toString() {
        return "IfNode{" +
                "condition=" + condition +
                ", thenStmt=" + thenStmt +
                ", elseStmt=" + elseStmt +
                '}';
    }
}
