package com.jiganaut.bonsai.grammar;

import java.util.List;

import com.jiganaut.bonsai.grammar.impl.GrammarProviders;

/**
 * @author Junji Mikami
 *
 */
public interface ChoiceRule extends Rule, Quantifiable {

    public static interface Builder extends Rule.Builder, Quantifiable {
        public ChoiceRule.Builder add(Rule rule);
        public ChoiceRule.Builder add(Rule.Builder builder);
        public ChoiceRule.Builder addEmpty();
        @Override
        public ChoiceRule build();
    }

    public static Builder builder() {
        return GrammarProviders.provider().createChoiceBuilder();
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

    public List<? extends Rule> getChoices();
}
