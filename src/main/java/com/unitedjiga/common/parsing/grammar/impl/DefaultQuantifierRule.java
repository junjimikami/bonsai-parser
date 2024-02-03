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

import com.unitedjiga.common.parsing.grammar.Rule;
import com.unitedjiga.common.parsing.grammar.ProductionSet;
import com.unitedjiga.common.parsing.grammar.QuantifierRule;

/**
 * @author Mikami Junji
 *
 */
class DefaultQuantifierRule extends AbstractRule implements QuantifierRule {
    static class Builder extends AbstractRule.Builder implements QuantifierRule.Builder {
        private final Rule.Builder builder;
        private final int from;
        private final Integer to;

        Builder(Rule.Builder builder, int from) {
            Objects.requireNonNull(builder);
            this.builder = builder;
            this.from = from;
            this.to = null;
        }

        Builder(Rule.Builder builder, int from, int to) {
            Objects.requireNonNull(builder);
            this.builder = builder;
            this.from = from;
            this.to = to;
        }

        @Override
        public QuantifierRule build(ProductionSet set) {
            checkForBuild();
            return new DefaultQuantifierRule(builder.build(set), from, to);
        }
    }

    private final Rule rule;
    private final int minCount;
    private final OptionalInt maxCount;

    private DefaultQuantifierRule(Rule rule, int minCount, Integer maxCount) {
        assert rule != null;
        this.rule = rule;
        this.minCount = minCount;
        if (maxCount == null) {
            this.maxCount = OptionalInt.empty();
        } else {
            this.maxCount = OptionalInt.of(maxCount);
        }
    }

    @Override
    public int getMinCount() {
        return minCount;
    }

    @Override
    public OptionalInt getMaxCount() {
        return maxCount;
    }

    @Override
    public Stream<Rule> stream() {
        var stream = Stream.generate(() -> rule);
        if (maxCount.isPresent()) {
            return stream.limit(maxCount.getAsInt());
        }
        return stream;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(minCount);
        if (maxCount.isEmpty()) {
            sb.append("*");
        } else if (minCount != maxCount.getAsInt()) {
            sb.append("*");
            sb.append(maxCount.getAsInt());
        }
        sb.append(rule);
        return sb.toString();
    }
}
