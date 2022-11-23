package Compiler.Analyzer;

import Compiler.Analyzer.Symbol.Symbol;

import java.util.HashMap;
import java.util.Map;

public class ScopedSymbolTable {
    public String scopeName;
    public int scopeLevel;
    public ScopedSymbolTable enclosingScope;
    private static final Map<String, Symbol> symbols = new HashMap<>();

    public ScopedSymbolTable(String scopeName, int scopeLevel, ScopedSymbolTable enclosingScope) {
        this.scopeName = scopeName;
        this.scopeLevel = scopeLevel;
        this.enclosingScope = enclosingScope;
    }

    @Override
    public String toString() {
        return "ScopedSymbolTable{" +
                "name='" + scopeName + '\'' +
                ", scopeLevel=" + scopeLevel +
                ", enclosingScope=" + enclosingScope +
                '}';
    }

    public void insert(Symbol symbol) {
        symbols.put(symbol.name, symbol);
    }

    public Symbol lookup(String name) {
        Symbol symbol =  symbols.get(name);
        if (symbol != null ) {
            return symbol;
        }
        if (enclosingScope != null) {
            return enclosingScope.lookup(name);
        }
        return null;
    }

    Symbol lookup(String name,boolean currentScopeOnly) {
        Symbol symbol =  symbols.get(name);
        if (symbol != null ) {
            return symbol;
        }
        if(currentScopeOnly){
            return null;
        }
        if (enclosingScope != null) {
            return enclosingScope.lookup(name);
        }
        return null;
    }
}
