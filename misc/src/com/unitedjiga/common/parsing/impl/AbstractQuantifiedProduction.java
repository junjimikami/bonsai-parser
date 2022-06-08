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
package com.unitedjiga.common.parsing.impl;

import java.util.OptionalLong;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.QuantifiedProduction;

/**
 * @author Mikami Junji
 *
 */
class AbstractQuantifiedProduction extends AbstractEntityProduction implements QuantifiedProduction {
    static class Builder extends AbstractProduction.Builder implements QuantifiedProduction.Builder {
        private String name;
        private long from;
        private long to;
        private boolean limited;
        private Supplier<Production> p;

        @Override
        public Builder setName(String name) {
            check();
            this.name = name;
            return this;
        }

        @Override
        public Builder set(Production p) {
            check();
            this.p = () -> p;
            return this;
        }

        @Override
        public Builder set(Production.Builder b) {
            check();
            this.p = b::build;
            return this;
        }

        @Override
        public Builder atLeast(long times) {
            check();
            this.from = times;
            this.limited = false;
            return this;
        }

        @Override
        public Builder range(long from, long to) {
            check();
            this.from = from;
            this.to = to;
            this.limited = true;
            return this;
        }

        @Override
        public QuantifiedProduction build() {
            checkForBuild();
            if (limited) {
                return new AbstractQuantifiedProduction(name, from, to, p.get());
            }
            return new AbstractQuantifiedProduction(name, from, p.get());
        }
    }

    private final long lowerLimit;
    private final OptionalLong upperLimit;
    private final Production p;

    private AbstractQuantifiedProduction(String name, long lowerLimit, Production p) {
        super(name);
        this.lowerLimit = lowerLimit;
        this.upperLimit = OptionalLong.empty();
        this.p = p;
    }
    private AbstractQuantifiedProduction(String name, long lowerLimit, long upperLimit, Production p) {
        super(name);
        this.lowerLimit = lowerLimit;
        this.upperLimit = OptionalLong.of(upperLimit);
        this.p = p;
    }

    @Override
    public long getLowerLimit() {
        return lowerLimit;
    }
    @Override
    public OptionalLong getUpperLimit() {
        return upperLimit;
    }
    @Override
    public Stream<Production> stream() {
        var s = Stream.generate(() -> p);
        if (upperLimit.isPresent()) {
            return s.limit(upperLimit.getAsLong());
        }
        return s;
    }
}
