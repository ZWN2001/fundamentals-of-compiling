package Compiler;

import Compiler.ConstantAndBuffer.Constant;
import Compiler.Error.Error;
import Compiler.Token.Token;
import Compiler.Token.TokenType;

import java.util.ArrayList;
import java.util.Objects;

public class Lexer {
    private final String input;
    private int readPosition; // 模拟指针，int，初始时指向text的开头
    private char currentChar; // 当前指向的字符

    ArrayList<Token> tokens; // 最后生成的token表

    public Lexer(String input) {
        this.input = input;
        this.readPosition = 0;
        this.currentChar = this.input.charAt(this.readPosition);
        tokens = new ArrayList<>();
    }

    public void advance() {
        //  Advance the `pos` pointer and set the `current_char` variable.
        //  将pos指针向后移动并更新current char变量
        if (this.currentChar == '\n') { // 进入下一行
            Error.lineno += 1;
            Error.column = 0;
        }
        this.readPosition += 1; // 指向下一个字符
        if (this.readPosition > this.input.length() - 1) { // 到达input末尾
            this.currentChar = 0;
        } else {
            this.currentChar = this.input.charAt(this.readPosition); // 将current_char更新到指针pos指向的位置
            Error.column += 1; // 更新Error的行内序号，以便正确地处理错误
        }
    }

    public void skipWhitespace() {
        // 如果current_char为空或者是空格就直接跳过
        while (this.currentChar != 0 && Character.isWhitespace(this.currentChar)) {
            this.advance();
        }
    }

    // 变量名开头只能以大小写字母与下划线组成（是否是合法变量名开头）
    private boolean isIdent(char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || c == '_';
    }

    // 是否是合法变量名
    private boolean isIdent2(char c) {
        return isIdent(c) || ('0' <= c && c <= '9');
    }

    Token number() {
        // 返回从输入中消耗的(多位数)整数或浮点数的Token。
        int oldColumn = Error.column;
        StringBuilder result = new StringBuilder();
        while (this.currentChar != 0 && ('0' <= currentChar && currentChar <= '9')) {
            result.append(this.currentChar);
            this.advance();
        }
        //其实可能数字后面跟着一些其他符号的情况也是错的，但是这里不做处理
        if(('A' <= input.charAt(readPosition+1) && input.charAt(readPosition+1) <= 'Z') || ('a' <= input.charAt(readPosition+1) && input.charAt(readPosition+1) <= 'z') ){
            Error.error(Error.lineno, Error.column,"error，数字后面不能跟字母");
        }
        Token token = new Token();
        token.type = TokenType.TK_INTEGER_CONST;
        token.value = result.toString();
        token.lineno = Error.lineno;
        token.column = Error.column;
        token.width = Error.column - oldColumn;
        return token;
    }

    int readPunct(String p) {
        //    # Read a punctuator token from p and returns
        //    # 从p中读取标点符号并返回
        //    # 但其实返回的是一个分类，如果p在pos的位置是以下面几个开头就返回2（都是长度为2的标点？），
        //    # 如果current_char是一个标点符号，返回True，也就是1，否则返回False，也就是0
        if (p.startsWith("==", this.readPosition) ||
                p.startsWith("!=", this.readPosition) ||
                p.startsWith("<=", this.readPosition) ||
                p.startsWith(">=", this.readPosition)) {
            return 2;
        }
        return Constant.punctuation.contains(String.valueOf(this.currentChar)) ? 1 : 0;
    }

    Token getNextToken() {
        //     """Lexical analyzer (also known as scanner or tokenizer)
        //
        //        This method is responsible for breaking a sentence
        //        apart into tokens. One token at a time.
        //        这个方法负责将一个句子分解成tokens。一次一个token。
        //        """
        while (this.currentChar != 0) {
            if (Character.isWhitespace(this.currentChar)) {
                this.skipWhitespace();
                continue;
            }
            // 返回一个标志是数值文字的token
            if ('0' <= this.currentChar && this.currentChar <= '9') {
                return this.number();
            }

            // 读入的是标识符，对标识符进行处理
            //变量名或者是保留字
            if (this.isIdent(this.currentChar)) {
                int oldColumn = Error.column;
                StringBuilder result = new StringBuilder();
                while (this.currentChar != 0 && this.isIdent2(this.currentChar)) {
                    result.append(this.currentChar);
                    this.advance();
                }
                Token token = new Token();
                if (TokenType.tokenMap.containsKey(result.toString())) {
                    token.type = TokenType.tokenMap.get(result.toString());
                } else {
                    token.type = TokenType.TK_IDENT;
                }
                token.value = result.toString();
                token.lineno = Error.lineno; // 标识token的位置
                token.column = Error.column;
                token.width = Error.column - oldColumn; // 长度
                return token;
            }

            //  # Punctuators
            //  # two-characters punctuator
            //  # 字符长度为2的标点
            if(this.readPunct(this.input) == 2) {
                Token token = new Token();
                token.type = TokenType.tokenMap.get(this.input.substring(this.readPosition, this.readPosition + 2));
                token.value = this.input.substring(this.readPosition, this.readPosition + 2);
                token.lineno = Error.lineno;
                token.column = Error.column;
                token.width = 2;
                this.advance();
                this.advance();
                return token;
            }else if(TokenType.tokenMap.containsKey(String.valueOf(this.currentChar))) {
                Token token = new Token();
                token.type = TokenType.tokenMap.get(String.valueOf(this.currentChar));
                token.value = String.valueOf(this.currentChar);
                token.lineno = Error.lineno;
                token.column = Error.column;
                token.width = 1;
                this.advance();
                return token;
            } else {
                Error.error(Error.lineno, Error.column, "Invalid token: " + this.currentChar);
            }

        }
        Token token = new Token();
        token.type = TokenType.TK_EOF;
        return token;
    }

    ArrayList<Token> gatherAllTokens(){
        Token token = this.getNextToken();
        this.tokens.add(token);
        while (!Objects.equals(token.type, TokenType.TK_EOF)) {
            token = this.getNextToken();
            this.tokens.add(token);
        }
        return this.tokens;
    }
}
