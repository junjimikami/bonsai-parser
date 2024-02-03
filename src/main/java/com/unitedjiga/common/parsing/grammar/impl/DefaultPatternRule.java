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

import java.util.regex.Pattern;

import com.unitedjiga.common.parsing.grammar.PatternRule;
import com.unitedjiga.common.parsing.grammar.ProductionSet;

/**
 *
 * @author Junji Mikami
 */
class DefaultPatternRule extends AbstractRule implements PatternRule {
    static class Builder extends AbstractRule.QuantifiableBuilder implements PatternRule.Builder {
        private Pattern pattern;

        Builder(String regex) {
            assert regex != null;
            this.pattern = Pattern.compile(regex);
        }

        Builder(Pattern pattern) {
            assert pattern != null;
            this.pattern = pattern;
        }

        @Override
        public PatternRule build(ProductionSet set) {
            checkForBuild();
            return new DefaultPatternRule(pattern);
        }

    }

    private final Pattern pattern;

    private DefaultPatternRule(Pattern pattern) {
        assert pattern != null;
        this.pattern = pattern;
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return "\"%s\"".formatted(pattern.pattern());
    }
}
