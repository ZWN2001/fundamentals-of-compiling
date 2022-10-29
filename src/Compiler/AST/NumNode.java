package Compiler.AST;
import Compiler.Token.Token;

public class NumNode extends AstNode {
    public Token token;
    public String value;

    public NumNode(Token token) {
        this.token = token;
        this.value = token.value;
    }
}
