package Compiler.AST;

import Compiler.Analyzer.Symbol.Symbol;

public class FormalParamNode extends AstNode {
    public AstNode typeNode;
    public AstNode parameterNode;
    public Symbol parameterSymbol;

    public FormalParamNode(AstNode typeNode, AstNode parameterNode) {
        this.typeNode = typeNode;
        this.parameterNode = parameterNode;
//        this.parameterSymbol = "";
    }

    @Override
    public String toString() {
        return "FormalParamNode{" +
                "typeNode=" + typeNode +
                ", parameterNode=" + parameterNode +
                ", parameterSymbol='" + parameterSymbol + '\'' +
                '}';
    }
}
