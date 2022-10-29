package Compiler.AST;
import Compiler.Token.Token;

import java.util.ArrayList;

public class BlockNode extends AstNode {
    public Token ltok;
    public Token rtok;
    public ArrayList<AstNode> stmts;

    public BlockNode(Token ltok, Token rtok, ArrayList<AstNode> stmts) {
        this.ltok = ltok;
        this.rtok = rtok;
        this.stmts = stmts;
    }
}
