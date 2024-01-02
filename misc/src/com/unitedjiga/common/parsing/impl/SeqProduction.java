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

import com.unitedjiga.common.parsing.grammar.Expression;
import com.unitedjiga.common.parsing.grammar.SequenceExpression;

/**
 *
 * @author Junji Mikami
 */
class SeqProduction extends AbstractEntityProduction implements SequenceExpression {
    static class Builder extends AbstractProduction.Builder implements SequenceExpression.Builder {
        private String name;
        private final List<Supplier<Expression>> elements = new ArrayList<>();

        @Override
        public Builder setName(String name) {
            check();
            this.name = name;
            return this;
        }

        @Override
        public SequenceExpression build() {
            checkForBuild();
            var el = elements.stream()
                    .map(e -> e.get())
                    .toList();
            return new SeqProduction(name, el);
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

    private final List<Expression> elements;

    private SeqProduction(String name, List<Expression> elements) {
        super(name);
        this.elements = elements;
    }

    @Override
    public List<Expression> getSequence() {
        return elements;
    }
}
