package com.jiganaut.bonsai.parser;

import java.util.Objects;
import java.util.stream.Stream;

import com.jiganaut.bonsai.impl.Message;

/**
 *
 * @author Junji Mikami
 */
public non-sealed interface TerminalNode<T> extends Tree<T> {

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
    public default <R, P> R accept(TreeVisitor<T, R, P> visitor, P p) {
        Objects.requireNonNull(visitor, () -> Message.VALIDATION_PARAMETER_NULL.format("visitor"));
        return visitor.visitTerminal(this, p);
    }

}
