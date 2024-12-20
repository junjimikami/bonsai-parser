package com.jiganaut.bonsai.grammar;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;

/**
 * @author Junji Mikami
 *
 */
public interface ReferenceRule extends Rule, Quantifiable {

    public static ReferenceRule of(String reference) {
        return GrammarProvider.load().createReference(reference);
    }

    @Override
    public default Kind getKind() {
        return Kind.REFERENCE;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
        return visitor.visitReference(this, p);
    }

    public default ProductionSet lookup(Grammar set) {
        return set.withSymbol(getSymbol());
    }

    public String getSymbol();
}
