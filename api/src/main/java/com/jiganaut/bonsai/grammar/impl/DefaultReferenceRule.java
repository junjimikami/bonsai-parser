package com.jiganaut.bonsai.grammar.impl;

import java.util.Objects;

import com.jiganaut.bonsai.grammar.ReferenceRule;

/**
 * @author Junji Mikami
 *
 */
class DefaultReferenceRule<T> implements ReferenceRule<T> {

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
        if (obj instanceof ReferenceRule<?> other) {
            return this.getKind() == other.getKind()
                    && this.symbol.equals(other.getSymbol());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), symbol);
    }

}
