package com.jiganaut.bonsai.grammar;

import java.util.Set;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;

/**
 *
 * @author Junji Mikami
 */
public interface Grammar {

    /**
     *
     */
    public static interface Builder {
        public default Grammar.Builder add(String symbol, Rule rule) {
            return add(symbol, () -> rule);
        }

        public Grammar.Builder add(String symbol, Rule.Builder builder);

        public Grammar.Builder asShortCircuit();

        public Grammar build();

    }

    public static Builder builder() {
        return GrammarProvider.load().createGrammarBuilder(null);
    }

    public static Builder builder(String startSymbol) {
        return GrammarProvider.load().createGrammarBuilder(startSymbol);
    }

    public String getStartSymbol();

    public Set<? extends ProductionRule> getProductionRules();

    public boolean isShortCircuit();

    public default ChoiceRule toChoiceRule() {
        var builder = getProductionRules().stream()
                .filter(e -> getStartSymbol() == null || getStartSymbol().equals(e.getSymbol()))
                .collect(ChoiceRule::builder,
                        ChoiceRule.Builder::add,
                        ChoiceRule.Builder::addAll);
        if (isShortCircuit()) {
            builder.asShortCircuit();
        }
        return builder.build();
    }

}
