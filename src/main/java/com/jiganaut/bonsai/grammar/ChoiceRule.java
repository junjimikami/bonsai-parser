package com.jiganaut.bonsai.grammar;

import java.util.List;

import com.jiganaut.bonsai.grammar.impl.GrammarProviders;

/**
 * @author Junji Mikami
 *
 */
public interface ChoiceRule extends Rule {

    public static interface Builder extends Rule.Builder, Quantifiable {
        public ChoiceRule.Builder add(Rule.Builder builder);
        public ChoiceRule.Builder add(String reference);
        public ChoiceRule.Builder addEmpty();
        @Override
        public ChoiceRule build(ProductionSet set);
    }

    public static Builder builder() {
        return GrammarProviders.provider().createChoiceBuilder();
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
