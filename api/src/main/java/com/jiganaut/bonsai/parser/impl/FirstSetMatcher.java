package com.jiganaut.bonsai.parser.impl;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SequenceRule;
import com.jiganaut.bonsai.grammar.SimpleRuleVisitor;

/**
 * @author Junji Mikami
 *
 */
final class FirstSetMatcher implements SimpleRuleVisitor<Boolean, Context> {
    private static final FirstSetMatcher INSTANCE = new FirstSetMatcher();

    private FirstSetMatcher() {
    }

    static boolean scan(Rule rule, Context context) {
        return INSTANCE.visit(rule, context);
    }

    @Override
    public Boolean visitPattern(PatternRule pattern, Context context) {
        return context.hasNext(pattern.getPattern());
    }

    @Override
    public Boolean visitChoice(ChoiceRule choice, Context p) {
        if (choice.getChoices().isEmpty()) {
            return false;
        }
        return SimpleRuleVisitor.super.visitChoice(choice, p);
    }

    @Override
    public Boolean visitSequence(SequenceRule sequence, Context p) {
        if (sequence.getRules().isEmpty()) {
            return false;
        }
        return SimpleRuleVisitor.super.visitSequence(sequence, p);
    }

    @Override
    public Boolean defaultAction(Rule rule, Context context) {
        var firstSet = FirstSet.of(rule, context);
        if (firstSet.isEmpty()) {
            return !context.hasNext();
        }
        return firstSet.stream().anyMatch(e -> visit(e, context));
    }
}
