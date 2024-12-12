package com.jiganaut.bonsai.grammar.impl;

import java.util.Objects;

import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.impl.Message;

/**
 * 
 * @author Junji Mikami
 */
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
        var encoded = Message.symbolEncode(symbol);
        return "%s = %s".formatted(encoded, rule);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Production p) {
            return this.getSymbol().equals(p.getSymbol())
                    && this.getRule().equals(p.getRule());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, rule);
    }

}
