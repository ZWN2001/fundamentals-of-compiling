package Compiler.AST;

import Compiler.Token.Token;

public class BinaryOpNode extends AstNode {
    public AstNode left;
    public AstNode right;
    public Token op;

    public BinaryOpNode(AstNode left, AstNode right, Token op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }
}
