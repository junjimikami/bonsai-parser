package com.jiganaut.bonsai.grammar;

import java.util.stream.Stream;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;

public interface SingleOriginGrammar extends Grammar {

    public static interface Builder extends Grammar.Builder {
        public SingleOriginGrammar.Builder add(String symbol, Rule rule);

        public SingleOriginGrammar.Builder add(String symbol, Rule.Builder builder);

        public SingleOriginGrammar.Builder setStartSymbol(String symbol);

        public SingleOriginGrammar build();
    }

    public static Builder builder() {
        return GrammarProvider.load().createSingleOriginGrammarBuilder();
    }

    public Production getStartProduction();

    @Override
    public default Stream<Production> scope() {
        return Stream.of(getStartProduction());
    }

    @Override
    default <R, P> R accept(GrammarVisitor<R, P> visitor, P p) {
        return visitor.visitSingleOrigin(this, p);
    }
}
