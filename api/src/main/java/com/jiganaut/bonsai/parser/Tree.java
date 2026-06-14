package com.jiganaut.bonsai.parser;

import java.util.stream.Stream;

/**
 *
 * @author Junji Mikami
 */
public sealed interface Tree<T> permits TerminalNode, NonTerminalNode, ErrorNode {

    /**
     *
     *
     */
    public static enum Kind {
        TERMINAL,
        NON_TERMINAL,
        ERROR;
    }

    /**
     *
     */
    public static interface Builder<T> {
        public Tree<T> build();
    }

    /**
     *
     * @return
     */
    public Kind getKind();

    public Position getPosition();

    public Stream<Tree<T>> subTrees();

    public Stream<T> values();

    /**
     *
     * @param <R>
     * @param <P>
     * @param visitor
     * @param p
     * @return
     */
    public <R, P> R accept(TreeVisitor<T, R, P> visitor, P p);

    public default <R, P> R accept(TreeVisitor<T, R, P> visitor) {
        return accept(visitor, null);
    }

}
