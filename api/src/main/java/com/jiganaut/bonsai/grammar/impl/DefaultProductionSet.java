package com.jiganaut.bonsai.grammar.impl;

import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.impl.Message;

class DefaultProductionSet extends AbstractSet<Production> implements ProductionSet {
    final Set<Production> productionSet;
    private final boolean shortCircuit;

    DefaultProductionSet(Set<Production> productionSet, boolean shortCircuit) {
        assert productionSet != null;
        this.productionSet = Collections.unmodifiableSet(productionSet);
        this.shortCircuit = shortCircuit;
    }

    @Override
    public int size() {
        return productionSet.size();
    }

    @Override
    public Iterator<Production> iterator() {
        return productionSet.iterator();
    }

    @Override
    public String toString() {
        return productionSet.toString();
    }

    @Override
    public boolean containsSymbol(String symbol) {
        return stream()
                .map(e -> e.getSymbol())
                .anyMatch(e -> Objects.equals(e, symbol));
    }

    @Override
    public ProductionSet withSymbol(String symbol) {
        var set = stream()
                .filter(e -> Objects.equals(e.getSymbol(), symbol))
                .collect(LinkedHashSet<Production>::new, Set::add, Set::addAll);
        if (set.isEmpty()) {
            throw new NoSuchElementException(Message.SYMBOL_NOT_FOUND.format(symbol));
        }
        return new DefaultProductionSet(set, isShortCircuit());
    }

    @Override
    public boolean isShortCircuit() {
        return shortCircuit;
    }

    @Override
    public ProductionSet shortCircuit() {
        return new DefaultProductionSet(productionSet, true);
    }

}
