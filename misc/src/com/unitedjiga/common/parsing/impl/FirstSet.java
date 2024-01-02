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

import com.unitedjiga.common.parsing.ChoiceExpression;
import com.unitedjiga.common.parsing.PatternExpression;
import com.unitedjiga.common.parsing.Expression;
import com.unitedjiga.common.parsing.ExpressionVisitor;
import com.unitedjiga.common.parsing.QuantifierExpression;
import com.unitedjiga.common.parsing.ReferenceExpression;
import com.unitedjiga.common.parsing.SequenceExpression;

/**
 * @author Mikami Junji
 *
 */
class FirstSet implements ExpressionVisitor<Set<Expression>, Set<Expression>> {

    @Override
    public Set<Expression> visit(Expression prd) {
        return visit(prd, Set.of());
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
        if (seq.getSequence().isEmpty()) {
            return followSet;
        }
        var list = new ArrayList<>(seq.getSequence());
        Collections.reverse(list);
        var set = followSet;
        for (var p : list) {
            set = visit(p, set);
        }
        return set;
    }

    @Override
    public Set<Expression> visitPattern(PatternExpression p, Set<Expression> followSet) {
        return Set.of(p);
    }

    @Override
    public Set<Expression> visitReference(ReferenceExpression ref, Set<Expression> followSet) {
        return visit(ref.get(), followSet);
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
