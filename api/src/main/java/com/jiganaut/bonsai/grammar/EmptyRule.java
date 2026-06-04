package com.jiganaut.bonsai.grammar;

import java.util.Objects;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;
import com.jiganaut.bonsai.impl.Message;

/**
 * @author Junji Mikami
 *
 */
public interface EmptyRule<T> extends Rule<T> {

    public static <T> EmptyRule<T> empty() {
        return GrammarProvider.load().createEmpty();
    }

    @Override
    public default Kind getKind() {
        return Kind.EMPTY;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<T, R, P> visitor, P p) {
        Objects.requireNonNull(visitor, () -> Message.VALIDATION_PARAMETER_NULL.format("visitor"));
        return visitor.visitEmpty(this, p);
    }

}
