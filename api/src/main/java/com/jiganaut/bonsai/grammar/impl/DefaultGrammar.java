package com.jiganaut.bonsai.grammar.impl;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.ProductionRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.impl.BaseBuilder;
import com.jiganaut.bonsai.impl.Message;

/**
 *
 * @author Junji Mikami
 */
class DefaultGrammar<T> implements Grammar<T> {

    /**
     *
     */
    static class Builder<T> extends BaseBuilder implements Grammar.Builder<T> {
        private final Set<Supplier<ProductionRule<T>>> suppliers = new LinkedHashSet<>();
        private final String startSymbol;
        private boolean shortCircuit = false;

        Builder(String startSymbol) {
            this.startSymbol = startSymbol;
        }

        @Override
        public Grammar.Builder<T> add(String symbol, Rule.Builder<T> builder) {
            check();
            Objects.requireNonNull(symbol, () -> Message.VALIDATION_PARAMETER_NULL.format("symbol"));
            Objects.requireNonNull(builder, () -> Message.VALIDATION_PARAMETER_NULL.format("builder"));
            suppliers.add(() -> {
                var rule = builder.build();
                if (rule == null) {
                    return null;
                }
                return new DefaultProductionRule<>(symbol, rule);
            });
            return this;
        }

        @Override
        public Grammar.Builder<T> asShortCircuit() {
            check();
            shortCircuit = true;
            return this;
        }

        @Override
        public Grammar<T> build() {
            checkForBuild();
            var collector = shortCircuit
                    ? Collectors.toCollection(LinkedHashSet<ProductionRule<T>>::new)
                    : Collectors.<ProductionRule<T>>toSet();
            var productionSet = suppliers.stream()
                    .map(Supplier::get)
                    .filter(Objects::nonNull)
                    .collect(collector);
            ReferenceValidator.validate(productionSet);
            return new DefaultGrammar<>(productionSet, shortCircuit, startSymbol);
        }

    }

    private final Set<ProductionRule<T>> productionSet;
    private final boolean shortCircuit;
    private final String startSymbol;
    private final String delimiter;

    private DefaultGrammar(Set<ProductionRule<T>> productionSet, boolean shortCircuit, String startSymbol) {
        assert productionSet != null;
        this.productionSet = Collections.unmodifiableSet(productionSet);
        this.shortCircuit = shortCircuit;
        this.delimiter = shortCircuit ? "\n/ " : "\n| ";
        this.startSymbol = startSymbol;
    }

    @Override
    public String toString() {
        return productionSet.stream()
                .map(ProductionRule::toString)
                .collect(Collectors.joining(delimiter));
    }

    @Override
    public String getStartSymbol() {
        return startSymbol;
    }

    @Override
    public Set<? extends ProductionRule<T>> getProductionRules() {
        return productionSet;
    }

    @Override
    public boolean isShortCircuit() {
        return shortCircuit;
    }

}
