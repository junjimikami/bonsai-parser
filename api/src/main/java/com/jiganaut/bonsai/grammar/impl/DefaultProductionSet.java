package com.jiganaut.bonsai.grammar.impl;

import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.ProductionSet;

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
    public boolean isShortCircuit() {
        return shortCircuit;
    }
}
