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
final class FirstSet implements ExpressionVisitor<Set<Expression>, Set<Expression>> {
    private static final FirstSet instance = new FirstSet();

    private FirstSet() {
    }

    static Set<Expression> of(Expression expression, Set<Expression> followSet) {
        return instance.visit(expression, followSet);
    }
    static Set<Expression> of(List<? extends Expression> sequence, Set<Expression> followSet) {
        return instance.visit(sequence, followSet);
    }

    @Override
    public Set<Expression> visit(Expression prd) {
        return visit(prd, Set.of());
    }

    private Set<Expression> visit(List<? extends Expression> sequence, Set<Expression> followSet) {
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
    public Set<Expression> visitChoice(ChoiceExpression alt, Set<Expression> followSet) {
        if (alt.getChoices().isEmpty()) {
            return followSet;
        }
        var set = new HashSet<Expression>();
        for (var p : alt.getChoices()) {
            set.addAll(visit(p, followSet));
        }
        return set;
    }

    @Override
    public Set<Expression> visitSequence(SequenceExpression seq, Set<Expression> followSet) {
        return visit(seq.getSequence(), followSet);
    }

    @Override
    public Set<Expression> visitPattern(PatternExpression p, Set<Expression> followSet) {
        return Set.of(p);
    }

    @Override
    public Set<Expression> visitReference(ReferenceExpression ref, Set<Expression> followSet) {
        var production = ref.get();
        return visit(production.getExpression(), followSet);
    }

    @Override
    public Set<Expression> visitQuantifier(QuantifierExpression qt, Set<Expression> followSet) {
        var set = new HashSet<Expression>();
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
    public Set<Expression> visitEmpty(Expression empty, Set<Expression> followSet) {
        return followSet;
    }
}
