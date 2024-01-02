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
package com.unitedjiga.common.parsing;

import java.util.function.Supplier;

import com.unitedjiga.common.parsing.impl.Productions;

/**
 * @author Mikami Junji
 *
 */
public interface ProductionFactory {

    public static ProductionFactory newFactory() {
        return Productions.newFactory();
    }

    public PatternProduction.Builder createPatternBuilder();
    public default PatternProduction createPattern(String regex) {
        return createPatternBuilder()
                .setPattern(regex)
                .build();
    }
    public default PatternProduction createPattern(String regex, int flags) {
        return createPatternBuilder()
                .setPattern(regex)
                .setFlags(flags)
                .build();
    }

    public ChoiceExpression.Builder createAlternativeBuilder();

    public SequenceExpression.Builder createSequentialBuilder();

    public Reference.Builder createReferenceBuilder();
    public default Reference createReference(Supplier<? extends Expression> supplier) {
        return createReferenceBuilder()
                .set(supplier)
                .build();
    }

    public QuantifiedProduction.Builder createQuantifiedBuilder();
    public default QuantifiedProduction createOptional(Expression p) {
        return createQuantifiedBuilder()
                .set(p)
                .range(0, 1)
                .build();
    }
    public default QuantifiedProduction createZeroOrMore(Expression p) {
        return createQuantifiedBuilder()
                .set(p)
                .atLeast(0)
                .build();
    }
}
