package Compiler;

import Compiler.AST.AstNode;
import Compiler.ConstantAndBuffer.InputFile;
import Compiler.Token.Token;

import java.io.*;
import java.util.ArrayList;

public class Compiler {
    public static void main(String[] args) throws Throwable {
        String fileName = "tmpc";
        String inputfilePlainText = readFile(fileName);
        Lexer lexer = new Lexer(inputfilePlainText);
        Parser parser = new Parser(lexer);
        ArrayList<AstNode> tree = parser.parse();

        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
        semanticAnalyzer.semanticAnalyze(tree);

        Codegenerator codegenerator = new Codegenerator();
        codegenerator.codeGeneerate(tree);

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
