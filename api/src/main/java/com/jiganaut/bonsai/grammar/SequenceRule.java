package com.jiganaut.bonsai.grammar;

import java.util.List;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;

/**
 * @author Junji Mikami
 *
 */
public interface SequenceRule extends Rule, Quantifiable {

    public static interface Builder extends Rule.Builder, Quantifiable {
        public SequenceRule.Builder add(Rule rule);
        public SequenceRule.Builder add(Rule.Builder builder);
        @Override
        public SequenceRule build();
    }

    public static Builder builder() {
        return GrammarProvider.load().createSequenceBuilder();
    }

    public static SequenceRule of(Rule... rules) {
        var builder = builder();
        for (var rule : rules) {
            builder.add(rule);
        }
        return builder.build();
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
