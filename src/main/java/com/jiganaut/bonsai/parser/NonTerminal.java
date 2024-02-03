package com.jiganaut.bonsai.parser;

import java.util.List;

/**
 *
 * @author Junji Mikami
 */
public interface NonTerminal extends Tree {

    @Override
    public default Kind getKind() {
        return Kind.NON_TERMINAL;
    }

    @Override
    public default <R, P> R accept(TreeVisitor<R, P> v, P p) {
        return v.visitNonTerminal(this, p);
    }

    public String getSymbol();
    public List<? extends Tree> getSubTrees();
}
