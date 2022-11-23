package Compiler;

import Compiler.AST.*;
import Compiler.Analyzer.NodeVisitor;
import Compiler.Analyzer.ScopedSymbolTable;
import Compiler.Analyzer.Symbol.FunctionSymbol;
import Compiler.Analyzer.Symbol.ParameterSymbol;
import Compiler.Analyzer.Symbol.Symbol;
import Compiler.Analyzer.Symbol.VarSymbol;
import Compiler.Token.TokenType;

import java.util.ArrayList;
import java.util.Objects;

public class SemanticAnalyzer extends NodeVisitor {
    private final int index = 0;
    ScopedSymbolTable globalScope;
    ScopedSymbolTable currentScope;

    public SemanticAnalyzer() {
        //todo:enclosing_scope可能有问题
        globalScope = new ScopedSymbolTable("global", 0, null);
        currentScope = globalScope;
    }

    public void log(String msg) {
        System.out.println(msg);
    }

    public void visitUnaryOpNode(UnaryOpNode node) {
//        visit(node);
    }

    public void visitReturnNode(ReturnNode node) throws Exception {
        visit(node.right,index);
    }

    public void visitBinaryOpNode(BinaryOpNode node) throws Exception {
        visit(node.left,index);
        visit(node.right,index);
    }

    public void visitAssignNode(AssignNode node) throws Exception {
          // make sure the left side of assign is a varible
        if (!Objects.equals(((VarNode) node.left).token.type, TokenType.TK_IDENT)){
            throw new RuntimeException("Semantic Error: left side of assign must be a variable");
        }
        visit(node.right,index);
        visit(node.left,index);
    }

    public void visitIfNode(IfNode node) throws Exception {
        visit(node.condition,index);
        if(node.thenStmt != null){
            visit(node.thenStmt,index);
        }
        if(node.elseStmt != null){
            visit(node.elseStmt,index);
        }
    }

    public void visitBlockNode(BlockNode node) throws Exception {
        String blockName = currentScope.scopeName +" block" + (currentScope.scopeLevel + 1);
        log("ENTER scope: " + blockName);
        ScopedSymbolTable temp = new ScopedSymbolTable(blockName,
                currentScope.scopeLevel + 1, currentScope);
        currentScope = temp;
        for (AstNode eachnode : node.stmts) {
            visit(eachnode,index);
        }
        currentScope = currentScope.enclosingScope;
        log("LEAVE scope: " + blockName);
    }

    public void visitNumNode(NumNode node) {}

    public void visitVarNode(VarNode node) {
        String varName = node.value;
        Symbol symbol = currentScope.lookup(varName);
        if (symbol == null) {
            log("Error: Undefined variable '" + node.token.value + "'");
            System.exit(1);
        }else {
            node.symbol = symbol;
        }
    }

    public void visitVarDeclNode(VarDeclNode node) {
        String varName = ((VarNode)node.varNode).value;
        String varType = ((Type)node.typeNode).value;
        Offset.sum += 8;
        int varOffset = Offset.sum;
        Symbol symbol = new VarSymbol(varName, varType, varOffset);
        currentScope.insert(symbol);
//        ((VarNode)node.varNode).symbol = symbol;
    }

    public void visitFormalParamNode(FormalParamNode node){
        String parameterName = ((VarNode)node.parameterNode).value;
        String parameterType = ((Type)node.typeNode).value;
        Offset.sum += 8;
        int varOffset = -Offset.sum;
        Symbol symbol = new ParameterSymbol(parameterName, parameterType, varOffset);
        currentScope.insert(symbol);
        node.parameterSymbol = symbol;
    }

    public void visitFunctionDefNode(FunctionDefNode node) throws Exception {
        Offset.sum = 0;
        String funcName = node.functionName;
        FunctionSymbol functionSymbol = new FunctionSymbol(funcName);
        currentScope.insert(functionSymbol);

        ScopedSymbolTable temp = new ScopedSymbolTable(funcName,
                currentScope.scopeLevel + 1, currentScope);
        currentScope = temp;
        for (AstNode eachnode : node.formalParameters) {
            visit(eachnode,index);
        }
        visit(node.blockNode,index);
        node.offset = Offset.sum;
        currentScope = currentScope.enclosingScope;
        functionSymbol.blockAst = node.blockNode;
    }

    public void visitFunctionCallNode(FunctionCallNode node) {}

    void semanticAnalyze(ArrayList<AstNode> tree) throws Exception {
        for (AstNode node : tree) {
            if (node != null){
                visit(node,index);
            }
        }
    }

}
