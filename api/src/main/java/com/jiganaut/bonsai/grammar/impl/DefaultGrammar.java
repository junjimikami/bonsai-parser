package com.jiganaut.bonsai.grammar.impl;

import java.util.NoSuchElementException;
import java.util.Objects;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.impl.Message;

class DefaultGrammar extends DefaultProductionSet implements Grammar {

    static class Builder extends DefaultProductionSet.Builder implements Grammar.Builder {
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
        public Grammar build() {
            var productionSet = super.build();
            var match = productionSet.stream()
                    .map(e -> e.getSymbol())
                    .anyMatch(e -> Objects.equals(e, startSymbol));
            if (!match) {
                throw new NoSuchElementException(Message.NO_SUCH_SYMBOL.format(startSymbol));
            }
            return new DefaultGrammar(productionSet, startSymbol);
        }
    }

    private final String startSymbol;

    private DefaultGrammar(ProductionSet productionSet, String startSymbol) {
        super(productionSet);
        assert startSymbol != null;
        this.startSymbol = startSymbol;
    }

    @Override
    public Production getStartProduction() {
        return getProduction(startSymbol);
    }

}
