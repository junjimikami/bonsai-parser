package com.unitedjiga.common.parsing.grammar;

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
    public default R visitPattern(PatternRule pattern, P p) {
        return defaultAction(pattern, p);
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
    public default R visitEmpty(Rule empty, P p) {
        return defaultAction(empty, p);
    }

    public R defaultAction(Rule rule, P p);
}
