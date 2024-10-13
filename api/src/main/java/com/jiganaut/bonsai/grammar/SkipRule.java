package com.jiganaut.bonsai.grammar;

/**
 * @author Junji Mikami
 *
 */
public interface SkipRule extends Rule {

    @Override
    public default Kind getKind() {
        return Kind.SKIP;
    }

    @Override
    public default <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
        return visitor.visitSkip(this, p);
    }

    public Rule getRule();
}
