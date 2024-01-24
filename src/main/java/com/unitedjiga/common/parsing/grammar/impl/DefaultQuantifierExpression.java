/*
 * The MIT License
 *
 * Copyright 2022 Mikami Junji.
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

import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Stream;

import com.unitedjiga.common.parsing.grammar.Expression;
import com.unitedjiga.common.parsing.grammar.ProductionSet;
import com.unitedjiga.common.parsing.grammar.QuantifierExpression;

/**
 * @author Mikami Junji
 *
 */
class DefaultQuantifierExpression extends AbstractExpression implements QuantifierExpression {
    static class Builder extends AbstractExpression.Builder implements QuantifierExpression.Builder {
        private final Expression.Builder builder;
        private final int from;
        private final Integer to;

        Builder(Expression.Builder builder, int from) {
            Objects.requireNonNull(builder);
            this.builder = builder;
            this.from = from;
            this.to = null;
        }

        Builder(Expression.Builder builder, int from, int to) {
            Objects.requireNonNull(builder);
            this.builder = builder;
            this.from = from;
            this.to = to;
        }

        @Override
        public QuantifierExpression build(ProductionSet set) {
            checkForBuild();
            return new DefaultQuantifierExpression(builder.build(set), from, to);
        }
    }

    private final Expression expression;
    private final int lowerLimit;
    private final OptionalInt upperLimit;

    private DefaultQuantifierExpression(Expression expression, int lowerLimit, Integer upperLimit) {
        assert expression != null;
        this.expression = expression;
        this.lowerLimit = lowerLimit;
        if (upperLimit == null) {
            this.upperLimit = OptionalInt.empty();
        } else {
            this.upperLimit = OptionalInt.of(upperLimit);
        }
    }

    @Override
    public int getLowerLimit() {
        return lowerLimit;
    }

    @Override
    public OptionalInt getUpperLimit() {
        return upperLimit;
    }

    @Override
    public Stream<Expression> stream() {
        var stream = Stream.generate(() -> expression);
        if (upperLimit.isPresent()) {
            return stream.limit(upperLimit.getAsInt());
        }
        return stream;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(lowerLimit);
        if (upperLimit.isEmpty()) {
            sb.append("*");
        } else if (lowerLimit != upperLimit.getAsInt()) {
            sb.append("*");
            sb.append(upperLimit.getAsInt());
        }
        sb.append(expression);
        return sb.toString();
    }
}
