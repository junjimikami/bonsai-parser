package com.jiganaut.bonsai.grammar.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.ProductionRule;
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
class ReferenceValidator<T> implements SimpleRuleVisitor<T, Void, List<String>> {

    private static final ReferenceValidator<?> INSTANCE = new ReferenceValidator<>();

    private ReferenceValidator() {
    }

    @SuppressWarnings("unchecked")
    static <T> void validate(Set<ProductionRule<T>> set) {
        var symbols = set.stream()
                .map(ProductionRule::getSymbol)
                .toList();
        var checker = (ReferenceValidator<T>) INSTANCE;
        set.stream()
                .map(ProductionRule::getRule)
                .forEach(e -> checker.visit(e, symbols));
    }

    @Override
    public Void visitChoice(ChoiceRule<T> choice, List<String> p) {
        choice.getChoices().forEach(e -> visit(e, p));
        return null;
    }

    @Override
    public Void visitSequence(SequenceRule<T> sequence, List<String> p) {
        sequence.getRules().forEach(e -> visit(e, p));
        return null;
    }

    @Override
    public Void visitReference(ReferenceRule<T> reference, List<String> p) {
        if (!p.contains(reference.getSymbol())) {
            throw new NoSuchElementException(Message.GRAMMAR_SYMBOL_NOT_FOUND.format(reference.getSymbol()));
        }
        return null;
    }

    @Override
    public Void visitQuantifier(QuantifierRule<T> quantifier, List<String> p) {
        visit(quantifier.getRule(), p);
        return null;
    }

    @Override
    public Void visitSkip(SkipRule<T> skip, List<String> p) {
        visit(skip.getRule(), p);
        return null;
    }

    @Override
    public Void visitProduction(ProductionRule<T> production, List<String> p) {
        visit(production.getRule(), p);
        return null;
    }

    @Override
    public Void defaultAction(Rule<T> rule, List<String> p) {
        return null;
    }

}
