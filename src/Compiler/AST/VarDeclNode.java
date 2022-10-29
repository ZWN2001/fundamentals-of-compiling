package Compiler.AST;

public class VarDeclNode extends AstNode {
    public AstNode typeNode;
    public AstNode varNode;

    public VarDeclNode(AstNode typeNode, AstNode varNode) {
        this.typeNode = typeNode;
        this.varNode = varNode;
    }
}
