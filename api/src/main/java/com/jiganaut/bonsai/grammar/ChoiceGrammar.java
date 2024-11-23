package com.jiganaut.bonsai.grammar;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;

public interface ChoiceGrammar extends Grammar {

    public static interface Builder extends Grammar.Builder {
        ChoiceGrammar.Builder add(String symbol, Rule rule);

        ChoiceGrammar.Builder add(String symbol, Rule.Builder builder);

        ChoiceGrammar build();
    }

    public static Builder builder() {
        return GrammarProvider.load().createChoiceGrammarBuilder();
    }

    @Override
    default <R, P> R accept(GrammarVisitor<R, P> visitor, P p) {
        return visitor.visitChoice(this, p);
    }
}
