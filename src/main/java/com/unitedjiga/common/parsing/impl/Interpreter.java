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
 * @author Junji Mikami
 *
 */
final class Interpreter implements RuleVisitor<List<Tree>, Context> {
    private static final Interpreter instance = new Interpreter();

    private Interpreter() {
    }

    static Tree parse(Context context) {
        var production = context.production();
        var trees = instance.visit(production.getRule(), context);
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
        var rules = choice.getChoices().stream()
                .filter(e -> AnyMatcher.scan(e, context))
                .toList();
        if (rules.isEmpty()) {
            var message = MessageSupport.tokenNotMatchRule(choice, context);
            throw new ParseException(message);
        }
        if (1 < rules.size()) {
            var message = MessageSupport.ambiguousChoice(choice, context);
            throw new ParseException(message);
        }
        return visit(rules.get(0), context);
    }

    @Override
    public List<Tree> visitSequence(SequenceRule sequence, Context context) {
        if (!AnyMatcher.scan(sequence, context)) {
            var message = MessageSupport.tokenNotMatchRule(sequence, context);
            throw new ParseException(message);
        }
        var trees = new ArrayList<Tree>();
        var rules = new LinkedList<>(sequence.getRules());
        while (!rules.isEmpty()) {
            var rule = rules.removeFirst();
            var subFollowSet = FirstSet.of(rules, context.followSet());
            var subContext = context.withFollowSet(subFollowSet);
            trees.addAll(visit(rule, subContext));
        }
        return trees;
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
        var subContext = context.withProduction(production);
        var tree = parse(subContext);
        return List.of(tree);
    }

    @Override
    public List<Tree> visitQuantifier(QuantifierRule quantfier, Context context) {
        var trees = new ArrayList<Tree>();
        long count = quantfier.stream()
                .takeWhile(e -> {
                    if (!AnyMatcher.scan(e, context)) {
                        return false;
                    }
                    return trees.addAll(visit(e, context));
                })
                .count();
        if (count < quantfier.getMinCount()) {
            var message = MessageSupport.tokenCountOutOfRange(quantfier, context, count);
            throw new ParseException(message);
        }
        quantfier.getMaxCount().ifPresent(maxCount -> {
            if (maxCount < count) {
                var message = MessageSupport.tokenCountOutOfRange(quantfier, context, count);
                throw new ParseException(message);
            }
        });
        return trees;
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
