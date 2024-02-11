package com.jiganaut.bonsai.grammar.impl;

import java.util.NoSuchElementException;
import java.util.Objects;

import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.ProductionSet;
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
        public ReferenceRule build(ProductionSet set) {
            checkForBuild();
            Objects.requireNonNull(set, Message.NULL_PARAMETER.format());
            if (!set.containsSymbol(symbol)) {
                throw new NoSuchElementException(Message.NO_SUCH_SYMBOL.format(symbol));
            }
            return new DefaultReferenceRule(set, symbol);
        }

    }

    private final ProductionSet set;
    private final String symbol;

    private DefaultReferenceRule(ProductionSet set, String symbol) {
        assert set != null;
        assert symbol != null;
        this.set = set;
        this.symbol = symbol;
    }

    @Override
    public Production getProduction() {
        return set.get(symbol);
    }

    @Override
    public String toString() {
        return symbol;
    }
}
