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
import com.unitedjiga.common.parsing.Expression;

/**
 * 
 * @author Junji Mikami
 *
 */
// package private
class AltProduction extends AbstractEntityProduction implements AlternativeProduction {
    static class Builder extends AbstractProduction.Builder implements AlternativeProduction.Builder {
        private String name;
        private final List<Supplier<Expression>> elements = new ArrayList<>();

        @Override
        public Builder setName(String name) {
            check();
            this.name = name;
            return this;
        }

        @Override
        public AlternativeProduction build() {
            checkForBuild();
            var el = elements.stream()
                    .map(Supplier::get)
                    .toList();
            return new AltProduction(name, el);
        }

        @Override
        public Builder add(Expression p) {
            check();
            elements.add(() -> p);
            return this;
        }

        @Override
        public Builder add(Expression.Builder b) {
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

    }

    private final List<? extends Expression> elements;

    AltProduction(String name, List<? extends Expression> elements) {
        super(name);
        this.elements = elements;
    }

    @Override
    public List<? extends Expression> getProductions() {
        return elements;
    }
}
