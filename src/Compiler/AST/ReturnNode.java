package Compiler.AST;
import Compiler.Token.Token;

public class ReturnNode extends AstNode {
    public Token token;
    public AstNode right;
    public String currentFunctionName;

    public ReturnNode(Token token, AstNode right, String currentFunctionName) {
        this.token = token;
        this.right = right;
        this.currentFunctionName = currentFunctionName;
    }
}
