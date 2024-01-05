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

import com.unitedjiga.common.parsing.grammar.ChoiceExpression;
import com.unitedjiga.common.parsing.grammar.Expression;
import com.unitedjiga.common.parsing.grammar.ExpressionVisitor;
import com.unitedjiga.common.parsing.grammar.PatternExpression;
import com.unitedjiga.common.parsing.grammar.QuantifierExpression;
import com.unitedjiga.common.parsing.grammar.ReferenceExpression;
import com.unitedjiga.common.parsing.grammar.SequenceExpression;

/**
 * @author Mikami Junji
 *
 */
public interface SimpleExpressionVisitor<R, P> extends ExpressionVisitor<R, P> {

    @Override
    public default R visitChoice(ChoiceExpression prd, P p) {
        return defaultAction(prd, p);
    }

    @Override
    public default R visitSequence(SequenceExpression prd, P p) {
        return defaultAction(prd, p);
    }

    @Override
    public default R visitPattern(PatternExpression prd, P p) {
        return defaultAction(prd, p);
    }

    @Override
    public default R visitReference(ReferenceExpression prd, P p) {
        return defaultAction(prd, p);
    }

    @Override
    public default R visitQuantifier(QuantifierExpression prd, P p) {
        return defaultAction(prd, p);
    }

    @Override
    public default R visitEmpty(Expression prd, P p) {
        return defaultAction(prd, p);
    }

    public R defaultAction(Expression prd, P p);
}