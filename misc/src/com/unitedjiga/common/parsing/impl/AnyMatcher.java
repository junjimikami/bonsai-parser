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
import com.unitedjiga.common.parsing.util.SimpleExpressionVisitor;

/**
 * @author Mikami Junji
 *
 */
class AnyMatcher implements SimpleExpressionVisitor<Boolean, Context> {
    private final FirstSet firstSet = new FirstSet();

    @Override
    public Boolean visit(Expression prd, Context args) {
        if (prd == Interpreter.EOF) {
            var tokenizer = args.getTokenizer();
            return !tokenizer.hasNext();
        }
        return SimpleExpressionVisitor.super.visit(prd, args);
    }

    @Override
    public Boolean visitPattern(PatternExpression prd, Context args) {
        var tokenizer = args.getTokenizer();
        return tokenizer.hasNext(prd.getPattern());
    }

    @Override
    public Boolean defaultAction(Expression prd, Context args) {
        var followSet = args.getFollowSet();
        return firstSet.visit(prd, followSet)
                .stream()
                .anyMatch(e -> visit(e, args));
    }
}
