package com.jiganaut.bonsai.parser;

import java.util.stream.Stream;

/**
 *
 * @author Junji Mikami
 */
public non-sealed interface TerminalNode<T> extends Tree<T> {

    public String getName();

    public T getValue();

    @Override
    public default Kind getKind() {
        return Kind.TERMINAL;
    }

    @Override
    public default Stream<Tree<T>> subTrees() {
        return Stream.empty();
    }

    @Override
    public default Stream<T> values() {
        return Stream.of(getValue());
    }

    @Override
    public default <R, P> R accept(TreeVisitor<T, R, P> v, P p) {
        return v.visitTerminal(this, p);
    }

}
