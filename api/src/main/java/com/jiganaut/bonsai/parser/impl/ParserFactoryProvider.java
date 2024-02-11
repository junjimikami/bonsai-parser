package com.jiganaut.bonsai.parser.impl;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.ParserFactory;

public interface ParserFactoryProvider {
    public ParserFactory createFactory(Grammar grammar);
}