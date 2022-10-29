package Compiler.AST;

public class FormalParamNode extends AstNode {
    public AstNode typeNode;
    public AstNode parameterNode;
    public String parameterSymbol;

    public FormalParamNode(AstNode typeNode, AstNode parameterNode) {
        this.typeNode = typeNode;
        this.parameterNode = parameterNode;
        this.parameterSymbol = "";
    }
}
