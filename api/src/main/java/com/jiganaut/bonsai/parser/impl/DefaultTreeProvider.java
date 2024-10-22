package com.jiganaut.bonsai.parser.impl;

import com.jiganaut.bonsai.parser.NonTerminalNode;
import com.jiganaut.bonsai.parser.TerminalNode;
import com.jiganaut.bonsai.parser.spi.TreeProvider;

public class DefaultTreeProvider extends TreeProvider {

    @Override
    public NonTerminalNode.Builder createNonTerminalBuilder() {
        return new DefaultNonTerminalNode.Builder();
    }

    @Override
    public TerminalNode.Builder createTerminalBuilder() {
        return new DefaultToken.Builder();
    }

}
