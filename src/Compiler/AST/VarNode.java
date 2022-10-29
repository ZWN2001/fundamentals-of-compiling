package Compiler.AST;
import Compiler.Token.Token;

public class VarNode extends AstNode {
    public Token token;
    public String value;
    public String symbol;

    public VarNode(Token token) {
        this.token = token;
        this.value = token.value;
        this.symbol = "";
    }
}
