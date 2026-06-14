package com.jiganaut.bonsai.parser;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.spi.ParserProvider;

/**
 *
 * @author Junji Mikami
 */
public non-sealed interface NonTerminalNode<T> extends Tree<T> {

    /**
     *
     */
    public static interface Builder<T> extends Tree.Builder<T>, Iterable<Tree.Builder<T>> {

        @Override
        public NonTerminalNode<T> build();

        public default NonTerminalNode.Builder<T> add(Tree<T> tree) {
            return add(() -> tree);
        }

        public NonTerminalNode.Builder<T> add(Tree.Builder<T> builder);

        public NonTerminalNode.Builder<T> addAll(NonTerminalNode.Builder<T> builder);

    }

    public static <T> NonTerminalNode.Builder<T> builder(String name) {
        return ParserProvider.load().createNonTerminalNodeBuilder(name);
    }

    @Override
    public default Kind getKind() {
        return Kind.NON_TERMINAL;
    }

    @Override
    public default Position getPosition() {
        if (getSubTrees().isEmpty()) {
            return Position.UNKNOWN;
        }
        var start = getSubTrees().getFirst().getPosition();
        var end = getSubTrees().getLast().getPosition();
        return Position.rangeOf(start, end);
    }

    @Override
    public default Stream<Tree<T>> subTrees() {
        return getSubTrees().stream();
    }

    @Override
    public default Stream<T> values() {
        return getSubTrees().stream().flatMap(Tree::values);
    }

    @Override
    public default <R, P> R accept(TreeVisitor<T, R, P> visitor, P p) {
        Objects.requireNonNull(visitor, () -> Message.VALIDATION_PARAMETER_NULL.format("visitor"));
        return visitor.visitNonTerminal(this, p);
    }

    public List<Tree<T>> getSubTrees();

    public String getName();

}
