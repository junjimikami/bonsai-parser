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
package com.unitedjiga.common.parsing.grammar;

/**
 * 
 * @author Junji Mikami
 */
public interface Rule {

    public static enum Kind {
        PATTERN, SEQUENCE, CHOICE, REFERENCE, QUANTIFIER, EMPTY;
    }

    @FunctionalInterface
    public static interface Builder {
        public Rule build(ProductionSet set);
    }

    public static final Rule EMPTY = new Rule() {

        @Override
        public Kind getKind() {
            return Kind.EMPTY;
        }

        @Override
        public <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
            return visitor.visitEmpty(this, p);
        }
        
        @Override
        public String toString() {
            return "empty";
        }
    };

    public <R, P> R accept(RuleVisitor<R, P> visitor, P p);
    public default <R, P> R accept(RuleVisitor<R, P> visitor) {
        return accept(visitor, null);
    }

    public Kind getKind();
}
