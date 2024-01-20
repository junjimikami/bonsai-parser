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

import java.util.List;

import com.unitedjiga.common.parsing.grammar.Expression;
import com.unitedjiga.common.parsing.grammar.ProductionSet;
import com.unitedjiga.common.parsing.grammar.SequenceExpression;

/**
 *
 * @author Junji Mikami
 */
class DefaultSequenceExpression extends AbstractCompositeExpression implements SequenceExpression {
    static class Builder extends AbstractCompositeExpression.Builder implements SequenceExpression.Builder {

        Builder() {
        }

        @Override
        public Builder add(Expression.Builder builder) {
            checkParameter(builder);
            builders.add(builder);
            return this;
        }

        @Override
        public Builder add(String reference) {
            checkParameter(reference);
            builders.add(new DefaultReferenceExpression.Builder(reference));
            return this;
        }

        @Override
        public SequenceExpression build(ProductionSet set) {
            checkForBuild();
            var elements = builders.stream().map(e -> e.build(set)).toList();
            return new DefaultSequenceExpression(elements);
        }

    }

    private DefaultSequenceExpression(List<Expression> elements) {
        super(elements);
    }

    @Override
    public List<? extends Expression> getSequence() {
        return elements;
    }
}
