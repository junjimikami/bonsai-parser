package com.jiganaut.bonsai.parser.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.PatternRule;
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
final class FirstSet implements RuleVisitor<Set<Rule>, Context> {
    private static final FirstSet INSTANCE = new FirstSet();

    private FirstSet() {
    }

    static Set<Rule> of(Rule rule, Context context) {
        return INSTANCE.visit(rule, context);
    }

    static Set<Rule> of(List<? extends Rule> sequence, Context context) {
        return INSTANCE.visit(sequence, context);
    }

    @FunctionalInterface
    private interface TemporaryRule extends Rule, Supplier<Set<Rule>> {
        @Override
        default Kind getKind() {
            throw new AssertionError();
        }
        
        @Override
        default <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
            throw new AssertionError();
        }
    }

    private Set<Rule> visit(List<? extends Rule> sequence, Context context) {
        if (sequence.isEmpty()) {
            return context.followSet();
        }
        var subRules = new LinkedList<>(sequence);
        var rule = subRules.removeFirst();
        var remaining = (TemporaryRule) () -> visit(subRules, context);
        var subFollowSet = Set.<Rule>of(remaining);
        var subContext = context.withFollowSet(subFollowSet);
        return visit(rule, subContext).stream()
                .<Rule>mapMulti((r, c) -> {
                    if (r instanceof TemporaryRule tr) {
                        tr.get().forEach(c::accept);
                    } else {
                        c.accept(r);
                    }
                })
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Rule> visitChoice(ChoiceRule choice, Context context) {
        if (choice.getChoices().isEmpty()) {
            return context.followSet();
        }
        var set = new HashSet<Rule>();
        for (var rule : choice.getChoices()) {
            set.addAll(visit(rule, context));
        }
        return set;
    }

    @Override
    public Set<Rule> visitSequence(SequenceRule sequence, Context context) {
        return visit(sequence.getRules(), context);
    }

    @Override
    public Set<Rule> visitPattern(PatternRule pattern, Context context) {
        return Set.of(pattern);
    }

    @Override
    public Set<Rule> visitReference(ReferenceRule reference, Context context) {
        var productionSet = context.productionSet();
        var production = reference.lookup(productionSet);
        return visit(production.getRule(), context);
    }

    @Override
    public Set<Rule> visitQuantifier(QuantifierRule quantifier, Context context) {
        var set = new HashSet<Rule>();
        var rule = quantifier.stream()
                .limit(1)
                .findFirst();
        if (rule.isPresent()) {
            var subContext = context.withFollowSet(Set.of());
            set.addAll(visit(rule.get(), subContext));
        }
        if (quantifier.getMinCount() == 0) {
            set.addAll(context.followSet());
        }
        return set;
    }

    @Override
    public Set<Rule> visitSkip(SkipRule skip, Context context) {
        return visit(skip.getRule(), context);
    }

    @Override
    public Set<Rule> visitEmpty(Rule empty, Context context) {
        return context.followSet();
    }
}
