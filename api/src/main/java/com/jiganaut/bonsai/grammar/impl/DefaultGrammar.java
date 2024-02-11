package com.jiganaut.bonsai.grammar.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.grammar.Rule;

class DefaultGrammar implements Grammar {

    static class Builder extends BaseBuilder implements Grammar.Builder {
        private final Map<String, Rule.Builder> builders = new HashMap<>();
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
        public Builder add(String symbol, Rule.Builder builder) {
            checkForAdd(symbol, builder);
            builders.merge(symbol, builder, (b0, b1) -> new DefaultChoiceRule.Builder()
                    .add(b0)
                    .add(b1));
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
                var rule = builder.build(set);
                Objects.requireNonNull(rule, Message.NULL_BUILD_RESULT.format(symbol));
                set.add(symbol, rule);
            });
            assert set.containsSymbol(startSymbol);
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

    @Override
    public String toString() {
        return set.toString();
    }
}
