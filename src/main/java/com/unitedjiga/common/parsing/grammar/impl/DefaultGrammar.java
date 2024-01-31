package com.unitedjiga.common.parsing.grammar.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Pattern;

import com.unitedjiga.common.parsing.grammar.Expression;
import com.unitedjiga.common.parsing.grammar.Grammar;
import com.unitedjiga.common.parsing.grammar.Production;
import com.unitedjiga.common.parsing.grammar.ProductionSet;

class DefaultGrammar implements Grammar {

    static class Builder extends BuilderSupport implements Grammar.Builder {
        private final Map<String, Expression.Builder> builders = new HashMap<>();
        private Pattern skipPattern;
        private String startSymbol;

        Builder() {
        }

        private void checkForAdd(String symbol, Object o) {
            super.check();
            Objects.requireNonNull(symbol, Message.NULL_PARAMETER.format());
            Objects.requireNonNull(o, Message.NULL_PARAMETER.format());
            if (startSymbol == null) {
                startSymbol = symbol;
            }
        }

        @Override
        protected void checkForBuild() {
            super.checkForBuild();
            if (builders.isEmpty()) {
                throw new IllegalStateException(Message.NO_ELELEMNTS.format());
            }
            if (!builders.containsKey(startSymbol)) {
                throw new NoSuchElementException(Message.NO_SUCH_SYMBOL.format(startSymbol));
            }
        }

        @Override
        public Builder add(String symbol, Expression.Builder builder) {
            checkForAdd(symbol, builder);
            builders.put(symbol, builder);
            return this;
        }

        @Override
        public Builder add(String symbol, String reference) {
            checkForAdd(symbol, reference);
            builders.put(symbol, new DefaultReferenceExpression.Builder(reference));
            return this;
        }

        @Override
        public Builder setSkipPattern(String regex) {
            checkParameter(regex);
            var pattern = Pattern.compile(regex);
            return setSkipPattern(pattern);
        }

        @Override
        public Builder setSkipPattern(Pattern pattern) {
            checkParameter(pattern);
            this.skipPattern = pattern;
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
            var set = new DefaultProductionSet(builders.keySet());
            builders.forEach((symbol, builder) -> {
                var expression = builder.build(set);
                set.add(symbol, expression);
            });
            assert set.contains(startSymbol);
            return new DefaultGrammar(set, startSymbol, skipPattern);
        }
    }

    private final ProductionSet set;
    private final String startSymbol;
    private final Pattern skipPattern;

    private DefaultGrammar(ProductionSet set, String startSymbol, Pattern skipPattern) {
        assert set != null;
        assert startSymbol != null;
        this.set = set;
        this.startSymbol = startSymbol;
        this.skipPattern = skipPattern;
    }

    @Override
    public String getStartSymbol() {
        return startSymbol;
    }

    @Override
    public Pattern getSkipPattern() {
        return skipPattern;
    }

    @Override
    public ProductionSet productionSet() {
        return set;
    }

    @Override
    public Production getStartProduction() {
        return set.get(startSymbol);
    }
}
