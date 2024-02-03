package com.unitedjiga.common.parsing.grammar;

import com.unitedjiga.common.parsing.grammar.impl.GrammarProviders;

/**
 * @author Junji Mikami
 *
 */
public interface ReferenceRule extends Rule {

    public static interface Builder extends Rule.Builder, Quantifiable {
        @Override
        public ReferenceRule build(ProductionSet set);
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
    
    public Production getProduction();
}
