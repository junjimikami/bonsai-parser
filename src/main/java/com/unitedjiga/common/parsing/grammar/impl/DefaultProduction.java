package com.unitedjiga.common.parsing.grammar.impl;

import com.unitedjiga.common.parsing.grammar.Production;
import com.unitedjiga.common.parsing.grammar.Rule;

class DefaultProduction implements Production {

    private final String symbol;
    private final Rule rule;

    /**
     * @param symbol
     * @param rule
     */
    DefaultProduction(String symbol, Rule rule) {
        assert symbol != null;
        assert rule != null;
        this.symbol = symbol;
        this.rule = rule;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public Rule getRule() {
        return rule;
    }

    @Override
    public String toString() {
        return "%s::=%s".formatted(symbol, rule);
    }
}
