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
        return tree.accept(this, null);
    }

    public default R visit(Tree tree, P p) {
        return tree.accept(this, p);
    }

    public R visitTerminal(TerminalNode tree, P p);

    public R visitNonTerminal(NonTerminalNode tree, P p);

    public default R visitError(ErrorNode tree, P p) {
        return visitNonTerminal(tree, p);
    }

}
