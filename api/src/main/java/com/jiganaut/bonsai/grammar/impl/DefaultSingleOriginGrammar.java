package com.jiganaut.bonsai.grammar.impl;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SingleOriginGrammar;
import com.jiganaut.bonsai.impl.Message;

/**
 * 
 * @author Junji Mikami
 */
class DefaultSingleOriginGrammar extends AbstractGrammar implements SingleOriginGrammar {

    /**
     * 
     * @author Junji Mikami
     */
    static class Builder extends AbstractGrammar.Builder implements SingleOriginGrammar.Builder {
        private String startSymbol;

        @Override
        public Builder add(String symbol, Rule rule) {
            super.add(symbol, rule);
            if (startSymbol == null) {
                startSymbol = symbol;
            }
            return this;
        }

        @Override
        public Builder add(String symbol, Rule.Builder builder) {
            super.add(symbol, builder);
            if (startSymbol == null) {
                startSymbol = symbol;
            }
            return this;
        }

        @Override
        public Builder setStartSymbol(String symbol) {
            checkParameter(symbol);
            this.startSymbol = symbol;
            return this;
        }

        @Override
        Set<Production> getProductionSet() {
            var productionSet = super.getProductionSet();
            var match = productionSet.stream()
                    .map(e -> e.getSymbol())
                    .anyMatch(e -> Objects.equals(e, startSymbol));
            if (!match) {
                throw new NoSuchElementException(Message.SYMBOL_NOT_FOUND.format(startSymbol));
            }
            return productionSet;
        }

        @Override
        public SingleOriginGrammar build() {
            checkForBuild();
            var productionSet = getProductionSet();
            return new DefaultSingleOriginGrammar(productionSet, false, startSymbol);
        }
    }

    private final String startSymbol;

    private DefaultSingleOriginGrammar(Set<Production> productionSet, boolean shortCircuit, String startSymbol) {
        super(productionSet, shortCircuit);
        assert startSymbol != null;
        this.startSymbol = startSymbol;
    }

    @Override
    public String getStartSymbol() {
        return startSymbol;
    }

    @Override
    public ProductionSet productionSet() {
        var set = stream()
                .filter(e -> Objects.equals(getStartSymbol(), e.getSymbol()))
                .collect(Collectors.toSet());
        return new DefaultProductionSet(set, isShortCircuit());
    }

    @Override
    public SingleOriginGrammar shortCircuit() {
        return new DefaultSingleOriginGrammar(productionSet, true, startSymbol);
    }

}
