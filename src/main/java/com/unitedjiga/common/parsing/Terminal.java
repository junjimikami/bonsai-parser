package com.unitedjiga.common.parsing;

/**
 *
 * @author Junji Mikami
 */
public interface Terminal extends Tree {

    @Override
    public default Kind getKind() {
        return Kind.TERMINAL;
    }

    public default <R, P> R accept(TreeVisitor<R, P> v, P p) {
        return v.visitTerminal(this, p);
    }

    public String getValue();
}
