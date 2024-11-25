package com.jiganaut.bonsai.grammar;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;

public interface SingleOriginGrammar extends Grammar {

    public static interface Builder extends Grammar.Builder {
        @Override
        public SingleOriginGrammar.Builder add(String symbol, Rule rule);

        @Override
        public SingleOriginGrammar.Builder add(String symbol, Rule.Builder builder);

        public SingleOriginGrammar.Builder setStartSymbol(String symbol);

        @Override
        public SingleOriginGrammar build();
    }

    public static Builder builder() {
        return GrammarProvider.load().createSingleOriginGrammarBuilder();
    }

    public String getStartSymbol();

}
