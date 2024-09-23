package com.jiganaut.bonsai.grammar.impl;

import com.jiganaut.bonsai.grammar.ReferenceRule;

/**
 * @author Junji Mikami
 *
 */
class DefaultReferenceRule extends AbstractRule implements ReferenceRule {
    static class Builder extends AbstractRule.QuantifiableBuilder implements ReferenceRule.Builder {
        private final String symbol;

        Builder(String symbol) {
            assert symbol != null;
            this.symbol = symbol;
        }

        @Override
        public ReferenceRule build() {
            checkForBuild();
            return new DefaultReferenceRule(symbol);
        }

    }

    private final String symbol;

    private DefaultReferenceRule(String symbol) {
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
