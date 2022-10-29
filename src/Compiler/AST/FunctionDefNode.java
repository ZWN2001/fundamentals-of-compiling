package Compiler.AST;

import java.util.ArrayList;

public class FunctionDefNode extends AstNode {
    public AstNode typeNode;
    public String functionName;
    public ArrayList<AstNode> formalParameters;
    public AstNode blockNode;
    public int offset;

    public FunctionDefNode(AstNode typeNode, String functionName, ArrayList<AstNode> formalParameters, AstNode blockNode) {
        this.typeNode = typeNode;
        this.functionName = functionName;
        this.formalParameters = formalParameters;
        this.blockNode = blockNode;
        this.offset = 0;
    }
}
