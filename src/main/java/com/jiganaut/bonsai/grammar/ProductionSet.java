package com.jiganaut.bonsai.grammar;

public interface ProductionSet {

    public boolean containsSymbol(String symbol);

    public Production get(String symbol);
}
