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
    ScopedSymbolTable globalScope;
    ScopedSymbolTable currentScope;

    public SemanticAnalyzer() {
        //todo:enclosing_scope可能有问题
        globalScope = new ScopedSymbolTable("global", 0, null);
        currentScope = globalScope;
    }

    void log(String msg) {
        System.out.println(msg);
    }

    void visitUnaryOpNode(UnaryOpNode node) {
//        visit(node);
    }

    void visitReturnNode(ReturnNode node){
        visit(node.right);
    }

    void visitBinaryOpNode(BinaryOpNode node) {
        visit(node.left);
        visit(node.right);
    }

    void visitAssignNode(AssignNode node) {
          // make sure the left side of assign is a varible
        if (!Objects.equals(((VarNode) node.left).token.type, TokenType.TK_IDENT)){
            throw new RuntimeException("Semantic Error: left side of assign must be a variable");
        }
        visit(node.right);
        visit(node.left);
    }

    void visitIfNode(IfNode node){
        visit(node.condition);
        if(node.thenStmt != null){
            visit(node.thenStmt);
        }
        if(node.elseStmt != null){
            visit(node.elseStmt);
        }
    }

    void visitBlockNode(BlockNode node){
        String blockName = currentScope.scopeName +" block" + (currentScope.scopeLevel + 1);
        log("ENTER scope: " + blockName);
        currentScope = new ScopedSymbolTable(blockName,
                currentScope.scopeLevel + 1, currentScope);
        for (AstNode eachnode : node.stmts) {
            visit(eachnode);
        }
        currentScope = currentScope.enclosingScope;
        log("LEAVE scope: " + blockName);
    }

    void visitNumNode(NumNode node) {}

    void visitVarNode(VarNode node) {
        String varName = node.value;
        Symbol symbol = currentScope.lookup(varName);
        if (symbol == null) {
            log("Error: Undefined variable '" + node.token.value + "'");
            System.exit(1);
        }else {
            node.symbol = symbol;
        }
    }

    void visitVarDeclNode(VarDeclNode node) {
        String varName = ((VarNode)node.varNode).value;
        String varType = ((Type)node.typeNode).value;
        Offset.sum += 8;
        int varOffset = Offset.sum;
        Symbol symbol = new VarSymbol(varName, varType, varOffset);
        currentScope.insert(symbol);
//        ((VarNode)node.varNode).symbol = symbol;
    }

    void visitFormalParamNode(FormalParamNode node){
        String parameterName = ((VarNode)node.parameterNode).value;
        String parameterType = ((Type)node.typeNode).value;
        Offset.sum += 8;
        int varOffset = Offset.sum;
        Symbol symbol = new ParameterSymbol(parameterName, parameterType, varOffset);
        currentScope.insert(symbol);
        node.parameterSymbol = symbol;
    }

    void visit_FunctionDef_Node(FunctionDefNode node) {
        Offset.sum = 0;
        String funcName = node.functionName;
        FunctionSymbol functionSymbol = new FunctionSymbol(funcName);
        currentScope.insert(functionSymbol);

        currentScope = new ScopedSymbolTable(funcName,
                currentScope.scopeLevel + 1, currentScope);
//        currentScope=
        for (AstNode eachnode : node.formalParameters) {
            visit(eachnode);
        }
        visit(node.blockNode);
        node.offset = Offset.sum;
        currentScope = currentScope.enclosingScope;
        functionSymbol.blockAst = node.blockNode;
    }

    void visitFunctionCallNode(FunctionCallNode node) {}

    void semanticAnalyze(ArrayList<AstNode> tree) {
        for (AstNode node : tree) {
            if (node != null){
                visit(node);
            }
        }
    }

}
