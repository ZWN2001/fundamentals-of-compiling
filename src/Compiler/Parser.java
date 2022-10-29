package Compiler;

import Compiler.AST.*;
import Compiler.Error.Error;
import Compiler.Error.ErrorCode;
import Compiler.Error.ParserError;
import Compiler.Token.Token;
import Compiler.Token.TokenType;

import java.util.ArrayList;
import java.util.Objects;

public class Parser {
    Lexer lexer;
    Token currentToken;
    String currentFunctionName;

    public Token getNextToken(){
        return lexer.getNextToken();
    }

    void error(String errorCode, Token token) throws Throwable {
        String message = "Error: "+errorCode+" at line "+token.lineno+" column "+token.column;
        throw Objects.requireNonNull(ParserError.error(message));
    }

    void eat(String tokenType) throws Throwable {
        if (currentToken.type.equals(tokenType)){
            currentToken = getNextToken();
        }else {
            error("Syntax error", currentToken);
        }
    }

    // # primary = "(" expr ")" | identifier args? | num
    // # args = "(" (assign ("," assign)*)? ")"
    AstNode primary() throws Throwable {
        Token token = currentToken;
        //# 括号匹配
        if (Objects.equals(token.type, TokenType.TK_LPAREN)) {
            eat(TokenType.TK_LPAREN);
            AstNode node = expression();
            eat(TokenType.TK_RPAREN);
            return node;
        }

        //标识符
        if (Objects.equals(token.type, TokenType.TK_IDENT)) {
            eat(TokenType.TK_IDENT);
            if (Objects.equals(currentToken.type, TokenType.TK_LPAREN)) {
                String function_name = token.value;
                eat(TokenType.TK_LPAREN);
                ArrayList<AstNode> actualParameterNodes = new ArrayList<>();
                if (!Objects.equals(currentToken.type, TokenType.TK_RPAREN)) {
                    actualParameterNodes.add(assign());
                }
                while (Objects.equals(currentToken.type, TokenType.TK_COMMA)) {
                    eat(TokenType.TK_COMMA);
                    actualParameterNodes.add(assign());
                }
                eat(TokenType.TK_RPAREN);
                return new FunctionCallNode(function_name, actualParameterNodes, token);
            }
            return new VarNode(token);
        }

        if(Objects.equals(token.type, TokenType.TK_INTEGER_CONST)){
            eat(TokenType.TK_INTEGER_CONST);
            return new NumNode(token);
        }

        return null;
    }

    // expression = assign
    AstNode expression() throws Throwable {
        return assign();
    }

    //assign = equality ("=" assign)?
    AstNode assign() throws Throwable {
        AstNode node = equality();
        Token token = currentToken;
        if (Objects.equals(token.type, TokenType.TK_ASSIGN)) {
            eat(TokenType.TK_ASSIGN);
            node = new AssignNode(node, assign(), token);
        }
        return node;
    }

    // equality = relational ("==" relational | "! =" relational)*
    AstNode equality() throws Throwable {
        AstNode node = relational();
        Token token;
        while(true){
            token = currentToken;
            if (Objects.equals(token.type, TokenType.TK_EQ)){
                eat(TokenType.TK_EQ);
                node = new BinaryOpNode(node, relational(), token);
            }else if (Objects.equals(token.type, TokenType.TK_NE)){
                eat(TokenType.TK_NE);
                node = new BinaryOpNode(node, relational(), token);
            }else {
                return node;
            }
        }
    }

    //  relational = add_sub ("<" add_sub | "<=" add_sub | ">" add_sub | ">=" add_sub)*
    AstNode relational() throws Throwable {
        AstNode node = addSub();
        Token token;
        while(true){
            token = currentToken;
            if (Objects.equals(token.type, TokenType.TK_LT)){
                eat(TokenType.TK_LT);
                node = new BinaryOpNode(node, addSub(), token);
            }else if (Objects.equals(token.type, TokenType.TK_GT)){
                eat(TokenType.TK_GT);
                node = new BinaryOpNode(node, addSub(), token);
            }else if (Objects.equals(token.type, TokenType.TK_LE)){
                eat(TokenType.TK_LE);
                node = new BinaryOpNode(node, addSub(), token);
            }else if (Objects.equals(token.type, TokenType.TK_GE)){
                eat(TokenType.TK_GE);
                node = new BinaryOpNode(node, addSub(), token);
            }else {
                return node;
            }
        }
    }

    // add-sub = mul_div ("+" mul_div | "-" mul_div)*
    AstNode addSub() throws Throwable {
        AstNode node = mulDiv();
        Token token;
        while (true) {
            token = currentToken;
            if (Objects.equals(token.type, TokenType.TK_PLUS)) {
                eat(TokenType.TK_PLUS);
                node = new BinaryOpNode(node, mulDiv(), token);
                continue;
            } else if (Objects.equals(token.type, TokenType.TK_MINUS)) {
                eat(TokenType.TK_MINUS);
                node = new BinaryOpNode(node, mulDiv(), token);
                continue;
            }
            return node;
        }
    }

    // mul_div = unary ("*" unary | "/" unary)*
    AstNode mulDiv() throws Throwable {
        AstNode node = unary();
        Token token;
        while (true) {
            token = currentToken;
            if (Objects.equals(token.type, TokenType.TK_MUL)) {
                eat(TokenType.TK_MUL);
                node = new BinaryOpNode(node, unary(), token);
                continue;
            }else if (Objects.equals(token.type, TokenType.TK_DIV)){
                eat(TokenType.TK_DIV);
                node = new BinaryOpNode(node, unary(), token);
                continue;
            }
            return node;
        }
    }

    //  # unary = ("+" | "-") unary
    //  #        | primary
    AstNode unary() throws Throwable {
        Token token = currentToken;
        if (Objects.equals(token.type, TokenType.TK_PLUS)){
            eat(TokenType.TK_PLUS);
            return new UnaryOpNode(token,unary());
        }else if (Objects.equals(token.type, TokenType.TK_MINUS)){
            eat(TokenType.TK_MINUS);
            return new UnaryOpNode(token,unary());
        }else {
            return primary();
        }
    }

    // expression-statement = expression? ";"
    AstNode expressionStatement() throws Throwable {
        AstNode node = null;
        Token token = currentToken;
        if(Objects.equals(token.type, TokenType.TK_SEMICOLON)){
            eat(TokenType.TK_SEMICOLON);
        }else {
            node = expression();
            if (Objects.equals(currentToken.type, TokenType.TK_SEMICOLON)){
                eat(TokenType.TK_SEMICOLON);
            }else {
                Error.error(token.lineno, token.column - token.width + 1, "expect \";\"");
            }

        }

        return node;
    }

    //    # statement = expression-statement
    //    #             | "return" expression-statement
    //    #             | block
    //    #             | "if" "(" expression ")" statement ("else" statement)?
    AstNode statement() throws Throwable {
        Token token = currentToken;
        if (Objects.equals(token.type, TokenType.TK_RETURN)) {
            eat(TokenType.TK_RETURN);
            return new ReturnNode(token, expressionStatement(), currentFunctionName);
        } else if (Objects.equals(token.type, TokenType.TK_LBRACE)) {
            return block();
        } else if (Objects.equals(token.type, TokenType.TK_IF)) {
            AstNode condition = null;
            AstNode thenStatement = null;
            AstNode elseStatement = null;
            eat(TokenType.TK_IF);
            if (Objects.equals(currentToken.type, TokenType.TK_LPAREN)) {
                eat(TokenType.TK_LPAREN);
                condition = expression();
                eat(TokenType.TK_RPAREN);
                if (Objects.equals(currentToken.type, TokenType.TK_THEN)) {
                    eat(TokenType.TK_THEN);
                    thenStatement = statement();
                    if (Objects.equals(currentToken.type, TokenType.TK_ELSE)) {
                        eat(TokenType.TK_ELSE);
                        elseStatement = statement();
                    }
                }
//                else {
//                    Error.error(token.lineno, token.column - token.width + 1, "expect \")\"");
//                }
            }
            return new IfNode(condition, thenStatement, elseStatement);
        }else {
            return expressionStatement();
        }
    }

    // type_specification = int
    AstNode typeSpecification() throws Throwable {
        Token token = currentToken;
        if (Objects.equals(token.type, TokenType.TK_INT)){
            eat(TokenType.TK_INT);
            return new Type(token);
        }else {
            Error.error(token.lineno, token.column - token.width + 1, "expect type");
            return null;
        }
    }

    // variable_declaration = type_specification (indentifier ("=" expr)? ("," indentifier ("=" expr)?)*)? ";"
    ArrayList<AstNode> variableDeclaration() throws Throwable {
        AstNode typeNode = typeSpecification();
        ArrayList<AstNode> variableNodes = new ArrayList<>();
        while (!Objects.equals(currentToken.type, TokenType.TK_SEMICOLON)){
            if (Objects.equals(currentToken.type, TokenType.TK_IDENT)){
                AstNode varNode = new VarNode(currentToken);
                AstNode node = new VarDeclNode(typeNode, varNode);
                eat(TokenType.TK_IDENT);
                variableNodes.add(node);
                if(Objects.equals(currentToken.type, TokenType.TK_COMMA)){
                    eat(TokenType.TK_COMMA);
                }
            }
        }
        eat(TokenType.TK_SEMICOLON);
        return variableNodes;
    }

    // compound_statement = (variable_declaration | statement)*
    ArrayList<AstNode> compoundStatement() throws Throwable {
        ArrayList<AstNode> statementNodes = new ArrayList<>();
        while (!Objects.equals(currentToken.type, TokenType.TK_RBRACE) && !Objects.equals(currentToken.type, TokenType.TK_EOF)) {
            if(Objects.equals(currentToken.type, TokenType.TK_INT)){
                ArrayList<AstNode> variableNodes = variableDeclaration();
                statementNodes.addAll(variableNodes);
            }else {
                AstNode node = statement();
                if (node != null) {
                    statementNodes.add(node);
                }
            }
        }
        return statementNodes;
    }

    // block = "{" compound_statement "}"
    AstNode block() throws Throwable {
        if (Objects.equals(currentToken.type, TokenType.TK_LBRACE)){
            Token ltoken = currentToken;
            eat(TokenType.TK_LBRACE);
            ArrayList<AstNode> nodes = compoundStatement();
            Token rtoken = currentToken;
            eat(TokenType.TK_RBRACE);
            return new BlockNode(ltoken, rtoken, nodes);
        }
        return null;
    }

    // formal_parameter = type_specification identifier
    AstNode formalParameter() throws Throwable {
        AstNode typeNode = typeSpecification();
        AstNode varNode = new VarNode(currentToken);
        eat(TokenType.TK_IDENT);
        return new VarDeclNode(typeNode, varNode);
    }

    // formal_parameters = formal_parameter (, formal_parameter)*
    ArrayList<AstNode> formalParameters() throws Throwable {
        ArrayList<AstNode> parameterNodes = new ArrayList<>();
        parameterNodes.add(formalParameter());
        while (!Objects.equals(currentToken.type, TokenType.TK_RPAREN)){
            eat(TokenType.TK_COMMA);
            parameterNodes.add(formalParameter());
        }
        return parameterNodes;
    }

    // function_definition= type_specification identifier "(" formal_parameters? ")" block
    AstNode functionDefinition() throws Throwable {
        Token token = currentToken;
        AstNode typeNode = typeSpecification();
        String functionName = currentToken.value;
        eat(TokenType.TK_IDENT);
        ArrayList<AstNode> formalParams = new ArrayList<>();
        if(Objects.equals(currentToken.type, TokenType.TK_LPAREN)){
            eat(TokenType.TK_LPAREN);
            formalParams = formalParameters();
            if (Objects.equals(currentToken.type, TokenType.TK_RPAREN)) {
                formalParams = formalParameters();
            }
            eat(TokenType.TK_RPAREN);
        }
        currentFunctionName = functionName;
        AstNode blockNode = null;
        if (Objects.equals(currentToken.type, TokenType.TK_LBRACE)) {
            blockNode = block();
        }else {
            Error.error(token.lineno, token.column - token.width + 1, "expect \"{\"");
        }
        return new FunctionDefNode(typeNode, functionName, formalParams, blockNode);
    }

    public ArrayList<AstNode> parse() throws Throwable {
        ArrayList<AstNode> functionDefinitionNodes = new ArrayList<>();
        while (!Objects.equals(currentToken.type, TokenType.TK_EOF)){
            AstNode node = functionDefinition();
            functionDefinitionNodes.add(node);
        }
        if(!Objects.equals(currentToken.type, TokenType.TK_EOF)){
            error(ErrorCode.UNEXPECTED_TOKEN,currentToken);
        }
        return functionDefinitionNodes;
    }

}
