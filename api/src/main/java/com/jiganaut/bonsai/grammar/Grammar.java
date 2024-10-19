package com.jiganaut.bonsai.grammar;

import java.util.stream.Stream;

import com.jiganaut.bonsai.grammar.impl.GrammarProviders;

public interface Grammar extends ProductionSet {

    public static interface Builder extends ProductionSet.Builder {
        public Grammar.Builder add(String symbol, Rule rule);
        public Grammar.Builder add(String symbol, Rule.Builder builder);
        public Grammar.Builder setStartSymbol(String symbol);
        public Grammar build();
    }

    public static Builder builder() {
        return GrammarProviders.provider().createGrammarBuilder();
    }

    public Production getStartProduction();

    @Override
    public default Stream<Production> scope() {
        return Stream.of(getStartProduction());
    }

}
