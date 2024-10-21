package com.jiganaut.bonsai.parser;

import java.util.List;

/**
 *
 * @author Junji Mikami
 */
public interface NonTerminalNode extends Tree {

    @Override
    public default Kind getKind() {
        return Kind.NON_TERMINAL;
    }

    @Override
    public default <R, P> R accept(TreeVisitor<R, P> v, P p) {
        return v.visitNonTerminal(this, p);
    }

    public String getName();
    public List<? extends Tree> getSubTrees();

    public default List<NonTerminalNode> getNonTerminals(String symbol) {
        return getSubTrees().stream()
                .filter(e -> e.getKind().isNonTerminal())
                .map(e -> e.asNonTerminal())
                .filter(e -> e.getName().equals(symbol))
                .toList();
    }
    public default NonTerminalNode getNonTerminal(String symbol, int index) {
        return getNonTerminals(symbol).get(index);
    }
    public default NonTerminalNode getNonTerminal(int index) {
        return getSubTrees().get(index).asNonTerminal();
    }
    public default TerminalNode getTerminal(int index) {
        return getSubTrees().get(index).asTerminal();
    }
}
