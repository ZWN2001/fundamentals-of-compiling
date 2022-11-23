package Compiler.Analyzer;

import Compiler.*;
import Compiler.AST.AstNode;

public class NodeVisitor {
    String methedName;
    Codegenerator codegenerator;
    SemanticAnalyzer semanticAnalyzer;;

    protected void visit(AstNode node, int kind) throws Exception {
        methedName = getNameByNode(node);
        if (kind == 0) {
            semanticAnalyzer = new SemanticAnalyzer();;
            try{
                semanticAnalyzer.getClass().getMethod(methedName, node.getClass()).invoke(semanticAnalyzer, node);
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("No " + methedName + " method found in SemanticAnalyzer");
            }
        }else if(kind == 1){
            codegenerator = new Codegenerator();
            try{
                codegenerator.getClass().getMethod(methedName, node.getClass()).invoke(codegenerator, node);
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("No " + methedName + " method found in codegenerator");
            }
        }
    }


    String getNameByNode(AstNode node) {
        return  "visit" + node.getClass().getSimpleName();
    }
}
