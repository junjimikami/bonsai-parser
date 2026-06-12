package com.jiganaut.bonsai.grammar;

import java.util.Objects;
import java.util.Set;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;
import com.jiganaut.bonsai.impl.Message;

/**
 * @author Junji Mikami
 *
 */
public interface ChoiceRule<T> extends Quantifiable<T>, Skippable<T> {

    /**
     *
     */
    public static interface Builder<T> extends Quantifiable.Builder<T>, Skippable.Builder<T>, Iterable<Rule.Builder<T>> {
        public default ChoiceRule.Builder<T> add(Rule<T> rule) {
            return add(() -> rule);
        }
        public ChoiceRule.Builder<T> add(Rule.Builder<T> builder);
        public ChoiceRule.Builder<T> addAll(ChoiceRule.Builder<T> builder);
        public default ChoiceRule.Builder<T> addEmpty() {
            return add(EmptyRule::empty);
        }
        public ChoiceRule.Builder<T> asShortCircuit();
        @Override
        public ChoiceRule<T> build();
    }

    public static <T> Builder<T> builder() {
        return GrammarProvider.load().createChoiceBuilder();
    }

    @Override
    public default Kind getKind() {
    	return Kind.CHOICE;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<T, R, P> visitor, P p) {
        Objects.requireNonNull(visitor, () -> Message.VALIDATION_PARAMETER_NULL.format("visitor"));
        if (isShortCircuit()) {
            return visitor.visitChoiceAsShortCircuit(this, p);
        }
        return visitor.visitChoice(this, p);
    }

    public Set<Rule<T>> getChoices();

    public boolean isShortCircuit();

}
