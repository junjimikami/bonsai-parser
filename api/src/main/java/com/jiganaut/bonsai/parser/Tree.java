package com.jiganaut.bonsai.parser;

import java.util.Optional;
import java.util.stream.Stream;

/**
 *
 * @author Junji Mikami
 */
public sealed interface Tree permits TerminalNode, NonTerminalNode {

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
    public static interface Builder {
        public Tree build();
    }

    /**
     *
     * @return
     */
    public Kind getKind();

    public String getName();

    public Position getPosition();

    public Stream<? extends Tree> subTrees();

    public Optional<String> value();

    /**
     *
     * @param <R>
     * @param <P>
     * @param v
     * @param p
     * @return
     */
    public <R, P> R accept(TreeVisitor<R, P> v, P p);

    public default <R, P> R accept(TreeVisitor<R, P> v) {
        return accept(v, null);
    }

}
