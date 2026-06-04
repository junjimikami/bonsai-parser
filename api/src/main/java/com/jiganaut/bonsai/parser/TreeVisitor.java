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

    public R visitTerminal(TerminalNode<T> tree, P p);

    public R visitNonTerminal(NonTerminalNode<T> tree, P p);

    public default R visitError(ErrorNode<T> tree, P p) {
        return visitNonTerminal(tree, p);
    }

}
