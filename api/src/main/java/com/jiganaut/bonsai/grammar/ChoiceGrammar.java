package com.jiganaut.bonsai.grammar;

import java.util.Set;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;

/**
 * 
 * @author Junji Mikami
 */
public interface ChoiceGrammar extends Grammar {

    /**
     * 
     * @author Junji Mikami
     */
    public static interface Builder extends Grammar.Builder {
        @Override
        public ChoiceGrammar.Builder add(String symbol, Rule rule);

        @Override
        public ChoiceGrammar.Builder add(String symbol, Rule.Builder builder);

        public ChoiceGrammar.Builder hidden();

        @Override
        public ChoiceGrammar build();

        @Override
        public default ChoiceGrammar shortCircuit() {
            return build().shortCircuit();
        }
    }

    public static Builder builder() {
        return GrammarProvider.load().createChoiceGrammarBuilder();
    }

    public Set<String> getHiddenSymbols();

    @Override
    public ChoiceGrammar shortCircuit();

}
