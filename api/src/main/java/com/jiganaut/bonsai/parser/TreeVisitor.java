package com.jiganaut.bonsai.parser;

/**
 * 
 * @author Junji Mikami
 *
 * @param <R>
 * @param <P>
 */
public interface TreeVisitor<R, P> {

    public default R visit(Tree tree) {
        return visit(tree, null);
    }

    public default R visit(Tree tree, P p) {
        return tree.accept(this, p);
    }

    public R visitTerminal(Terminal tree, P p);

    public R visitNonTerminal(NonTerminal tree, P p);
}
