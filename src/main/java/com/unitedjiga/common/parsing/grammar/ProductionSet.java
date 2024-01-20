package com.unitedjiga.common.parsing.grammar;

public interface ProductionSet {

    public boolean contains(String symbol);

    public Production get(String symbol);
}
