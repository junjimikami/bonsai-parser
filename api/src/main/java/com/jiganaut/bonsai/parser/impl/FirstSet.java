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
final class FirstSet implements RuleVisitor<Set<Rule>, Set<Rule>> {
    private static final FirstSet instance = new FirstSet();

    private FirstSet() {
    }

    static Set<Rule> of(Rule rule, Set<Rule> followSet) {
        return instance.visit(rule, followSet);
    }

    static Set<Rule> of(List<? extends Rule> sequence, Set<Rule> followSet) {
        return instance.visit(sequence, followSet);
    }

    @Override
    public Set<Rule> visit(Rule prd) {
        return visit(prd, Set.of());
    }

    private Set<Rule> visit(List<? extends Rule> sequence, Set<Rule> followSet) {
        if (sequence.isEmpty()) {
            return followSet;
        }
        var subRules = new LinkedList<>(sequence);
        var rule = subRules.removeFirst();
        var subFollowSet = Set.<Rule>of(new SequenceRule() {
            @Override
            public List<? extends Rule> getRules() {
                var set = visit(subRules, followSet);
                return new ArrayList<>(set);
            }

            @Override
            public String toString() {
                return subRules.toString();
            }
        });
        return visit(rule, subFollowSet);
    }

    @Override
    public Set<Rule> visitChoice(ChoiceRule alt, Set<Rule> followSet) {
        if (alt.getChoices().isEmpty()) {
            return followSet;
        }
        var set = new HashSet<Rule>();
        for (var p : alt.getChoices()) {
            set.addAll(visit(p, followSet));
        }
        return set;
    }

    @Override
    public Set<Rule> visitSequence(SequenceRule seq, Set<Rule> followSet) {
        return visit(seq.getRules(), followSet);
    }

    @Override
    public Set<Rule> visitPattern(PatternRule p, Set<Rule> followSet) {
        return Set.of(p);
    }

    @Override
    public Set<Rule> visitReference(ReferenceRule ref, Set<Rule> followSet) {
        var production = ref.getProduction();
        return visit(production.getRule(), followSet);
    }

    @Override
    public Set<Rule> visitQuantifier(QuantifierRule qt, Set<Rule> followSet) {
        var set = new HashSet<Rule>();
        var prd = qt.stream()
                .limit(1)
                .findFirst();
        if (prd.isPresent()) {
            set.addAll(visit(prd.get()));
        }
        if (qt.getMinCount() == 0) {
            set.addAll(followSet);
        }
        return set;
    }

    @Override
    public Set<Rule> visitEmpty(Rule empty, Set<Rule> followSet) {
        return followSet;
    }
}
