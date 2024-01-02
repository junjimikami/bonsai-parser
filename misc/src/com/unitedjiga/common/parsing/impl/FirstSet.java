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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
class FirstSet implements ProductionVisitor<Set<Production>, Set<Production>> {

    @Override
    public Set<Production> visit(Production prd) {
        return visit(prd, Set.of());
    }

    @Override
    public Set<Production> visitAlternative(AlternativeProduction alt, Set<Production> followSet) {
        if (alt.getProductions().isEmpty()) {
            return followSet;
        }
        var set = new HashSet<Production>();
        for (var p : alt.getProductions()) {
            set.addAll(visit(p, followSet));
        }
        return set;
    }

    @Override
    public Set<Production> visitSequential(SequentialProduction seq, Set<Production> followSet) {
        if (seq.getProductions().isEmpty()) {
            return followSet;
        }
        var list = new ArrayList<>(seq.getProductions());
        Collections.reverse(list);
        var set = followSet;
        for (var p : list) {
            set = visit(p, set);
        }
        return set;
    }

    @Override
    public Set<Production> visitPattern(PatternProduction p, Set<Production> followSet) {
        return Set.of(p);
    }

    @Override
    public Set<Production> visitReference(Reference ref, Set<Production> followSet) {
        return visit(ref.get(), followSet);
    }

    @Override
    public Set<Production> visitQuantified(QuantifiedProduction qt, Set<Production> followSet) {
        var set = new HashSet<Production>();
        var prd = qt.stream()
                .limit(1)
                .findFirst();
        if (prd.isPresent()) {
            set.addAll(visit(prd.get()));
        }
        if (qt.getLowerLimit() == 0) {
            set.addAll(followSet);
        }
        return set;
    }

    @Override
    public Set<Production> visitEmpty(Production empty, Set<Production> followSet) {
        return followSet;
    }
}
