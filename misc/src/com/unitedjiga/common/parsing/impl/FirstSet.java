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
import java.util.List;
import java.util.Set;

import com.unitedjiga.common.parsing.AlternativeProduction;
import com.unitedjiga.common.parsing.PatternProduction;
import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.ProductionVisitor;
import com.unitedjiga.common.parsing.QuantifiedProduction;
import com.unitedjiga.common.parsing.Reference;
import com.unitedjiga.common.parsing.SequentialProduction;
import com.unitedjiga.common.parsing.TerminalProduction;
import com.unitedjiga.common.parsing.Tokenizer;

/**
 * @author Mikami Junji
 *
 */
class FirstSet implements ProductionVisitor<Set<TerminalProduction>, Set<TerminalProduction>> {

    @Override
    public Set<TerminalProduction> visit(Production prd) {
        return visit(prd, Set.of());
    }

    @Override
    public Set<TerminalProduction> visitAlternative(AlternativeProduction alt, Set<TerminalProduction> followSet) {
        var set = new HashSet<TerminalProduction>();
        for (var p : alt.getProductions()) {
            set.addAll(visit(p, followSet));
        }
        return set;
    }

    @Override
    public Set<TerminalProduction> visitSequential(SequentialProduction seq, Set<TerminalProduction> followSet) {
        return visitSequential(seq.getProductions(), followSet);
    }
    Set<TerminalProduction> visitSequential(List<Production> seq, Set<TerminalProduction> followSet) {
        var list = new ArrayList<>(seq);
        Collections.reverse(list);
        var set = followSet;
        for (var p : list) {
            set = visit(p, set);
        }
        return set;
    }

    @Override
    public Set<TerminalProduction> visitPattern(PatternProduction p, Set<TerminalProduction> followSet) {
        return Set.of(p);
    }

    @Override
    public Set<TerminalProduction> visitReference(Reference<?> ref, Set<TerminalProduction> followSet) {
        return visit(ref.get(), followSet);
    }

    @Override
    public Set<TerminalProduction> visitQuantified(QuantifiedProduction qt, Set<TerminalProduction> followSet) {
        var set = new HashSet<TerminalProduction>();
        set.addAll(visit(qt.get()));
        set.addAll(followSet);
        return set;
    }

    @Override
    public Set<TerminalProduction> visitEmpty(TerminalProduction empty, Set<TerminalProduction> followSet) {
        return followSet;
    }

    boolean anyMatch(Production prd, Tokenizer tokenizer, Set<TerminalProduction> followSet) {
        var set = visit(prd, followSet);
        if (!tokenizer.hasNext()) {
            return set.contains(Productions.empty());
        }
        var t = tokenizer.next();
        tokenizer.previous();
        return set.stream().anyMatch(p -> p.matches(t));
    }
}
