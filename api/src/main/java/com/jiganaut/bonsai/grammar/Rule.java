package com.jiganaut.bonsai.grammar;

/**
 *
 * @author Junji Mikami
 */
public interface Rule {

    /**
     *
     */
    public static enum Kind {
        MATCH,
        SEQUENCE,
        CHOICE,
        REFERENCE,
        QUANTIFIER,
        SKIP,
        EMPTY,
        PRODUCTION;

        public boolean isComposite() {
            return this == SEQUENCE || this == CHOICE;
        }
    }

    /**
     *
     */
    public static interface Builder {
        public Rule build();
    }

    public <R, P> R accept(RuleVisitor<R, P> visitor, P p);

    public default <R, P> R accept(RuleVisitor<R, P> visitor) {
        return accept(visitor, null);
    }

    public Kind getKind();
}
