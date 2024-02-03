package com.unitedjiga.common.parsing.grammar.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.unitedjiga.common.parsing.grammar.Rule;
import com.unitedjiga.common.parsing.grammar.Production;
import com.unitedjiga.common.parsing.grammar.ProductionSet;

class DefaultProductionSet implements ProductionSet {
    private final Map<String, Production> map = new HashMap<>();

    DefaultProductionSet(Set<String> keys) {
        keys.forEach(key -> map.put(key, null));
    }

    @Override
    public boolean containsSymbol(String symbol) {
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

    void add(String symbol, Rule rule) {
        var production = new DefaultProduction(symbol, rule);
        map.put(symbol, production);
    }

    @Override
    public String toString() {
        return map.values().toString();
    }
}
