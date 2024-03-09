package com.jiganaut.bonsai.parser;

@FunctionalInterface
public interface TerminalVisitor<R, P> extends TreeVisitor<R, P> {

    @Override
    default R visitNonTerminal(NonTerminal tree, P p) {
        return visitTerminal(tree.asTerminal(), p);
    }
}
