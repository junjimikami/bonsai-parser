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

import com.unitedjiga.common.parsing.grammar.ChoiceRule;
import com.unitedjiga.common.parsing.grammar.Rule;
import com.unitedjiga.common.parsing.grammar.RuleVisitor;
import com.unitedjiga.common.parsing.grammar.PatternRule;
import com.unitedjiga.common.parsing.grammar.QuantifierRule;
import com.unitedjiga.common.parsing.grammar.ReferenceRule;
import com.unitedjiga.common.parsing.grammar.SequenceRule;

/**
 * @author Mikami Junji
 *
 */
final class FirstSet implements RuleVisitor<Set<Rule>, Set<Rule>> {
    private static final FirstSet instance = new FirstSet();

    private FirstSet() {
    }

    static Set<Rule> of(Rule rule, Set<Rule> followSet) {
        return instance.visit(rule, followSet);
    }
    static Set<Rule> of(List<? extends Rule> sequence, Set<Rule> followSet) {
        return instance.visit(sequence, followSet);
    }

    @Override
    public Set<Rule> visit(Rule prd) {
        return visit(prd, Set.of());
    }

    private Set<Rule> visit(List<? extends Rule> sequence, Set<Rule> followSet) {
        if (sequence.isEmpty()) {
            return followSet;
        }
        var list = new ArrayList<>(sequence);
        Collections.reverse(list);
        var set = followSet;
        for (var p : list) {
            set = visit(p, set);
        }
        return set;
    }

    @Override
    public Set<Rule> visitChoice(ChoiceRule alt, Set<Rule> followSet) {
        if (alt.getChoices().isEmpty()) {
            return followSet;
        }
        var set = new HashSet<Rule>();
        for (var p : alt.getChoices()) {
            set.addAll(visit(p, followSet));
        }
        return set;
    }

    @Override
    public Set<Rule> visitSequence(SequenceRule seq, Set<Rule> followSet) {
        return visit(seq.getRules(), followSet);
    }

    @Override
    public Set<Rule> visitPattern(PatternRule p, Set<Rule> followSet) {
        return Set.of(p);
    }

    @Override
    public Set<Rule> visitReference(ReferenceRule ref, Set<Rule> followSet) {
        var production = ref.getProduction();
        return visit(production.getRule(), followSet);
    }

    @Override
    public Set<Rule> visitQuantifier(QuantifierRule qt, Set<Rule> followSet) {
        var set = new HashSet<Rule>();
        var prd = qt.stream()
                .limit(1)
                .findFirst();
        if (prd.isPresent()) {
            set.addAll(visit(prd.get()));
        }
        if (qt.getMinCount() == 0) {
            set.addAll(followSet);
        }
        return set;
    }

    @Override
    public Set<Rule> visitEmpty(Rule empty, Set<Rule> followSet) {
        return followSet;
    }
}
