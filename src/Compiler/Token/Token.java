package Compiler.Token;

public class Token {
    public String type;
    public String value;
    public int lineno;
    public int column;
    public int width;

    @Override
    public String toString() {
        return "Compiler.Token.Token{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", lineno=" + lineno +
                ", column=" + column +
                ", width=" + width +
                '}';
    }
}
