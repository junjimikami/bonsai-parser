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

import com.unitedjiga.common.parsing.grammar.Expression;
import com.unitedjiga.common.parsing.grammar.PatternExpression;
import com.unitedjiga.common.parsing.grammar.SimpleExpressionVisitor;

/**
 * @author Mikami Junji
 *
 */
final class AnyMatcher implements SimpleExpressionVisitor<Boolean, Context> {
    private static final AnyMatcher instance = new AnyMatcher();

    private AnyMatcher() {
    }

    static boolean scan(Expression expression, Context context) {
        return instance.visit(expression, context);
    }

    @Override
    public Boolean visitPattern(PatternExpression pattern, Context context) {
        var tokenizer = context.tokenizer();
        return tokenizer.hasNext(pattern.getPattern());
    }

    @Override
    public Boolean defaultAction(Expression expression, Context context) {
        var firstSet = FirstSet.of(expression, context.followSet());
        if (firstSet.isEmpty()) {
            return !context.tokenizer().hasNext();
        }
        return firstSet.stream().anyMatch(e -> visit(e, context));
    }
}
