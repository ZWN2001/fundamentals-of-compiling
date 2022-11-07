package Compiler.AST;
import Compiler.Token.Token;

public class UnaryOpNode extends AstNode {
    public Token op;
    public AstNode right;

    public UnaryOpNode(Token op, AstNode right) {
        this.op = op;
        this.right = right;
    }

    @Override
    public String toString() {
        return "UnaryOpNode{" +
                "op=" + op +
                ", right=" + right +
                '}';
    }
}

