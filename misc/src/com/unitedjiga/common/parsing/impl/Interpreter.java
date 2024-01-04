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
import java.util.LinkedList;
import java.util.List;

import com.unitedjiga.common.parsing.ParsingException;
import com.unitedjiga.common.parsing.Tree;
import com.unitedjiga.common.parsing.grammar.ChoiceExpression;
import com.unitedjiga.common.parsing.grammar.Expression;
import com.unitedjiga.common.parsing.grammar.ExpressionVisitor;
import com.unitedjiga.common.parsing.grammar.PatternExpression;
import com.unitedjiga.common.parsing.grammar.Production;
import com.unitedjiga.common.parsing.grammar.QuantifierExpression;
import com.unitedjiga.common.parsing.grammar.ReferenceExpression;
import com.unitedjiga.common.parsing.grammar.SequenceExpression;

/**
 * @author Mikami Junji
 *
 */
class Interpreter implements ExpressionVisitor<List<Tree>, Context> {
    static final Expression EOF = new Expression() {

        @Override
        public <R, P> R accept(ExpressionVisitor<R, P> visitor, P p) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Kind getKind() {
            throw new UnsupportedOperationException();
        }
    };

    private final FirstSet firstSet = new FirstSet();
    private final AnyMatcher anyMatcher = new AnyMatcher();

//    Tree parse(Expression prd, Tokenizer t) {
//        var s = interpret(prd, t);
//        if (t.hasNext()) {
//            throw new ParsingException();
//        }
//        return s;
//    }
    Tree interpret(Production production, Context context) {
        var trees = visit(production.getExpression(), context);
        return new DefaultNonTerminal(production.getSymbol(), trees);
    }
//    Tree interpret(Expression prd, Tokenizer t) {
//        var followSet = Set.of(EOF);
//        return interpret(prd, t, followSet);
//    }
//    Tree interpret(Expression prd, Tokenizer tokenizer, Set<Expression> followSet) {
//        var p = new Context(tokenizer, followSet);
//        return visit(prd, p);
//    }

    @Override
    public List<Tree> visitChoice(ChoiceExpression alt, Context args) {
        var list = alt.getChoices().stream()
                .filter(e -> anyMatcher.visit(e, args))
                .toList();
        if (list.size() != 1) {
            throw new ParsingException();
        }
        return visit(list.get(0), args);
    }

    @Override
    public List<Tree> visitSequence(SequenceExpression seq, Context args) {
        var tokenizer = args.getTokenizer();
        var followSet = args.getFollowSet();
        if (!anyMatcher.visit(seq, args)) {
            throw new ParsingException();
        }
        var list = new ArrayList<Tree>();
        var subSequence = new LinkedList<>(seq.getSequence());
        while (!subSequence.isEmpty()) {
            var expression = subSequence.remove();
            followSet = firstSet.visit(subSequence, followSet);
            var context2 = new Context(tokenizer, followSet);
            list.addAll(visit(expression, context2));
        }
        return list;
    }

    @Override
    public List<Tree> visitPattern(PatternExpression pattern, Context args) {
        if (!anyMatcher.visit(pattern, args)) {
            throw new ParsingException();
        }
        var tokenizer = args.getTokenizer();
        var token = tokenizer.next(pattern.getPattern());
        return List.of(token);
    }

    @Override
    public List<Tree> visitReference(ReferenceExpression reference, Context args) {
        var tree = interpret(reference.get(), args);
        return List.of(tree);
    }

    @Override
    public List<Tree> visitQuantifier(QuantifierExpression quantfier, Context args) {
        var list = new ArrayList<Tree>();
        var count = quantfier.stream().takeWhile(e -> {
            if (anyMatcher.visit(e, args)) {
                list.addAll(visit(e, args));
                return true;
            }
            return false;
        }).count();
        if (count < quantfier.getLowerLimit()
                || quantfier.getUpperLimit().orElse(Integer.MAX_VALUE) < count
                || Integer.MAX_VALUE < count) {
            throw new ParsingException();
        }
        return list;
    }

    @Override
    public List<Tree> visitEmpty(Expression empty, Context args) {
        if (!anyMatcher.visit(empty, args)) {
            throw new ParsingException();
        }
        return List.of();
    }
}
