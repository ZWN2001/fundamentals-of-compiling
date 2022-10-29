package Compiler;

import Compiler.ConstantAndBuffer.InputFile;
import Compiler.Token.Token;

import java.io.*;
import java.util.ArrayList;

public class Compiler {
    public static void main(String[] args) throws IOException {
        String fileName = "tmpc";
        String inputfilePlainText = readFile(fileName);
        Lexer lexer = new Lexer(inputfilePlainText);
        ArrayList<Token> tokens = lexer.gatherAllTokens();
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    static String readFile(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                //相当于加到缓存里，在报错的时候进行定位
                InputFile.buffer.add(line);
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        }
    }


}
