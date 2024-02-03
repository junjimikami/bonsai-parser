package com.unitedjiga.common.parsing.impl;

import java.util.Collections;
import java.util.List;

import com.unitedjiga.common.parsing.NonTerminal;
import com.unitedjiga.common.parsing.Tree;

/**
 * 
 * @author Junji Mikami
 *
 */
class DefaultNonTerminal implements NonTerminal {
    private final String symbol;
    private final List<? extends Tree> list;

    DefaultNonTerminal(String symbol, List<? extends Tree> list) {
        assert symbol != null;
        assert list != null;
        this.symbol = symbol;
        this.list = list;
    }

    DefaultNonTerminal(String symbol, Tree s) {
        assert symbol != null;
        assert s != null;
        this.symbol = symbol;
        this.list = List.of(s);
    }

    DefaultNonTerminal(String symbol) {
        this(symbol, List.of());
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public List<? extends Tree> getSubTrees() {
        return Collections.unmodifiableList(list);
    }
}
