package com.jiganaut.bonsai.grammar;

import java.util.List;
import java.util.Objects;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;
import com.jiganaut.bonsai.impl.Message;

/**
 * @author Junji Mikami
 *
 */
public interface SequenceRule<T> extends Quantifiable<T> {

    /**
     *
     */
    public static interface Builder<T> extends Quantifiable.Builder<T>, Iterable<Rule.Builder<T>> {
        public default SequenceRule.Builder<T> add(Rule<T> rule) {
            return add(() -> rule);
        }
        public SequenceRule.Builder<T> add(Rule.Builder<T> builder);
        public SequenceRule.Builder<T> addAll(SequenceRule.Builder<T> builder);
        @Override
        public SequenceRule<T> build();
    }

    public static <T> Builder<T> builder() {
        return GrammarProvider.load().createSequenceBuilder();
    }

    @Override
    public default Kind getKind() {
    	return Kind.SEQUENCE;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<T, R, P> visitor, P p) {
        Objects.requireNonNull(visitor, () -> Message.VALIDATION_PARAMETER_NULL.format("visitor"));
        return visitor.visitSequence(this, p);
    }

    public List<Rule<T>> getRules();
}
