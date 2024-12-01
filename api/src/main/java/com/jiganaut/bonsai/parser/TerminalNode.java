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
        public TerminalNode.Builder setValue(String value);

        @Override
        public TerminalNode build();

    }

    public static TerminalNode.Builder builder() {
        return TreeProvider.load().createTerminalBuilder();
    }

    public static TerminalNode of(String name, String value) {
        return builder()
                .setName(name)
                .setValue(value)
                .build();
    }

    public static TerminalNode ofUnnamed(String value) {
        return builder()
                .setValue(value)
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
