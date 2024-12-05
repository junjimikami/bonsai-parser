package com.jiganaut.bonsai.grammar;

import java.util.Set;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;

/**
 * @author Junji Mikami
 *
 */
public interface ChoiceRule extends Rule, Quantifiable {

    public static interface Builder extends Rule.Builder, Quantifiable {
        public ChoiceRule.Builder add(Rule rule);
        public ChoiceRule.Builder add(Rule.Builder builder);
        public default ChoiceRule.Builder addEmpty() {
            return add(EMPTY);
        }
        @Override
        public ChoiceRule build();
        public default ChoiceRule shortCircuit() {
            return build().shortCircuit();
        }
    }

    public static Builder builder() {
        return GrammarProvider.load().createChoiceBuilder();
    }

    public static ChoiceRule of(Rule... rules) {
        var builder = builder();
        for (var rule : rules) {
            builder.add(rule);
        }
        return builder.build();
    }

    @Override
    public default Kind getKind() {
    	return Kind.CHOICE;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
        return visitor.visitChoice(this, p);
    }

    public Set<? extends Rule> getChoices();

    public boolean isShortCircuit();
    
    public ChoiceRule shortCircuit();
}
