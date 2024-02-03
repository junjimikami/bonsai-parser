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

import com.unitedjiga.common.parsing.ParseException;
import com.unitedjiga.common.parsing.Tree;
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
final class Interpreter implements RuleVisitor<List<Tree>, Context> {
    private static final Interpreter instance = new Interpreter();

    private Interpreter() {
    }

    static Tree parse(Context context) {
        return instance.interpret(context);
    }

    private Tree interpret(Context context) {
        var production = context.production();
        var trees = visit(production.getRule(), context);
        return new DefaultNonTerminal(production.getSymbol(), trees);
    }

    @Override
    public List<Tree> visit(Rule rule, Context context) {
        if (!AnyMatcher.scan(rule, context)) {
            context.skip();
        }
        return RuleVisitor.super.visit(rule, context);
    }

    @Override
    public List<Tree> visitChoice(ChoiceRule choice, Context context) {
        var list = choice.getChoices().stream()
                .filter(e -> AnyMatcher.scan(e, context))
                .toList();
        if (list.isEmpty()) {
            var message = MessageSupport.tokenNotMatchRule(choice, context);
            throw new ParseException(message);
        }
        if (1 < list.size()) {
            var message = MessageSupport.ambiguousChoice(choice, context);
            throw new ParseException(message);
        }
        return visit(list.get(0), context);
    }

    @Override
    public List<Tree> visitSequence(SequenceRule sequence, Context context) {
        if (!AnyMatcher.scan(sequence, context)) {
            var message = MessageSupport.tokenNotMatchRule(sequence, context);
            throw new ParseException(message);
        }
        var list = new ArrayList<Tree>();
        var subSequence = new LinkedList<>(sequence.getRules());
        while (!subSequence.isEmpty()) {
            var rule = subSequence.remove();
            var followSet = FirstSet.of(subSequence, context.followSet());
            var context2 = context.withFollowSet(followSet);
            list.addAll(visit(rule, context2));
        }
        return list;
    }

    @Override
    public List<Tree> visitPattern(PatternRule pattern, Context context) {
        if (!AnyMatcher.scan(pattern, context)) {
            var message = MessageSupport.tokenNotMatchRule(pattern, context);
            throw new ParseException(message);
        }
        var tokenizer = context.tokenizer();
        var token = tokenizer.next(pattern.getPattern());
        return List.of(token);
    }

    @Override
    public List<Tree> visitReference(ReferenceRule reference, Context context) {
        var production = reference.getProduction();
        var context2 = context.withProduction(production);
        var tree = interpret(context2);
        return List.of(tree);
    }

    @Override
    public List<Tree> visitQuantifier(QuantifierRule quantfier, Context context) {
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
        var minCount = quantfier.getMinCount();
        var maxCount = quantfier.getMaxCount().orElse(count);
        if (count < minCount || maxCount < count) {
            var message = MessageSupport.tokenCountOutOfRange(quantfier, context, count);
            throw new ParseException(message);
        }
        return list;
    }

    @Override
    public List<Tree> visitEmpty(Rule empty, Context context) {
        if (!AnyMatcher.scan(empty, context)) {
            var message = MessageSupport.tokenNotMatchRule(empty, context);
            throw new ParseException(message);
        }
        return List.of();
    }

}
