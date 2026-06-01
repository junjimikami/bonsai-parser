package com.jiganaut.bonsai.grammar;

/**
 * @author Junji Mikami
 *
 */
public interface SimpleRuleVisitor<R, P> extends RuleVisitor<R, P> {

    @Override
    public default R visitChoice(ChoiceRule choice, P p) {
        return defaultAction(choice, p);
    }

    @Override
    public default R visitSequence(SequenceRule sequence, P p) {
        return defaultAction(sequence, p);
    }

    @Override
    public default R visitMatch(MatchingRule match, P p) {
        return defaultAction(match, p);
    }

    @Override
    public default R visitReference(ReferenceRule reference, P p) {
        return defaultAction(reference, p);
    }

    @Override
    public default R visitQuantifier(QuantifierRule quantifier, P p) {
        return defaultAction(quantifier, p);
    }

    @Override
    public default R visitSkip(SkipRule skip, P p) {
        return defaultAction(skip, p);
    }

    @Override
    public default R visitEmpty(EmptyRule empty, P p) {
        return defaultAction(empty, p);
    }

    @Override
    public default R visitProduction(ProductionRule production, P p) {
        return defaultAction(production, p);
    }

    public R defaultAction(Rule rule, P p);
}
