package com.jiganaut.bonsai.grammar;

import java.util.Set;

public interface ProductionSet extends Set<Production> {

    public boolean containsSymbol(String symbol);

    public ProductionSet withSymbol(String symbol);

    public boolean isShortCircuit();

    public ProductionSet shortCircuit();
}
