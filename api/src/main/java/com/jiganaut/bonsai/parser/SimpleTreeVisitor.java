package com.jiganaut.bonsai.parser;

/**
 *
 * @author Junji Mikami
 *
 * @param <R>
 * @param <P>
 */
public interface SimpleTreeVisitor<T, R, P> extends TreeVisitor<T, R, P> {

    @Override
    public default R visitTerminal(TerminalNode<T> terminal, P p) {
        return defaultAction(terminal, p);
    }

    @Override
    public default R visitNonTerminal(NonTerminalNode<T> nonTerminal, P p) {
        return defaultAction(nonTerminal, p);
    }

    @Override
    public default R visitError(ErrorNode<T> error, P p) {
        return defaultAction(error, p);
    }

    public R defaultAction(Tree<T> tree, P p);

}
