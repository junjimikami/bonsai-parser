package com.jiganaut.bonsai.parser;

/**
 *
 * @author Junji Mikami
 *
 * @param <R>
 * @param <P>
 */
public interface TreeVisitor<T, R, P> {

    public default R visit(Tree<T> tree) {
        return tree.accept(this, null);
    }

    public default R visit(Tree<T> tree, P p) {
        return tree.accept(this, p);
    }

    public R visitTerminal(TerminalNode<T> terminal, P p);

    public R visitNonTerminal(NonTerminalNode<T> nonTerminal, P p);

    public R visitError(ErrorNode<T> error, P p);

}
