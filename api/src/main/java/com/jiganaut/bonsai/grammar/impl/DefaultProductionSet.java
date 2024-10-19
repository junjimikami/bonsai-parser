package com.jiganaut.bonsai.grammar.impl;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.grammar.Rule;

class DefaultProductionSet extends AbstractSet<Production> implements ProductionSet {

    static class Builder extends BaseBuilder implements ProductionSet.Builder {
        private final Set<Supplier<Production>> set = new LinkedHashSet<>();

        @Override
        public ProductionSet.Builder add(String symbol, Rule rule) {
            checkParameter(symbol);
            checkParameter(rule);
            set.add(() -> new DefaultProduction(symbol, rule));
            return this;
        }

        @Override
        public ProductionSet.Builder add(String symbol, Rule.Builder builder) {
            checkParameter(symbol);
            checkParameter(builder);
            set.add(() -> {
                var rule = Objects.requireNonNull(builder.build(), Message.NULL_PARAMETER.format());
                return new DefaultProduction(symbol, rule);
            });
            return this;
        }

        @Override
        public ProductionSet build() {
            checkForBuild();
            if (set.isEmpty()) {
                throw new IllegalStateException(Message.NO_ELELEMNTS.format());
            }
            var productionSet = set.stream()
                    .map(e -> e.get())
                    .collect(() -> new LinkedHashSet<Production>(), Set::add, Set::addAll);
            ReferenceCheck.run(productionSet);
            return new DefaultProductionSet(productionSet);
        }

    }

    private final Set<Production> productionSet;

    DefaultProductionSet(Set<Production> productionSet) {
        assert productionSet != null;
        this.productionSet = productionSet;
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
