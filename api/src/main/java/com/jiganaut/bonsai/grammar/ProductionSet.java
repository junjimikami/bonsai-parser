package com.jiganaut.bonsai.grammar;

import java.util.Set;

import com.jiganaut.bonsai.grammar.impl.GrammarProviders;

public interface ProductionSet extends Set<Production> {

    public static interface Builder {
        ProductionSet.Builder add(String symbol, Rule rule);

        ProductionSet.Builder add(String symbol, Rule.Builder builder);

        ProductionSet build();
    }

    public static ProductionSet.Builder builder() {
        return GrammarProviders.provider().createProductionSetBuilder();
    }

    public boolean containsSymbol(String symbol);

    public Production getProduction(String symbol);
}
