package Compiler.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TokenType {
    public static String TK_PLUS = "+";
    public static String TK_MINUS = "-";
    public static String TK_MUL = "*";
    public static String TK_DIV = "/";
    public static String TK_NEG = "unary-";
    public static String TK_LT = "<";
    public static String TK_GT = ">";
    public static String TK_EQ = "==";
    public static String TK_NE = "!=";
    public static String TK_GE = ">=";
    public static String TK_LE = "<=";
    public static String TK_LPAREN = "(";
    public static String TK_RPAREN = ")";
    public static String TK_LBRACE = "{";
    public static String TK_RBRACE = "}";
    public static String TK_LBRACK = "[";
    public static String TK_RBRACK = "]";
    public static String TK_COMMA = ",";
    public static String TK_SEMICOLON = ";";
    // block of reserved words
    public static String TK_RETURN = "return";
    public static String TK_INT = "int";
    public static String TK_IF = "if";
    public static String TK_THEN = "then";
    public static String TK_ELSE = "else";
    // misc
    public static String TK_IDENT = "IDENT";
    public static String TK_INTEGER_CONST = "INTEGER_CONST";
    public static String TK_ASSIGN = "=";
    public static String TK_EOF = "EOF";


    public static Map<String,String> tokenMap = new HashMap<>() {{
        put("+", TK_PLUS);
        put("-", TK_MINUS);
        put("*", TK_MUL);
        put("/", TK_DIV);
        put("unary-", TK_NEG);
        put("<", TK_LT);
        put(">", TK_GT);
        put("==", TK_EQ);
        put("!=", TK_NE);
        put(">=", TK_GE);
        put("<=", TK_LE);
        put("(", TK_LPAREN);
        put(")", TK_RPAREN);
        put("{", TK_LBRACE);
        put("}", TK_RBRACE);
        put("[", TK_LBRACK);
        put("]", TK_RBRACK);
        put(",", TK_COMMA);
        put(";", TK_SEMICOLON);
        put("return", TK_RETURN);
        put("int", TK_INT);
        put("if", TK_IF);
        put("then", TK_THEN);
        put("else", TK_ELSE);
        put("IDENT", TK_IDENT);
        put("INTEGER_CONST", TK_INTEGER_CONST);
        put("=", TK_ASSIGN);
        put("EOF", TK_EOF);
    }};
}
