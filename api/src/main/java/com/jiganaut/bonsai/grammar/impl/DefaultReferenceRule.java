package com.jiganaut.bonsai.grammar.impl;

import com.jiganaut.bonsai.grammar.ReferenceRule;

/**
 * @author Junji Mikami
 *
 */
class DefaultReferenceRule extends AbstractRule implements ReferenceRule, DefaultQuantifiableRule {
    private final String symbol;

    DefaultReferenceRule(String symbol) {
        assert symbol != null;
        this.symbol = symbol;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
