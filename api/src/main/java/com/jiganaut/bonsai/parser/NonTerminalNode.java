package com.jiganaut.bonsai.parser;

import com.jiganaut.bonsai.parser.spi.ParserProvider;

/**
 *
 * @author Junji Mikami
 */
public interface NonTerminalNode extends Tree {

    /**
     * 
     * @author Junji Mikami
     */
    public static interface Builder extends Tree.Builder {

        @Override
        public NonTerminalNode.Builder setName(String name);

        @Override
        public NonTerminalNode.Builder setValue(String value);

        @Override
        public NonTerminalNode build();

        public NonTerminalNode.Builder add(Tree tree);

        public default NonTerminalNode.Builder addAll(NonTerminalNode.Builder builder) {
            builder.build().getSubTrees().forEach(this::add);
            return this;
        }

    }

    public static NonTerminalNode.Builder builder() {
        return ParserProvider.load().createNonTerminalNodeBuilder();
    }

    public static NonTerminalNode of(String name, String value, Tree... trees) {
        var builder = builder()
                .setName(name)
                .setValue(value);
        for (var tree : trees) {
            builder.add(tree);
        }
        return builder.build();
    }

    public static NonTerminalNode of(String name, Tree... trees) {
        var builder = builder()
                .setName(name);
        for (var tree : trees) {
            builder.add(tree);
        }
        return builder.build();
    }

    @Override
    public default Kind getKind() {
        return Kind.NON_TERMINAL;
    }

    @Override
    public default <R, P> R accept(TreeVisitor<R, P> v, P p) {
        return v.visitNonTerminal(this, p);
    }

}
