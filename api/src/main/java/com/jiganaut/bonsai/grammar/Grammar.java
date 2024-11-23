package com.jiganaut.bonsai.grammar;

import java.util.Set;
import java.util.stream.Stream;

public interface Grammar extends Set<Production> {

    public static interface Builder {
        Grammar.Builder add(String symbol, Rule rule);

        Grammar.Builder add(String symbol, Rule.Builder builder);

        Grammar build();
    }

    public boolean containsSymbol(String symbol);

    public Production getProduction(String symbol);

    public default Stream<Production> scope() {
        return stream();
    }

    public <R, P> R accept(GrammarVisitor<R, P> visitor, P p);
}
