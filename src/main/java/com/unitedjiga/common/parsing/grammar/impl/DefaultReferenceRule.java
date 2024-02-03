/*
 * The MIT License
 *
 * Copyright 2021 Junji Mikami.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.unitedjiga.common.parsing.grammar.impl;

import java.util.NoSuchElementException;
import java.util.Objects;

import com.unitedjiga.common.parsing.grammar.Production;
import com.unitedjiga.common.parsing.grammar.ProductionSet;
import com.unitedjiga.common.parsing.grammar.ReferenceRule;

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
