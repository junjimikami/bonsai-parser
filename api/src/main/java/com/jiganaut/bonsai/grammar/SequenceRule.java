package com.jiganaut.bonsai.grammar;

import java.util.List;

import com.jiganaut.bonsai.grammar.impl.GrammarProviders;

/**
 * @author Junji Mikami
 *
 */
public interface SequenceRule extends Rule {

    public static interface Builder extends Rule.Builder, Quantifiable {
        public SequenceRule.Builder add(Rule.Builder builder);
        @Override
        public SequenceRule build();
    }

    public static Builder builder() {
        return GrammarProviders.provider().createSequenceBuilder();
    }

    @Override
    public default Kind getKind() {
    	return Kind.SEQUENCE;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
        return visitor.visitSequence(this, p);
    }

    public List<? extends Rule> getRules();
}
