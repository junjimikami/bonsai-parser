package com.unitedjiga.common.parsing.grammar.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.unitedjiga.common.parsing.grammar.Expression;
import com.unitedjiga.common.parsing.grammar.Production;
import com.unitedjiga.common.parsing.grammar.ProductionSet;

class DefaultProductionSet implements ProductionSet {
    private final Map<String, Production> map = new HashMap<>();

    DefaultProductionSet(Set<String> keys) {
        keys.forEach(key -> map.put(key, null));
    }

    @Override
    public boolean contains(String symbol) {
        return map.containsKey(symbol);
    }

    @Override
    public Production get(String symbol) {
        var p = map.get(symbol);
        if (p == null) {
            throw new NoSuchElementException(Message.NO_SUCH_SYMBOL.format(symbol));
        }
        return p;
    }

    void add(String symbol, Expression expression) {
        var production = new DefaultProduction(symbol, expression);
        map.put(symbol, production);
    }
}
