package com.jiganaut.bonsai.grammar;

import java.util.Objects;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;
import com.jiganaut.bonsai.impl.Message;

/**
 * @author Junji Mikami
 *
 */
public interface SkipRule<T> extends Rule<T> {

    public static interface Builder<T> extends Rule.Builder<T> {

        @Override
        public SkipRule<T> build();

    }

    public static <T> SkipRule<T> of(Rule<T> rule) {
        return GrammarProvider.load().createSkip(rule);
    }

    @Override
    public default Kind getKind() {
        return Kind.SKIP;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<T, R, P> visitor, P p) {
        Objects.requireNonNull(visitor, () -> Message.VALIDATION_PARAMETER_NULL.format("visitor"));
        return visitor.visitSkip(this, p);
    }

    public Rule<T> getRule();
}
