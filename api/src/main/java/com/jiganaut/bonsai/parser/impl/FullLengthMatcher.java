package com.jiganaut.bonsai.parser.impl;

import java.util.Set;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.EmptyRule;
import com.jiganaut.bonsai.grammar.MatchingRule;
import com.jiganaut.bonsai.grammar.ProductionRule;
import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.RuleVisitor;
import com.jiganaut.bonsai.grammar.SequenceRule;
import com.jiganaut.bonsai.grammar.SkipRule;

/**
 * @author Junji Mikami
 *
 */
final class FullLengthMatcher<T> implements RuleVisitor<T, Boolean, Context<T>> {

    private static final FullLengthMatcher<?> INSTANCE = new FullLengthMatcher<>();

    private FullLengthMatcher() {
    }

    static <T> boolean scan(Rule<T> rule, Context<T> context) {
        @SuppressWarnings("unchecked")
        var instance = (FullLengthMatcher<T>) INSTANCE;
        return instance.visit(rule, context);
    }

    @Override
    public Boolean visitChoiceAsShortCircuit(ChoiceRule<T> choice, Context<T> context) {
        var cursor = context.startCache();
        for (var rule : choice.getChoices()) {
            if (visit(rule, context)) {
                cursor.clear();
                return true;
            }
            cursor.reset();
        }
        return false;
    }

    @Override
    public Boolean visitChoice(ChoiceRule<T> choice, Context<T> context) {
        var candidates = choice.getChoices().stream()
                .filter(e -> FirstSet.scan(e, context))
                .toList();
        if (candidates.isEmpty()) {
            return false;
        }
        if (candidates.size() == 1) {
            return visit(candidates.get(0), context);
        }
        var subContext = context.subContext(Set.of());
        candidates = candidates.stream()
                .filter(e -> FirstSet.scan(e, subContext))
                .toList();
        if (candidates.isEmpty()) {
            return false;
        }
        if (1 < candidates.size()) {
            return false;
        }
        return visit(candidates.get(0), context);
    }

    @Override
    public Boolean visitSequence(SequenceRule<T> sequence, Context<T> context) {
        if (sequence.getRules().isEmpty()) {
            return false;
        }
        var rules = new ArrayListRule<>(sequence);
        while (!rules.isEmpty()) {
            var rule = rules.removeFirst();
            var subFollowSet = FirstSet.of(rules, context);
            var subContext = context.subContext(subFollowSet);
            if (!visit(rule, subContext)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean visitMatch(MatchingRule<T> match, Context<T> context) {
        if (!context.hasNext()) {
            return false;
        }
        var token = context.next();
        if (!match.test(token)) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean visitReference(ReferenceRule<T> reference, Context<T> context) {
        var productionChoice = reference.lookup(context.grammar());
        return visit(productionChoice, context);
    }

    @Override
    public Boolean visitQuantifier(QuantifierRule<T> quantifier, Context<T> context) {
        long count = quantifier.stream().takeWhile(e -> visit(e, context)).count();
        return quantifier.getMinCount() <= count;
    }

    @Override
    public Boolean visitSkip(SkipRule<T> skip, Context<T> context) {
        return visit(skip.getRule(), context);
    }

    @Override
    public Boolean visitEmpty(EmptyRule<T> empty, Context<T> context) {
        return context.followSet().stream().anyMatch(e -> e.test(context.peek()));
    }

    @Override
    public Boolean visitProduction(ProductionRule<T> production, Context<T> context) {
        return visit(production.getRule(), context);
    }

}
