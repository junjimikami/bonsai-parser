package com.jiganaut.bonsai.grammar;

import com.jiganaut.bonsai.grammar.impl.GrammarProviders;

public interface Grammar {

    public static interface Builder {
        public Grammar.Builder add(String symbol, Rule.Builder builder);
        public Grammar.Builder setStartSymbol(String symbol);
        public Grammar build();
    }

    public static Builder builder() {
        return GrammarProviders.provider().createGrammarBuilder();
    }

    public String getStartSymbol();

    public ProductionSet productionSet();

    public Production getStartProduction();
}
