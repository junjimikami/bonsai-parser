package com.jiganaut.bonsai.grammar;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;

/**
 * 
 * @author Junji Mikami
 */
public interface SingleOriginGrammar extends Grammar {

    /**
     * 
     * @author Junji Mikami
     */
    public static interface Builder extends Grammar.Builder {
        @Override
        public SingleOriginGrammar.Builder add(String symbol, Rule rule);

        @Override
        public SingleOriginGrammar.Builder add(String symbol, Rule.Builder builder);

        public SingleOriginGrammar.Builder setStartSymbol(String symbol);

        @Override
        public SingleOriginGrammar build();

        @Override
        public default SingleOriginGrammar shortCircuit() {
            return build().shortCircuit();
        }
    }

    public static Builder builder() {
        return GrammarProvider.load().createSingleOriginGrammarBuilder();
    }

    public String getStartSymbol();

    @Override
    public SingleOriginGrammar shortCircuit();

}
