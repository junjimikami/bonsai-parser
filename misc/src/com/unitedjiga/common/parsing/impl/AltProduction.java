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
package com.unitedjiga.common.parsing.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.unitedjiga.common.parsing.AlternativeProduction;
import com.unitedjiga.common.parsing.Production;

/**
 * 
 * @author Junji Mikami
 *
 */
// package private
class AltProduction extends AbstractProductionStructure implements AlternativeProduction {
    static class Builder extends AbstractProduction.Builder implements AlternativeProduction.Builder {
        private final List<Supplier<Production>> elements = new ArrayList<>();

        // PP
        Builder() {
            super();
        }
        Builder(String name) {
            super(name);
        }

        @Override
        public AlternativeProduction build() {
            checkForBuild();
            var el = elements.stream()
                    .map(Supplier::get)
                    .toList();
            return name == null ? new AltProduction(el) : new AltProduction(name, el);
        }

        @Override
        public Builder add(Production p) {
            check();
            elements.add(() -> p);
            return this;
        }

        @Override
        public Builder add(Production.Builder b) {
            check();
            elements.add(b::build);
            return this;
        }

        @Override
        public Builder addEmpty() {
            check();
            elements.add(Productions::empty);
            return this;
        }
        @Override
        public Builder add(String name, Production p) {
            check();
            elements.add(() -> p.as(name));
            return this;
        }
        @Override
        public Builder add(String name, Production.Builder b) {
            check();
            elements.add(() -> b.build().as(name));
            return this;
        }
    }

    private AltProduction(List<Production> elements) {
        super(elements);
    }
    private AltProduction(String name, List<Production> elements) {
        super(name, elements);
    }

    @Override
    public AlternativeProduction as(String name) {
        return (AlternativeProduction) super.as(name);
    }
}
