package Compiler.Analyzer.Symbol;

import Compiler.AST.AstNode;

import java.util.ArrayList;

public class FunctionSymbol extends Symbol{
    String name;
    ArrayList<FunctionSymbol> formalParams = new ArrayList<>();
    public AstNode blockAst;

    public FunctionSymbol(String name) {
        super(name);
    }

    public FunctionSymbol(String name, ArrayList<FunctionSymbol> formalParams){
        super(name);
        this.formalParams = formalParams;
    }
}
