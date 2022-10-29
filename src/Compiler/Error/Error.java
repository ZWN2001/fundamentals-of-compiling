package Compiler.Error;
import Compiler.ConstantAndBuffer.InputFile;

public class Error {
    public static int lineno = 1;
    public static int column = 1;
    public static void error(int lineno, int position, String message) {
        String line = InputFile.buffer.get(lineno - 1);
        System.out.print(InputFile.name+":line {"+lineno+"}  --->");
        System.out.print(line.strip());
        System.out.println(position+"s^");
        System.out.println(message);
        System.exit(1);
    }
}
