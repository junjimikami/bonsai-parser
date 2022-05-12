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

import java.util.OptionalInt;
import java.util.stream.Stream;

import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.QuantifiedProduction;

/**
 * @author Mikami Junji
 *
 */
abstract class AbstractQuantifiedProduction extends AbstractProduction implements QuantifiedProduction {
    abstract static class Builder extends AbstractProduction.Builder implements QuantifiedProduction.Builder {
        protected final Production.Builder builder;
        Builder(Production.Builder builder) {
            super();
            this.builder = builder;
        }
        Builder(String name, Production.Builder builder) {
            super(name);
            this.builder = builder;
        }
    }

    private final int lowerLimit;
    private final OptionalInt upperLimit;

    AbstractQuantifiedProduction(int lowerLimit) {
        super();
        this.lowerLimit = lowerLimit;
        this.upperLimit = OptionalInt.empty();
    }
    AbstractQuantifiedProduction(int lowerLimit, int upperLimit) {
        super();
        this.lowerLimit = lowerLimit;
        this.upperLimit = OptionalInt.of(upperLimit);
    }
    AbstractQuantifiedProduction(String name, int lowerLimit) {
        super(name);
        this.lowerLimit = lowerLimit;
        this.upperLimit = OptionalInt.empty();
    }
    AbstractQuantifiedProduction(String name, int lowerLimit, int upperLimit) {
        super(name);
        this.lowerLimit = lowerLimit;
        this.upperLimit = OptionalInt.of(upperLimit);
    }

    @Override
    public QuantifiedProduction as(String name) {
        return (QuantifiedProduction) super.as(name);
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
    public Stream<Production> stream() {
        var s = Stream.generate(this::get);
        upperLimit.ifPresent(l -> s.limit(l));
        return s;
    }
}
