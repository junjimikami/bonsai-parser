package com.jiganaut.bonsai.grammar;

/**
 *
 * @author Junji Mikami
 */
public interface Rule<T> {

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
    public static interface Builder<T> {
        public Rule<T> build();
    }

    public <R, P> R accept(RuleVisitor<T, R, P> visitor, P p);

    public default <R, P> R accept(RuleVisitor<T, R, P> visitor) {
        return accept(visitor, null);
    }

    public Kind getKind();
}
