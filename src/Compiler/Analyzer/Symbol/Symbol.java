package Compiler.Analyzer.Symbol;

public class Symbol {
    public String name;
    public String type;

    public Symbol(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Symbol(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
