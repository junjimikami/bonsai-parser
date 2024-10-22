package com.jiganaut.bonsai.parser;

import java.util.List;

import com.jiganaut.bonsai.parser.spi.TreeProvider;

/**
 *
 * @author Junji Mikami
 */
public interface TerminalNode extends Tree {

    public static interface Builder extends Tree.Builder {

        @Override
        public TerminalNode.Builder setName(String name);

        @Override
        public TerminalNode.Builder setValue(String name);

        @Override
        public TerminalNode build();

    }

    public static TerminalNode.Builder builder() {
        return TreeProvider.provider().createTerminalBuilder();
    }

    public static TerminalNode of(String name, String value) {
        return builder()
                .setName(name)
                .setValue(name)
                .build();
    }

    @Override
    public default Kind getKind() {
        return Kind.TERMINAL;
    }

    @Override
    public default List<? extends Tree> getSubTrees() {
        return List.of();
    }

    @Override
    public default <R, P> R accept(TreeVisitor<R, P> v, P p) {
        return v.visitTerminal(this, p);
    }

}
