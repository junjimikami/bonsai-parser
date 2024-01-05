/*
 * The MIT License
 *
 * Copyright 2020 Junji Mikami.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import com.unitedjiga.common.parsing.grammar.Expression;
import com.unitedjiga.common.parsing.grammar.ProductionSet;
import com.unitedjiga.common.parsing.grammar.SequenceExpression;

/**
 *
 * @author Junji Mikami
 */
class DefaultSequenceExpression extends AbstractExpression implements SequenceExpression {
    static class Builder extends AbstractExpression.QuantifiableBuilder implements SequenceExpression.Builder {
        private final List<Expression.Builder> builders = new ArrayList<>();

        Builder() {
        }

        @Override
        protected void checkForBuild() {
            super.checkForBuild();
            if (builders.isEmpty()) {
                throw new IllegalStateException(Message.NO_ELEMENTS.format());
            }
        }

        @Override
        public Builder add(Expression.Builder builder) {
            check(builder);
            builders.add(builder);
            return this;
        }

        @Override
        public Builder add(String reference) {
            check(reference);
            builders.add(new DefaultReferenceExpression.Builder(reference));
            return this;
        }

        @Override
        public Builder addPattern(String regex) {
            check(regex);
            builders.add(new DefaultPatternExpression.Builder(regex));
            return this;
        }

        @Override
        public Builder addPattern(Pattern pattern) {
            check(pattern);
            builders.add(new DefaultPatternExpression.Builder(pattern));
            return this;
        }

        @Override
        public SequenceExpression build(ProductionSet set) {
            checkForBuild();
            var elements = builders.stream().map(e -> e.build(set)).toList();
            return new DefaultSequenceExpression(elements);
        }

    }

    private final List<? extends Expression> elements;

    private DefaultSequenceExpression(List<Expression> elements) {
        assert elements != null;
        this.elements = Objects.requireNonNull(elements);
    }

    @Override
    public List<? extends Expression> getSequence() {
        return elements;
    }
}