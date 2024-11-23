package com.jiganaut.bonsai.grammar.impl;

import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.impl.BaseBuilder;
import com.jiganaut.bonsai.impl.Message;

abstract class AbstractGrammar extends AbstractSet<Production> implements Grammar {

    static abstract class Builder extends BaseBuilder implements Grammar.Builder {
        final Set<Supplier<Production>> set = new LinkedHashSet<>();

        @Override
        public Grammar.Builder add(String symbol, Rule rule) {
            checkParameter(symbol, rule);
            set.add(() -> new DefaultProduction(symbol, rule));
            return this;
        }

        @Override
        public Grammar.Builder add(String symbol, Rule.Builder builder) {
            checkParameter(symbol, builder);
            set.add(() -> {
                var rule = Objects.requireNonNull(builder.build(), Message.NULL_PARAMETER.format());
                return new DefaultProduction(symbol, rule);
            });
            return this;
        }

        @Override
        protected void checkForBuild() {
            if (set.isEmpty()) {
                throw new IllegalStateException(Message.NO_ELELEMNTS.format());
            }
            super.checkForBuild();
        }

    }

    private final Set<Production> productionSet;

    AbstractGrammar(Set<Production> productionSet) {
        assert productionSet != null;
        this.productionSet = Collections.unmodifiableSet(productionSet);
    }

    @Override
    public boolean containsSymbol(String symbol) {
        return productionSet.stream()
                .map(e -> e.getSymbol())
                .anyMatch(e -> Objects.equals(e, symbol));
    }

    @Override
    public Production getProduction(String symbol) {
        var ps = productionSet.stream()
                .filter(e -> Objects.equals(e.getSymbol(), symbol))
                .toList();
        if (ps.isEmpty()) {
            throw new NoSuchElementException(Message.NO_SUCH_SYMBOL.format(symbol));
        }
        if (ps.size() == 1) {
            return ps.get(0);
        }
        var builder = ChoiceRule.builder();
        ps.forEach(e -> builder.add(e.getRule()));
        return new DefaultProduction(symbol, builder.build());
    }

    @Override
    public Iterator<Production> iterator() {
        return productionSet.iterator();
    }

    @Override
    public int size() {
        return productionSet.size();
    }

    @Override
    public String toString() {
        return productionSet.toString();
    }
}
