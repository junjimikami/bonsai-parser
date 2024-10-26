package com.jiganaut.bonsai.grammar.impl;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ReferenceRule r) {
            return this.getKind() == r.getKind()
                    && this.getSymbol().equals(r.getSymbol());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), symbol);
    }

}
