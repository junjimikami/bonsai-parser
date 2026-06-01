package com.jiganaut.bonsai.grammar;

import java.util.List;
import java.util.Objects;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;
import com.jiganaut.bonsai.impl.Message;

/**
 * @author Junji Mikami
 *
 */
public interface SequenceRule extends Quantifiable {

    /**
     *
     */
    public static interface Builder extends Quantifiable.Builder, Iterable<Rule.Builder> {
        public default SequenceRule.Builder add(Rule rule) {
            return add(() -> rule);
        }
        public SequenceRule.Builder add(Rule.Builder builder);
        public SequenceRule.Builder addAll(SequenceRule.Builder builder);
        @Override
        public SequenceRule build();
    }

    public static Builder builder() {
        return GrammarProvider.load().createSequenceBuilder();
    }

    @Override
    public default Kind getKind() {
    	return Kind.SEQUENCE;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
        Objects.requireNonNull(visitor, () -> Message.VALIDATION_PARAMETER_NULL.format("visitor"));
        return visitor.visitSequence(this, p);
    }

    public List<? extends Rule> getRules();
}
