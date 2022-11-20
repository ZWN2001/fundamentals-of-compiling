package Compiler.Analyzer.Symbol;

public class VarSymbol extends Symbol{
    public String varName;
    public String varType;
    public int varOffset;
    public Symbol varSymbol;

    public VarSymbol(String varName, String varType, int varOffset) {
        super(varName, varType);
        this.varName = varName;
        this.varType = varType;
        this.varOffset = varOffset;
    }

    @Override
    public String toString() {
        return "VarSymbol{" +
                "varName='" + varName + '\'' +
                ", varType='" + varType + '\'' +
                ", varOffset=" + varOffset +
                ", varSymbol=" + varSymbol +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
