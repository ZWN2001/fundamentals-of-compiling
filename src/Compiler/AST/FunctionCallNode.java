package Compiler.AST;
import Compiler.Token.Token;

import java.util.ArrayList;

public class FunctionCallNode extends AstNode{
    public String functionName;
    public ArrayList<AstNode> actualParameterNodes;
    public Token token;

public FunctionCallNode(String functionName, ArrayList<AstNode> actualParameterNodes, Token token) {
        this.functionName = functionName;
        this.actualParameterNodes = actualParameterNodes;
        this.token = token;
    }
}
