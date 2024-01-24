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

    static Tree parse(Context context) {
        return instance.interpret(context);
    }

    private Tree interpret(Context context) {
        var production = context.production();
        var trees = visit(production.getExpression(), context);
        return new DefaultNonTerminal(production.getSymbol(), trees);
    }

    @Override
    public List<Tree> visit(Expression expression, Context context) {
        if (!AnyMatcher.scan(expression, context)) {
            context.skip();
        }
        return ExpressionVisitor.super.visit(expression, context);
    }

    @Override
    public List<Tree> visitChoice(ChoiceExpression choice, Context context) {
        var list = choice.getChoices().stream()
                .filter(e -> AnyMatcher.scan(e, context))
                .toList();
        if (list.isEmpty()) {
            var message = MessageSupport.tokenNotMatchExpression(choice, context);
            throw new ParsingException(message);
        }
        if (1 < list.size()) {
            var message = MessageSupport.ambiguousChoice(choice, context);
            throw new ParsingException(message);
        }
        return visit(list.get(0), context);
    }

    @Override
    public List<Tree> visitSequence(SequenceExpression sequence, Context context) {
        if (!AnyMatcher.scan(sequence, context)) {
            var message = MessageSupport.tokenNotMatchExpression(sequence, context);
            throw new ParsingException(message);
        }
        var list = new ArrayList<Tree>();
        var subSequence = new LinkedList<>(sequence.getSequence());
        while (!subSequence.isEmpty()) {
            var expression = subSequence.remove();
            var followSet = FirstSet.of(subSequence, context.followSet());
            var context2 = context.withFollowSet(followSet);
            list.addAll(visit(expression, context2));
        }
        return list;
    }

    @Override
    public List<Tree> visitPattern(PatternExpression pattern, Context context) {
        if (!AnyMatcher.scan(pattern, context)) {
            var message = MessageSupport.tokenNotMatchExpression(pattern, context);
            throw new ParsingException(message);
        }
        var tokenizer = context.tokenizer();
        var token = tokenizer.next(pattern.getPattern());
        return List.of(token);
    }

    @Override
    public List<Tree> visitReference(ReferenceExpression reference, Context context) {
        var production = reference.get();
        var context2 = context.withProduction(production);
        var tree = interpret(context2);
        return List.of(tree);
    }

    @Override
    public List<Tree> visitQuantifier(QuantifierExpression quantfier, Context context) {
        var list = new ArrayList<Tree>();
        var count = (int) quantfier.stream()
                .takeWhile(e -> {
                    if (!AnyMatcher.scan(e, context)) {
                        return false;
                    }
                    list.addAll(visit(e, context));
                    return true;
                })
                .count();
        var lowerLimit = quantfier.getLowerLimit();
        var upperLimit = quantfier.getUpperLimit().orElse(count);
        if (count < lowerLimit || upperLimit < count) {
            var message = MessageSupport.tokenCountOutOfRange(quantfier, context, count);
            throw new ParsingException(message);
        }
        return list;
    }

    @Override
    public List<Tree> visitEmpty(Expression empty, Context context) {
        if (!AnyMatcher.scan(empty, context)) {
            var message = MessageSupport.tokenNotMatchExpression(empty, context);
            throw new ParsingException(message);
        }
        return List.of();
    }

}
