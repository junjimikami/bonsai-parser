package com.jiganaut.bonsai.parser.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.parser.NonTerminalNode;
import com.jiganaut.bonsai.parser.Tree;

/**
 * 
 * @author Junji Mikami
 *
 */
class DefaultNonTerminalNode implements NonTerminalNode {
    private final String symbol;
    private final List<? extends Tree> list;

    DefaultNonTerminalNode(String symbol, List<? extends Tree> list) {
        assert symbol != null;
        assert list != null;
        this.symbol = symbol;
        this.list = list;
    }

    @Override
    public String getName() {
        return symbol;
    }

    @Override
    public List<? extends Tree> getSubTrees() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public String toString() {
        return list.stream()
                .map(Tree::toString)
                .collect(Collectors.joining());
    }
}
