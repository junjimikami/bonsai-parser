package com.jiganaut.bonsai.grammar;

import com.jiganaut.bonsai.grammar.impl.GrammarProviders;

/**
 * @author Junji Mikami
 *
 */
public interface ReferenceRule extends Rule {

    public static interface Builder extends Rule.Builder, Quantifiable {
        @Override
        public ReferenceRule build();
    }

    public static Builder builder(String reference) {
        return GrammarProviders.provider().createReferenceBuilder(reference);
    }

    @Override
    public default Kind getKind() {
        return Kind.REFERENCE;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
        return visitor.visitReference(this, p);
    }

    public default Production getProduction(ProductionSet set) {
        return set.get(getSymbol());
    }

    public String getSymbol();
}
