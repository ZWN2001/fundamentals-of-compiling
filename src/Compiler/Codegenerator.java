package Compiler;

import Compiler.AST.*;
import Compiler.Analyzer.NodeVisitor;
import Compiler.Analyzer.Symbol.VarSymbol;
import Compiler.ConstantAndBuffer.Constant;
import Compiler.Token.TokenType;

import java.util.ArrayList;
import java.util.Objects;

import static Compiler.Error.ParserError.error;

public class Codegenerator extends NodeVisitor {
    public int alignTo(int size, int align) {
        return (size + align - 1) / align * align;
    }

    void visitUnaryOpNode(UnaryOpNode node){
        visit(node.right);
        if(Objects.equals(node.op.type, TokenType.TK_MINUS)){
            System.out.println("neg");
        }
    }

    void visitReturnNode(ReturnNode node){
        visit(node.right);
        if(Objects.equals(node.token.type, TokenType.TK_RETURN)){
            System.out.println("neg");
        }
    }

    void visitBinaryOpNode(BinaryOpNode node){
        visit(node.left);
        System.out.println("push rax");
        visit(node.right);
        System.out.println("pop rdi");
        if(Objects.equals(node.op.type, TokenType.TK_PLUS)){
            System.out.println("  add %rdi, %rax");
        }
        else if(Objects.equals(node.op.type, TokenType.TK_MINUS)){
            System.out.println("  sub %rdi, %rax");
        }
        else if(Objects.equals(node.op.type, TokenType.TK_MUL)){
            System.out.println("  imul %rdi, %rax");
        }
        else if(Objects.equals(node.op.type, TokenType.TK_DIV)){
            System.out.println("  cqo");
            System.out.println("  idiv %rdi");
        }
        else if(Objects.equals(node.op.type, TokenType.TK_EQ)){
            System.out.println("  cmp %rdi, %rax");
            System.out.println("  sete %al");
            System.out.println("  movzb %al, %rax");
        }
        else if(Objects.equals(node.op.type, TokenType.TK_NE)){
            System.out.println("  cmp %rdi, %rax");
            System.out.println("  setne %al");
            System.out.println("  movzb %al, %rax");
        }
        else if(Objects.equals(node.op.type, TokenType.TK_LT)){
            System.out.println("  cmp %rdi, %rax");
            System.out.println("  setl %al");
            System.out.println("  movzb %al, %rax");
        }
        else if(Objects.equals(node.op.type, TokenType.TK_LE)){
            System.out.println("  cmp %rdi, %rax");
            System.out.println("  setle %al");
            System.out.println("  movzb %al, %rax");
        }
        else if(Objects.equals(node.op.type, TokenType.TK_GT)){
            System.out.println("  cmp %rdi, %rax");
            System.out.println("  setg %al");
            System.out.println("  movzb %al, %rax");
        }
        else if(Objects.equals(node.op.type, TokenType.TK_GE)){
            System.out.println("  cmp %rdi, %rax");
            System.out.println("  setge %al");
            System.out.println("  movzb %al, %rax");
        }
    }

    void visitAssignNode(AssignNode node) throws Throwable {
        if (Objects.equals(((VarNode) (node.left)).token.type, TokenType.TK_IDENT)){
            int varOffset = ((VarSymbol)((VarNode) (node.left)).symbol).varOffset;
            System.out.println("  lea "+varOffset+"{var_offset}(%rbp), %rax");
            System.out.println("  push %rax");
            visit(node.right);
            System.out.println("  pop %rdi");
            System.out.println("  mov %rax, (%rdi)");
        }else {
            throw  error("not an lvalue");
        }
    }

    void visitNumNode(NumNode node){
        System.out.println("  mov $"+node.token.value+", %rax");
    }

    void visitIfNode(IfNode node){
        Count.i++;
        visit(node.condition);
        System.out.println("  cmp $0, %rax");
        System.out.println("  je .Lelse"+Count.i);
        if(node.thenStmt != null){
            visit(node.thenStmt);
        }
        System.out.println("  jmp .Lend"+Count.i);
        System.out.println(".Lelse"+Count.i+":");
        if(node.elseStmt != null){
            visit(node.elseStmt);
        }
        System.out.println(".Lend"+Count.i+":");
    }

    void visit_Block_Node(BlockNode node){
        for (AstNode eachnode : node.stmts) {
            visit(eachnode);
        }
    }

    void visitVarNode(VarNode node){
        int varOffset = ((VarSymbol)node.symbol).varOffset;
        System.out.println("  lea "+varOffset+"(%rbp), %rax");
        System.out.println("  mov (%rax), %rax");
    }

    void visitVarDeclNode(VarDeclNode node){}

    void visitFormalParamNode(FormalParamNode node){}

    void visitFunctionCallNode(FunctionCallNode node){
        int nparams = 0;
        for (AstNode eachnode : node.actualParameterNodes) {
            visit(eachnode);
            System.out.println("  push %rax");
            nparams++;
        }
        for (int i = nparams; i > 0; i--) {
            System.out.println("  pop "+(Constant.parameter_registers[i - 1])+"(%rsp)");
        }
        System.out.println("  mov $0, %rax");
        System.out.println("  call "+node.functionName);
    }

    void visitFunctionDefNode(FunctionDefNode node){
        Offset.sum = 0;
        System.out.println("  .text");
        System.out.println("  .globl "+node.functionName);
        System.out.println(node.functionName+":");
        System.out.println("  push %rbp");
        System.out.println("  mov %rsp, %rbp");
        int stackSize = alignTo(Offset.sum, 16);
        System.out.println("  sub $"+stackSize+", %rsp");

        int i = 0;
        for (AstNode eachnode : node.formalParameters) {
            int varOffset = ((VarSymbol)((FormalParamNode)eachnode).parameterSymbol).varOffset;
//            System.out.println("  lea "+varOffset+"(%rbp), %rax");
//            System.out.println("  mov "+Constant.parameter_registers[i]+"(%rsp), %rdi");
//            System.out.println("  mov %rdi, (%rax)");
            System.out.println("  mov "+Constant.parameter_registers[i]+varOffset+"(%rbp)");
            i++;
        }

        visit(node.blockNode);
        System.out.println("."+node.functionName+".return:");
        System.out.println("  mov %rbp, %rsp");
        System.out.println("  pop %rbp");
        System.out.println("  ret");
    }

    void codeGeneerate(ArrayList<AstNode> tree){
        for (AstNode node : tree) {
            if (node != null){
                visit(node);
            }
        }
    }

}
