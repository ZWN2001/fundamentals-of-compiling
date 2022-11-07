package Compiler.AST;

import Compiler.Token.Token;

public class AssignNode extends AstNode {
    public AstNode left;
    public AstNode right;
    public Token op;

    public AssignNode(AstNode left, AstNode right, Token op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    public String toString() {
        return "AssignNode{" +
                "left=" + left +
                ", right=" + right +
                ", op=" + op +
                '}';
    }
}
