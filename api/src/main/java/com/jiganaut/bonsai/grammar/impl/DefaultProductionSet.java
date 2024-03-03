package com.jiganaut.bonsai.grammar.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.grammar.Rule;

class DefaultProductionSet implements ProductionSet {
    private final Map<String, Production> map = new LinkedHashMap<>();

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

    @Override
    public Iterator<Production> iterator() {
        return Collections.unmodifiableCollection(map.values()).iterator();
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
