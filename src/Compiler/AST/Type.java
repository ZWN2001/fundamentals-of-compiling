package Compiler.AST;
import Compiler.Token.Token;

public class Type extends AstNode {
    public Token token;
    public String value;

    public Type(Token token) {
        this.token = token;
        this.value = token.value;
    }
}
