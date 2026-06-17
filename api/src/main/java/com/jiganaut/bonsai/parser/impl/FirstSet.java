package com.jiganaut.bonsai.parser.impl;

import java.util.HashSet;
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
final class FirstSet<T> implements RuleVisitor<T, Set<MatchingRule<T>>, Context<T>> {

    private static final FirstSet<?> INSTANCE = new FirstSet<>();

    private FirstSet() {
    }

    static <T> Set<MatchingRule<T>> of(Rule<T> rule, Context<T> context) {
        @SuppressWarnings("unchecked")
        var instance = (FirstSet<T>) INSTANCE;
        return instance.visit(rule, context.resetPath());
    }

    static <T> boolean scan(Rule<T> rule, Context<T> context) {
        var firstSet = of(rule, context);
        return firstSet.stream().anyMatch(e -> e.test(context.peek()));
    }

    @Override
    public Set<MatchingRule<T>> visitChoice(ChoiceRule<T> choice, Context<T> context) {
        if (choice.getChoices().isEmpty()) {
            return context.followSet();
        }
        var set = new HashSet<MatchingRule<T>>();
        for (var rule : choice.getChoices()) {
            set.addAll(visit(rule, context));
        }
        return set;
    }

    @Override
    public Set<MatchingRule<T>> visitSequence(SequenceRule<T> sequence, Context<T> context) {
        if (sequence.getRules().isEmpty()) {
            return context.followSet();
        }
        var subRules = new ArrayListRule<T>(sequence);
        var rule = subRules.removeFirst();
        var subContext = context.subContext(() -> visit(subRules, context));
        return visit(rule, subContext);
    }

    @Override
    public Set<MatchingRule<T>> visitMatch(MatchingRule<T> match, Context<T> context) {
        return Set.of(match);
    }

    @Override
    public Set<MatchingRule<T>> visitReference(ReferenceRule<T> reference, Context<T> context) {
        var productionChoice = reference.lookup(context.grammar());
        return visit(productionChoice, context);
    }

    @Override
    public Set<MatchingRule<T>> visitQuantifier(QuantifierRule<T> quantifier, Context<T> context) {
        var set = new HashSet<MatchingRule<T>>();
        var rule = quantifier.stream()
                .limit(1)
                .findFirst();
        if (rule.isPresent()) {
            var subContext = context.subContext(Set::of);
            set.addAll(visit(rule.get(), subContext));
        }
        if (quantifier.getMinCount() == 0) {
            set.addAll(context.followSet());
        }
        return set;
    }

    @Override
    public Set<MatchingRule<T>> visitSkip(SkipRule<T> skip, Context<T> context) {
        return visit(skip.getRule(), context);
    }

    @Override
    public Set<MatchingRule<T>> visitEmpty(EmptyRule<T> empty, Context<T> context) {
        return context.followSet();
    }

    @Override
    public Set<MatchingRule<T>> visitProduction(ProductionRule<T> production, Context<T> context) {
        if (context.productionPath().contains(production)) {
            return Set.of();
        }
        var subContext = context.subContext(production);
        return visit(production.getRule(), subContext);
    }

}
