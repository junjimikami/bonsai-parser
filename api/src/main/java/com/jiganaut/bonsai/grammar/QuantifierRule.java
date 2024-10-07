package com.jiganaut.bonsai.grammar;

import java.util.OptionalInt;
import java.util.stream.Stream;

/**
 * @author Junji Mikami
 *
 */
public interface QuantifierRule extends Rule {

    @Override
    public default Kind getKind() {
        return Kind.QUANTIFIER;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
        return visitor.visitQuantifier(this, p);
    }

    public int getMinCount();
    public OptionalInt getMaxCount();
    public Rule getRule();
    public Stream<Rule> stream();
}
