package com.jiganaut.bonsai.grammar.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SequenceRule;
import com.jiganaut.bonsai.grammar.SimpleRuleVisitor;
import com.jiganaut.bonsai.grammar.SkipRule;
import com.jiganaut.bonsai.impl.Message;

/**
 * 
 * @author Junji Mikami
 */
class ReferenceCheck implements SimpleRuleVisitor<Void, List<String>> {
    private static final ReferenceCheck INSTANCE = new ReferenceCheck();

    private ReferenceCheck() {
    }

    static void run(Set<Production> set) {
        var symbols = set.stream()
                .map(e -> e.getSymbol())
                .toList();
        set.stream()
                .map(e -> e.getRule())
                .forEach(e -> INSTANCE.visit(e, symbols));
    }

    @Override
    public Void visitChoice(ChoiceRule choice, List<String> p) {
        choice.getChoices().forEach(e -> visit(e, p));
        return null;
    }

    @Override
    public Void visitSequence(SequenceRule sequence, List<String> p) {
        sequence.getRules().forEach(e -> visit(e, p));
        return null;
    }

    @Override
    public Void visitReference(ReferenceRule reference, List<String> p) {
        if (!p.contains(reference.getSymbol())) {
            throw new NoSuchElementException(Message.SYMBOL_NOT_FOUND.format(reference.getSymbol()));
        }
        return null;
    }

    @Override
    public Void visitQuantifier(QuantifierRule quantifier, List<String> p) {
        visit(quantifier.getRule(), p);
        return null;
    }

    @Override
    public Void visitSkip(SkipRule skip, List<String> p) {
        visit(skip.getRule(), p);
        return null;
    }

    @Override
    public Void defaultAction(Rule rule, List<String> p) {
        return null;
    }

}
