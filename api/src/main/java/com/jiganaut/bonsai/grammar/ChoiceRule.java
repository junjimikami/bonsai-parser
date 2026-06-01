package com.jiganaut.bonsai.grammar;

import java.util.Objects;
import java.util.Set;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;
import com.jiganaut.bonsai.impl.Message;

/**
 * @author Junji Mikami
 *
 */
public interface ChoiceRule extends Quantifiable, Skippable {

    /**
     *
     */
    public static interface Builder extends Quantifiable.Builder, Skippable.Builder, Iterable<Rule.Builder> {
        public default ChoiceRule.Builder add(Rule rule) {
            return add(() -> rule);
        }
        public ChoiceRule.Builder add(Rule.Builder builder);
        public ChoiceRule.Builder addAll(ChoiceRule.Builder builder);
        public default ChoiceRule.Builder addEmpty() {
            return add(EmptyRule::empty);
        }
        public ChoiceRule.Builder asShortCircuit();
        @Override
        public ChoiceRule build();
    }

    public static Builder builder() {
        return GrammarProvider.load().createChoiceBuilder();
    }

    @Override
    public default Kind getKind() {
    	return Kind.CHOICE;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
        Objects.requireNonNull(visitor, () -> Message.VALIDATION_PARAMETER_NULL.format("visitor"));
        if (isShortCircuit()) {
            return visitor.visitChoiceAsShortCircuit(this, p);
        }
        return visitor.visitChoice(this, p);
    }

    public Set<? extends Rule> getChoices();

    public boolean isShortCircuit();

}
