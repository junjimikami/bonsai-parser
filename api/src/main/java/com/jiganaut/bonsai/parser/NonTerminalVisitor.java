package com.jiganaut.bonsai.parser;

@FunctionalInterface
public interface NonTerminalVisitor<R, P> extends TreeVisitor<R, P> {

    @Override
    default R visitTerminal(Terminal tree, P p) {
        return visitNonTerminal(tree.asNonTerminal(), p);
    }
}
