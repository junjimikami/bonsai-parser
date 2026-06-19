package com.jiganaut.bonsai.grammar;

import java.util.Objects;

import com.jiganaut.bonsai.impl.Message;

/**
 *
 * @author Junji Mikami
 */
public interface ProductionRule<T> extends Rule<T> {

    @Override
    public default Rule.Kind getKind() {
        return Rule.Kind.PRODUCTION;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<T, R, P> visitor, P p) {
        Objects.requireNonNull(visitor, () -> Message.VALIDATION_PARAMETER_NULL.format("visitor"));
        return visitor.visitProduction(this, p);
    }

    public String getSymbol();

    public Rule<T> getRule();
}
