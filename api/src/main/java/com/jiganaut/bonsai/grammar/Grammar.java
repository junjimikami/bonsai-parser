package com.jiganaut.bonsai.grammar;

import java.util.Set;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;

/**
 *
 * @author Junji Mikami
 */
public interface Grammar<T> {

    /**
     *
     */
    public static interface Builder<T> {
        public default Grammar.Builder<T> add(String symbol, Rule<T> rule) {
            return add(symbol, () -> rule);
        }

        public Grammar.Builder<T> add(String symbol, Rule.Builder<T> builder);

        public Grammar.Builder<T> asShortCircuit();

        public Grammar<T> build();

    }

    public static <T> Builder<T> builder() {
        return GrammarProvider.load().createGrammarBuilder(null);
    }

    public static <T> Builder<T> builder(String startSymbol) {
        return GrammarProvider.load().createGrammarBuilder(startSymbol);
    }

    public String getStartSymbol();

    public Set<? extends ProductionRule<T>> getProductionRules();

    public boolean isShortCircuit();

    public default ChoiceRule<T> toChoiceRule() {
        var builder = getProductionRules().stream()
                .filter(e -> getStartSymbol() == null || getStartSymbol().equals(e.getSymbol()))
                .collect(ChoiceRule::<T>builder,
                        ChoiceRule.Builder::add,
                        ChoiceRule.Builder::addAll);
        if (isShortCircuit()) {
            builder.asShortCircuit();
        }
        return builder.build();
    }

}
