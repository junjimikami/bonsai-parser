package com.jiganaut.bonsai.grammar;

/**
 * @author Junji Mikami
 *
 */
public interface RuleVisitor<R, P> {

    public default R visit(Rule rule) {
        return visit(rule, null);
    }

    public default R visit(Rule rule, P p) {
        return rule.accept(this, p);
    }

    public R visitChoice(ChoiceRule choice, P p);

    public R visitSequence(SequenceRule sequence, P p);

    public R visitPattern(PatternRule pattern, P p);

    public R visitReference(ReferenceRule reference, P p);

    public R visitQuantifier(QuantifierRule quantifier, P p);

    public R visitEmpty(Rule rule, P p);
}
