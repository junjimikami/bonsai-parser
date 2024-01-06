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
final class Interpreter implements ExpressionVisitor<List<Tree>, Context> {
    private static final Interpreter instance = new Interpreter();

    private Interpreter() {
    }

    static Tree parse(Production production, Context context) {
        return instance.interpret(production, context);
    }

    Tree interpret(Production production, Context context) {
        var trees = visit(production.getExpression(), context);
        return new DefaultNonTerminal(production.getSymbol(), trees);
    }

    @Override
    public List<Tree> visitChoice(ChoiceExpression alt, Context args) {
        var list = alt.getChoices().stream()
                .filter(e -> AnyMatcher.scan(e, args))
                .toList();
        if (list.isEmpty()) {
            throw new ParsingException(Message.NO_MATCHING_TOKEN.format());
        }
        if (1 < list.size()) {
            throw new ParsingException(Message.AMBIGUOUS_RULE.format());
        }
        return visit(list.get(0), args);
    }

    @Override
    public List<Tree> visitSequence(SequenceExpression seq, Context args) {
        var tokenizer = args.getTokenizer();
        var followSet = args.getFollowSet();
        if (!AnyMatcher.scan(seq, args)) {
            throw new ParsingException(Message.NO_MATCHING_TOKEN.format());
        }
        var list = new ArrayList<Tree>();
        var subSequence = new LinkedList<>(seq.getSequence());
        while (!subSequence.isEmpty()) {
            var expression = subSequence.remove();
            followSet = FirstSet.of(subSequence, followSet);
            var context2 = new Context(tokenizer, followSet);
            list.addAll(visit(expression, context2));
        }
        return list;
    }

    @Override
    public List<Tree> visitPattern(PatternExpression pattern, Context args) {
        if (!AnyMatcher.scan(pattern, args)) {
            throw new ParsingException(Message.NO_MATCHING_TOKEN.format());
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
        var count = quantfier.stream()
                .takeWhile(e -> {
                    if (!AnyMatcher.scan(e, args)) {
                        return false;
                    }
                    list.addAll(visit(e, args));
                    return true;
                })
                .count();
        if (Integer.MAX_VALUE < count) {
            throw new ParsingException(Message.UNEXPECTED_OCCURENCES.format());
        }
        var lowerLimit = quantfier.getLowerLimit();
        var upperLimit = quantfier.getUpperLimit().orElse((int) count);
        if (count < lowerLimit || upperLimit < count) {
            throw new ParsingException(Message.OCCURENCES_OUT_OF_RANGE.format());
        }
        return list;
    }

    @Override
    public List<Tree> visitEmpty(Expression empty, Context args) {
        if (!AnyMatcher.scan(empty, args)) {
            throw new ParsingException(Message.NO_MATCHING_TOKEN.format());
        }
        return List.of();
    }
}
