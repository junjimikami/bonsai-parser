package com.jiganaut.bonsai.grammar;

import com.jiganaut.bonsai.grammar.impl.GrammarProviders;

/**
 * @author Junji Mikami
 *
 */
public interface ReferenceRule extends Rule, Quantifiable {

    public static ReferenceRule of(String reference) {
        return GrammarProviders.provider().createReference(reference);
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
