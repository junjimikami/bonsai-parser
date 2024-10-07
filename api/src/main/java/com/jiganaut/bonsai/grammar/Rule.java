package com.jiganaut.bonsai.grammar;

/**
 * 
 * @author Junji Mikami
 */
public interface Rule {

    public static enum Kind {
        PATTERN,
        SEQUENCE,
        CHOICE,
        REFERENCE,
        QUANTIFIER,
        EMPTY;

        public boolean isComposite() {
            return this == SEQUENCE || this == CHOICE;
        }
    }

    public static interface Builder {
        public Rule build();
    }

    public static final Rule EMPTY = new Rule() {

        @Override
        public Kind getKind() {
            return Kind.EMPTY;
        }

        @Override
        public <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
            return visitor.visitEmpty(this, p);
        }

        @Override
        public String toString() {
            return "empty";
        }
    };

    public <R, P> R accept(RuleVisitor<R, P> visitor, P p);

    public default <R, P> R accept(RuleVisitor<R, P> visitor) {
        return accept(visitor, null);
    }

    public Kind getKind();
}
