package com.jiganaut.bonsai.grammar;

/**
 * @author Junji Mikami
 *
 */
public interface SimpleRuleVisitor<T, R, P> extends RuleVisitor<T, R, P> {

    @Override
    public default R visitChoice(ChoiceRule<T> choice, P p) {
        return defaultAction(choice, p);
    }

    @Override
    public default R visitSequence(SequenceRule<T> sequence, P p) {
        return defaultAction(sequence, p);
    }

    @Override
    public default R visitMatch(MatchingRule<T> match, P p) {
        return defaultAction(match, p);
    }

    @Override
    public default R visitReference(ReferenceRule<T> reference, P p) {
        return defaultAction(reference, p);
    }

    @Override
    public default R visitQuantifier(QuantifierRule<T> quantifier, P p) {
        return defaultAction(quantifier, p);
    }

    @Override
    public default R visitSkip(SkipRule<T> skip, P p) {
        return defaultAction(skip, p);
    }

    @Override
    public default R visitEmpty(EmptyRule<T> empty, P p) {
        return defaultAction(empty, p);
    }

    @Override
    public default R visitProduction(ProductionRule<T> production, P p) {
        return defaultAction(production, p);
    }

    public R defaultAction(Rule<T> rule, P p);
}
