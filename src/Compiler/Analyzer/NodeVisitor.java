package Compiler.Analyzer;

import Compiler.AST.AstNode;

public class NodeVisitor {
    String methedName;

    protected void visit(AstNode node) {
        methedName = "visit_" + node.getClass().getSimpleName();
    }

    String getByName(String name) throws Exception {
        if (!name.equals(methedName)) {
            throw new Exception("No visit_" + name + " method found");
        }else {
            return methedName;
        }
    }

    String getByNode(AstNode node) throws Exception {
        return getByName( "visit_" + node.getClass().getSimpleName());
    }
}
