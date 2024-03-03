package com.jiganaut.bonsai.grammar;

public interface ProductionSet extends Iterable<Production>{

    public boolean containsSymbol(String symbol);

    public Production get(String symbol);
}
