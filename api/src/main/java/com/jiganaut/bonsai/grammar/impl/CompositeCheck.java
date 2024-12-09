package com.jiganaut.bonsai.grammar.impl;

import java.util.Set;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SequenceRule;
import com.jiganaut.bonsai.grammar.SimpleRuleVisitor;
import com.jiganaut.bonsai.grammar.SkipRule;
import com.jiganaut.bonsai.impl.Message;

/**
 * 
 * @author Junji Mikami
 */
class CompositeCheck implements SimpleRuleVisitor<Void, Void> {
    private static final CompositeCheck INSTANCE = new CompositeCheck();

    private CompositeCheck() {
    }

    static void run(Set<Production> set) {
        set.stream()
                .map(e -> e.getRule())
                .forEach(e -> INSTANCE.visit(e));
    }

    @Override
    public Void visitChoice(ChoiceRule choice, Void p) {
        if (choice.getChoices().isEmpty()) {
            throw new IllegalStateException(Message.EMPTY_CHOICE.format());
        }
        choice.getChoices().forEach(this::visit);
        return null;
    }

    @Override
    public Void visitSequence(SequenceRule sequence, Void p) {
        if (sequence.getRules().isEmpty()) {
            throw new IllegalStateException(Message.EMPTY_SEQUENCE.format());
        }
        sequence.getRules().forEach(this::visit);
        return null;
    }

    @Override
    public Void visitQuantifier(QuantifierRule quantifier, Void p) {
        visit(quantifier.getRule());
        return null;
    }

    @Override
    public Void visitSkip(SkipRule skip, Void p) {
        visit(skip.getRule());
        return null;
    }

    @Override
    public Void defaultAction(Rule rule, Void p) {
        return null;
    }

}
