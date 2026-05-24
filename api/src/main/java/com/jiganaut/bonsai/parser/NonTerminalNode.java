package com.jiganaut.bonsai.parser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.jiganaut.bonsai.parser.spi.ParserProvider;

/**
 *
 * @author Junji Mikami
 */
public non-sealed interface NonTerminalNode extends Tree {

    /**
     *
     */
    public static interface Builder extends Tree.Builder {

        @Override
        public NonTerminalNode build();

        public NonTerminalNode.Builder add(Tree tree);

        public NonTerminalNode.Builder add(Tree.Builder builder);

        public NonTerminalNode.Builder addAll(NonTerminalNode.Builder builder);

    }

    public static NonTerminalNode.Builder builder(String name) {
        return ParserProvider.load().createNonTerminalNodeBuilder(name);
    }

    public static NonTerminalNode.Builder builder() {
        return ParserProvider.load().createNonTerminalNodeBuilder(null);
    }

    @Override
    public default Kind getKind() {
        return Kind.NON_TERMINAL;
    }

    @Override
    public default Position getPosition() {
        return getSubTrees().stream()
                .limit(1)
                .map(Tree::getPosition)
                .findFirst()
                .orElse(Position.UNKNOWN);
    }

    @Override
    public default Stream<? extends Tree> subTrees() {
        return getSubTrees().stream();
    }

    @Override
    public default Optional<String> value() {
        return Optional.empty();
    }

    @Override
    public default <R, P> R accept(TreeVisitor<R, P> v, P p) {
        return v.visitNonTerminal(this, p);
    }

    public List<? extends Tree> getSubTrees();

}
