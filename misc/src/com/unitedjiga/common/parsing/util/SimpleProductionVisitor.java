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
package com.unitedjiga.common.parsing.util;

import com.unitedjiga.common.parsing.AlternativeProduction;
import com.unitedjiga.common.parsing.PatternProduction;
import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.ProductionVisitor;
import com.unitedjiga.common.parsing.QuantifiedProduction;
import com.unitedjiga.common.parsing.Reference;
import com.unitedjiga.common.parsing.SequentialProduction;

/**
 * @author Mikami Junji
 *
 */
@FunctionalInterface
public interface SimpleProductionVisitor<R, P> extends ProductionVisitor<R, P> {

    @Override
    public default R visitAlternative(AlternativeProduction prd, P p) {
        return defaultAction(prd, p);
    }

    @Override
    public default R visitSequential(SequentialProduction prd, P p) {
        return defaultAction(prd, p);
    }

    @Override
    public default R visitPattern(PatternProduction prd, P p) {
        return defaultAction(prd, p);
    }

    @Override
    public default R visitReference(Reference prd, P p) {
        return defaultAction(prd, p);
    }

    @Override
    public default R visitQuantified(QuantifiedProduction prd, P p) {
        return defaultAction(prd, p);
    }

    @Override
    public default R visitEmpty(Production prd, P p) {
        return defaultAction(prd, p);
    }

    public R defaultAction(Production prd, P p);
}
