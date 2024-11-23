package com.jiganaut.bonsai.grammar;

import com.jiganaut.bonsai.grammar.impl.GrammarProviders;

public interface ShortCircuitChoiceGrammar extends ChoiceGrammar {

    public static interface Builder extends ChoiceGrammar.Builder {
        ShortCircuitChoiceGrammar.Builder add(String symbol, Rule rule);

        ShortCircuitChoiceGrammar.Builder add(String symbol, Rule.Builder builder);

        ShortCircuitChoiceGrammar build();
    }

    public static Builder builder() {
        return GrammarProviders.provider().createShortCircuitChoiceGrammarBuilder();
    }

    @Override
    default <R, P> R accept(GrammarVisitor<R, P> visitor, P p) {
        return visitor.visitShortCircuitChoice(this, p);
    }
}
