package com.jiganaut.bonsai.parser.impl;

import java.util.LinkedList;
import java.util.Set;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.ProductionSet;
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
final class FullLengthMatcher implements RuleVisitor<Boolean, Context> {
    private static final FullLengthMatcher INSTANCE = new FullLengthMatcher();

    private FullLengthMatcher() {
    }

    static boolean scan(Rule rule, Context context) {
        return INSTANCE.visit(rule, context);
    }

    private boolean visitProductionSet(ProductionSet productionSet, Context context) {
        if (productionSet.isShortCircuit()) {
            return visitProductionSetAsShortCircuit(productionSet, context);
        }
        return visitProductionSetAsNotShortCircuit(productionSet, context);
    }

    private boolean visitProductionSetAsShortCircuit(ProductionSet productionSet, Context context) {
        int position = context.mark();
        for (var production : productionSet) {
            if (visit(production.getRule(), context)) {
                context.clear();
                return true;
            }
            context.reset(position);
        }
        return false;
    }

    private boolean visitProductionSetAsNotShortCircuit(ProductionSet productionSet, Context context) {
        var rules = productionSet.stream()
                .map(Production::getRule)
                .filter(e -> FirstSetMatcher.scan(e, context))
                .toList();
        if (rules.isEmpty()) {
            return false;
        }
        if (rules.size() == 1) {
            return visit(rules.get(0), context);
        }
        var subContext = context.withFollowSet(Set.of());
        rules = rules.stream()
                .filter(e -> FirstSetMatcher.scan(e, subContext))
                .toList();
        if (rules.isEmpty()) {
            return false;
        }
        if (1 < rules.size()) {
            return false;
        }
        return visit(rules.get(0), context);
    }

    @Override
    public Boolean visitChoice(ChoiceRule choice, Context context) {
        if (choice.isShortCircuit()) {
            return visitChoiceAsShortCircuit(choice, context);
        }
        return visitChoiceAsNotShortCircuit(choice, context);
    }

    private Boolean visitChoiceAsShortCircuit(ChoiceRule choice, Context context) {
        int position = context.mark();
        for (var rule : choice.getChoices()) {
            if (visit(rule, context)) {
                context.clear();
                return true;
            }
            context.reset(position);
        }
        return false;
    }

    private Boolean visitChoiceAsNotShortCircuit(ChoiceRule choice, Context context) {
        var rules = choice.getChoices().stream()
                .filter(e -> FirstSetMatcher.scan(e, context))
                .toList();
        if (rules.isEmpty()) {
            return false;
        }
        if (rules.size() == 1) {
            return visit(rules.get(0), context);
        }
        var subContext = context.withFollowSet(Set.of());
        rules = rules.stream()
                .filter(e -> FirstSetMatcher.scan(e, subContext))
                .toList();
        if (rules.isEmpty()) {
            return false;
        }
        if (1 < rules.size()) {
            return false;
        }
        return visit(rules.get(0), context);
    }

    @Override
    public Boolean visitSequence(SequenceRule sequence, Context context) {
        if (sequence.getRules().isEmpty()) {
            return false;
        }
        var rules = new LinkedList<>(sequence.getRules());
        while (!rules.isEmpty()) {
            var rule = rules.removeFirst();
            var subFollowSet = FirstSet.of(rules, context);
            var subContext = context.withFollowSet(subFollowSet);
            if (!visit(rule, subContext)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean visitPattern(PatternRule pattern, Context context) {
        if (!context.hasNextValue(pattern.getPattern())) {
            return false;
        }
        context.next();
        return true;
    }

    @Override
    public Boolean visitReference(ReferenceRule reference, Context context) {
        var productionSet = reference.lookup(context.grammar());
        return visitProductionSet(productionSet, context);
    }

    @Override
    public Boolean visitQuantifier(QuantifierRule quantifier, Context context) {
        long count = quantifier.stream().takeWhile(e -> visit(e, context)).count();
        return quantifier.getMinCount() <= count;
    }

    @Override
    public Boolean visitSkip(SkipRule skip, Context context) {
        return visit(skip.getRule(), context);
    }

    @Override
    public Boolean visitEmpty(Rule empty, Context context) {
        var firstSet = FirstSet.of(empty, context);
        if (firstSet.isEmpty()) {
            return !context.hasNext();
        }
        return firstSet.stream().anyMatch(e -> FirstSetMatcher.scan(e, context));
    }

}