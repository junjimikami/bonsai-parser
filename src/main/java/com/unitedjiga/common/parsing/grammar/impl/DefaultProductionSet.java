package com.unitedjiga.common.parsing.grammar.impl;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import com.unitedjiga.common.parsing.grammar.Expression;
import com.unitedjiga.common.parsing.grammar.Production;
import com.unitedjiga.common.parsing.grammar.ProductionSet;

class DefaultProductionSet implements ProductionSet {

    private final Set<Production> set = new HashSet<>();

    DefaultProductionSet() {
    }

    @Override
    public Production get(String symbol) {
        return set.stream()
                .filter(p -> p.getSymbol().equals(symbol))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(Message.NO_SUCH_SYMBOL.format(symbol)));
    }

    void add(String symbol, Expression expression) {
        set.add(new DefaultProduction(symbol, expression));
    }
}
