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
package com.unitedjiga.common.parsing.grammar;

/**
 * @author Mikami Junji
 *
 */
public interface SimpleRuleVisitor<R, P> extends RuleVisitor<R, P> {

    @Override
    public default R visitChoice(ChoiceRule choice, P p) {
        return defaultAction(choice, p);
    }

    @Override
    public default R visitSequence(SequenceRule sequence, P p) {
        return defaultAction(sequence, p);
    }

    @Override
    public default R visitPattern(PatternRule pattern, P p) {
        return defaultAction(pattern, p);
    }

    @Override
    public default R visitReference(ReferenceRule reference, P p) {
        return defaultAction(reference, p);
    }

    @Override
    public default R visitQuantifier(QuantifierRule quantifier, P p) {
        return defaultAction(quantifier, p);
    }

    @Override
    public default R visitEmpty(Rule empty, P p) {
        return defaultAction(empty, p);
    }

    public R defaultAction(Rule rule, P p);
}
