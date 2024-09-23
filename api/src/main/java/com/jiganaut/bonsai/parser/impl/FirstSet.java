package com.jiganaut.bonsai.parser.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.RuleVisitor;
import com.jiganaut.bonsai.grammar.SequenceRule;

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

    @Override
    public Set<Rule> visit(Rule prd) {
        var context = new Context(null, null, null, Set.of());
        return visit(prd, context);
    }

    private Set<Rule> visit(List<? extends Rule> sequence, Context context) {
        if (sequence.isEmpty()) {
            return context.followSet();
        }
        var subRules = new LinkedList<>(sequence);
        var rule = subRules.removeFirst();
        var subFollowSet = Set.<Rule>of(new SequenceRule() {
            @Override
            public List<? extends Rule> getRules() {
                var set = visit(subRules, context);
                return new ArrayList<>(set);
            }

            @Override
            public String toString() {
                return subRules.toString();
            }
        });
        var subContext = context.withFollowSet(subFollowSet);
        return visit(rule, subContext);
    }

    @Override
    public Set<Rule> visitChoice(ChoiceRule alt, Context context) {
        if (alt.getChoices().isEmpty()) {
            return context.followSet();
        }
        var set = new HashSet<Rule>();
        for (var p : alt.getChoices()) {
            set.addAll(visit(p, context));
        }
        return set;
    }

    @Override
    public Set<Rule> visitSequence(SequenceRule seq, Context context) {
        return visit(seq.getRules(), context);
    }

    @Override
    public Set<Rule> visitPattern(PatternRule p, Context context) {
        return Set.of(p);
    }

    @Override
    public Set<Rule> visitReference(ReferenceRule ref, Context context) {
        var productionSet = context.grammar().productionSet();
        var production = ref.getProduction(productionSet);
        return visit(production.getRule(), context);
    }

    @Override
    public Set<Rule> visitQuantifier(QuantifierRule qt, Context context) {
        var set = new HashSet<Rule>();
        var prd = qt.stream()
                .limit(1)
                .findFirst();
        if (prd.isPresent()) {
            set.addAll(visit(prd.get()));
        }
        if (qt.getMinCount() == 0) {
            set.addAll(context.followSet());
        }
        return set;
    }

    @Override
    public Set<Rule> visitEmpty(Rule empty, Context context) {
        return context.followSet();
    }
}
