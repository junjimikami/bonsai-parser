package com.jiganaut.bonsai.parser;

import java.util.List;

/**
 *
 * @author Junji Mikami
 */
public interface NonTerminal extends Tree {

    @Override
    public default Kind getKind() {
        return Kind.NON_TERMINAL;
    }

    @Override
    public default <R, P> R accept(TreeVisitor<R, P> v, P p) {
        return v.visitNonTerminal(this, p);
    }

    public String getSymbol();
    public List<? extends Tree> getSubTrees();

    public default List<NonTerminal> getNonTerminals(String symbol) {
        return getSubTrees().stream()
                .filter(e -> e.getKind().isNonTerminal())
                .map(e -> e.asNonTerminal())
                .filter(e -> e.getSymbol().equals(symbol))
                .toList();
    }
    public default NonTerminal getNonTerminal(String symbol, int index) {
        return getNonTerminals(symbol).get(index);
    }
    public default NonTerminal getNonTerminal(int index) {
        return getSubTrees().get(index).asNonTerminal();
    }
    public default Terminal getTerminal(int index) {
        return getSubTrees().get(index).asTerminal();
    }
}
