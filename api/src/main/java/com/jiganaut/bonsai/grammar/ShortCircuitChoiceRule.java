package com.jiganaut.bonsai.grammar;

import java.util.Set;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;

/**
 * @author Junji Mikami
 *
 */
public interface ShortCircuitChoiceRule extends ChoiceRule {

    public static interface Builder extends ChoiceRule.Builder {
        public ShortCircuitChoiceRule.Builder add(Rule rule);
        public ShortCircuitChoiceRule.Builder add(Rule.Builder builder);
        public ShortCircuitChoiceRule.Builder addEmpty();
        @Override
        public ShortCircuitChoiceRule build();
    }

    public static Builder builder() {
        return GrammarProvider.load().createShortCircuitChoiceBuilder();
    }

    public static ShortCircuitChoiceRule of(Rule... rules) {
        var builder = builder();
        for (var rule : rules) {
            builder.add(rule);
        }
        return builder.build();
    }

    @Override
    public default Kind getKind() {
    	return Kind.SHORT_CIRCUIT_CHOICE;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
        return visitor.visitShortCircuitChoice(this, p);
    }

    public Set<? extends Rule> getChoices();
}
