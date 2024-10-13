package com.jiganaut.bonsai.grammar.impl;

import java.util.NoSuchElementException;
import java.util.Objects;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.grammar.Rule;

class DefaultGrammar implements Grammar {

    static class Builder extends BaseBuilder implements Grammar.Builder {
        private final ProductionSet.Builder psBuilder = ProductionSet.builder();
        private String startSymbol;

        Builder() {
        }

        @Override
        public Builder add(String symbol, Rule rule) {
            checkParameter(symbol);
            checkParameter(rule);
            psBuilder.add(symbol, rule);
            if (startSymbol == null) {
                startSymbol = symbol;
            }
            return this;
        }

        @Override
        public Builder add(String symbol, Rule.Builder builder) {
            checkParameter(symbol);
            checkParameter(builder);
            psBuilder.add(symbol, builder);
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
        public Grammar build() {
            checkForBuild();
            var productionSet = psBuilder.build();
            var match = productionSet.stream()
                    .map(e -> e.getSymbol())
                    .anyMatch(e -> Objects.equals(e, startSymbol));
            if (!match) {
                throw new NoSuchElementException(Message.NO_SUCH_SYMBOL.format(startSymbol));
            }
            return new DefaultGrammar(productionSet, startSymbol);
        }
    }

    private final ProductionSet productionSet;
    private final String startSymbol;

    private DefaultGrammar(ProductionSet productionSet, String startSymbol) {
        assert productionSet != null;
        assert startSymbol != null;
        this.productionSet = productionSet;
        this.startSymbol = startSymbol;
    }

    @Override
    public String getStartSymbol() {
        return startSymbol;
    }

    @Override
    public ProductionSet productionSet() {
        return productionSet;
    }

    @Override
    public Production getStartProduction() {
        return productionSet.get(startSymbol);
    }

    @Override
    public String toString() {
        return productionSet.toString();
    }
}
