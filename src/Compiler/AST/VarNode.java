package Compiler.AST;
import Compiler.Analyzer.Symbol.Symbol;
import Compiler.Token.Token;

public class VarNode extends AstNode {
    public Token token;
    public String value;
    public Symbol symbol;

    public VarNode(Token token) {
        this.token = token;
        this.value = token.value;
    }

    @Override
    public String toString() {
        return "VarNode{" +
                "token=" + token +
                ", value='" + value + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
