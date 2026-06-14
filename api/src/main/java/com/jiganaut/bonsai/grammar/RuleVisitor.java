package com.jiganaut.bonsai.grammar;

/**
 * @author Junji Mikami
 *
 */
public interface RuleVisitor<T, R, P> {

    public default R visit(Rule<T> rule) {
        return rule.accept(this);
    }

    public default R visit(Rule<T> rule, P p) {
        return rule.accept(this, p);
    }

    public R visitChoice(ChoiceRule<T> choice, P p);

    public default R visitChoiceAsShortCircuit(ChoiceRule<T> choice, P p) {
        return visitChoice(choice, p);
    }

    public R visitSequence(SequenceRule<T> sequence, P p);

    public R visitMatch(MatchingRule<T> match, P p);

    public R visitReference(ReferenceRule<T> reference, P p);

    public R visitQuantifier(QuantifierRule<T> quantifier, P p);

    public R visitSkip(SkipRule<T> skip, P p);

    public R visitEmpty(EmptyRule<T> empty, P p);

    public R visitProduction(ProductionRule<T> production, P p);
}
