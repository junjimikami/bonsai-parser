package com.jiganaut.bonsai.parser;

import java.util.Optional;
import java.util.stream.Stream;

/**
 *
 * @author Junji Mikami
 */
public non-sealed interface TerminalNode extends Tree {

    public String getValue();

    @Override
    public default Kind getKind() {
        return Kind.TERMINAL;
    }

    @Override
    public default Stream<? extends Tree> subTrees() {
        return Stream.empty();
    }

    @Override
    public default Optional<String> value() {
        return Optional.ofNullable(getValue());
    }

    @Override
    public default <R, P> R accept(TreeVisitor<R, P> v, P p) {
        return v.visitTerminal(this, p);
    }

}
