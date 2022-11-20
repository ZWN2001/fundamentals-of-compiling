package Compiler.Analyzer.Symbol;

public class ParameterSymbol extends Symbol{
    public String parameterName;
    public String parameterType;
    public int parameterOffset;


    public ParameterSymbol(String parameterName, String parameterType, int parameterOffset) {
        super(parameterName, parameterType);
        this.parameterName = parameterName;
        this.parameterType = parameterType;
        this.parameterOffset = parameterOffset;
    }



}
