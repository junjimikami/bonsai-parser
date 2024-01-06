package com.unitedjiga.common.parsing.grammar.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.unitedjiga.common.parsing.grammar.Expression;
import com.unitedjiga.common.parsing.grammar.Grammar;
import com.unitedjiga.common.parsing.grammar.ProductionSet;

class DefaultGrammar implements Grammar {

    static class Builder extends BuilderSupport implements Grammar.Builder {
        private final Map<String, Expression.Builder> builders = new HashMap<>();
        private Pattern skipPattern;
        private String startSymbol;

        Builder() {
        }

        @Override
        protected void check(String symbol, Object o) {
            super.check(symbol, o);
            if (startSymbol == null) {
                startSymbol = symbol;
            }
        }

        @Override
        protected void checkForBuild() {
            super.checkForBuild();
            if (startSymbol == null || builders.isEmpty()) {
                throw new IllegalStateException(Message.NO_ELEMENTS.format());
            }
        }

        @Override
        public Builder add(String symbol, Expression.Builder builder) {
            check(symbol, builder);
            builders.put(symbol, builder);
            return this;
        }

        @Override
        public Builder add(String symbol, String reference) {
            check(symbol, reference);
            builders.put(symbol, new DefaultReferenceExpression.Builder(reference));
            return this;
        }

        @Override
        public Builder addPattern(String symbol, String regex) {
            check(symbol, regex);
            builders.put(symbol, new DefaultPatternExpression.Builder(regex));
            return this;
        }

        @Override
        public Builder addPattern(String symbol, Pattern pattern) {
            check(symbol, pattern);
            builders.put(symbol, new DefaultPatternExpression.Builder(pattern));
            return this;
        }

        @Override
        public Builder setSkipPattern(String regex) {
            check(regex);
            var pattern = Pattern.compile(regex);
            return setSkipPattern(pattern);
        }

        @Override
        public Builder setSkipPattern(Pattern pattern) {
            check(pattern);
            this.skipPattern = pattern;
            return this;
        }

        @Override
        public Builder setStartSymbol(String symbol) {
            check(symbol);
            this.startSymbol = symbol;
            return this;
        }

        @Override
        public Grammar build() {
            checkForBuild();
            var set = new DefaultProductionSet();
            builders.forEach((symbol,builder) -> {
                var expression = builder.build(set);
                set.add(symbol, expression);
            });
            return new DefaultGrammar(set, startSymbol, skipPattern);
        }
    }

    private ProductionSet set;
    private String startSymbol;
    private Pattern skipPattern;

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
}
