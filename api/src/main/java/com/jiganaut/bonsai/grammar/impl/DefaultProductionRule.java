package com.jiganaut.bonsai.grammar.impl;

import java.util.Objects;

import com.jiganaut.bonsai.grammar.ProductionRule;
import com.jiganaut.bonsai.grammar.Rule;

/**
 *
 * @author Junji Mikami
 */
class DefaultProductionRule<T> implements ProductionRule<T> {

    private final String symbol;
    private final Rule<T> rule;

    /**
     * @param symbol
     * @param rule
     */
    DefaultProductionRule(String symbol, Rule<T> rule) {
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
    public Rule<T> getRule() {
        return rule;
    }

    @Override
    public String toString() {
        return "%s = %s".formatted(symbol, rule);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductionRule<?> other) {
            return this.getKind() == other.getKind()
                    && this.symbol.equals(other.getSymbol())
                    && this.rule.equals(other.getRule());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), symbol, rule);
    }

}
