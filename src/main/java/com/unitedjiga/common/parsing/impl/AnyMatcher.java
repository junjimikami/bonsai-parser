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

import com.unitedjiga.common.parsing.grammar.Rule;
import com.unitedjiga.common.parsing.grammar.PatternRule;
import com.unitedjiga.common.parsing.grammar.SimpleRuleVisitor;

/**
 * @author Mikami Junji
 *
 */
final class AnyMatcher implements SimpleRuleVisitor<Boolean, Context> {
    private static final AnyMatcher instance = new AnyMatcher();

    private AnyMatcher() {
    }

    static boolean scan(Rule rule, Context context) {
        return instance.visit(rule, context);
    }

    @Override
    public Boolean visitPattern(PatternRule pattern, Context context) {
        var tokenizer = context.tokenizer();
        return tokenizer.hasNext(pattern.getPattern());
    }

    @Override
    public Boolean defaultAction(Rule rule, Context context) {
        var firstSet = FirstSet.of(rule, context.followSet());
        if (firstSet.isEmpty()) {
            return !context.tokenizer().hasNext();
        }
        return firstSet.stream().anyMatch(e -> visit(e, context));
    }
}
