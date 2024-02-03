/*
 * The MIT License
 *
 * Copyright 2021 Junji Mikami.
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

import java.util.regex.Pattern;

import com.unitedjiga.common.parsing.grammar.impl.GrammarProviders;

/**
 * @author Junji Mikami
 *
 */
public interface PatternRule extends Rule {

    public static interface Builder extends Rule.Builder, Quantifiable {
        @Override
        public PatternRule build(ProductionSet set);
    }

    public static Builder builder(String regex) {
        return GrammarProviders.provider().createPatternBuilder(regex);
    }

    public static Builder builder(Pattern pattern) {
        return GrammarProviders.provider().createPatternBuilder(pattern);
    }

    @Override
    public default Kind getKind() {
        return Kind.PATTERN;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
        return visitor.visitPattern(this, p);
    }

    public Pattern getPattern();
}