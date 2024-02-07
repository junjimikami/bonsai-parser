package com.jiganaut.bonsai.parser.impl;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.TokenizerFactory;

public interface TokenizerFactoryProvider {
    public TokenizerFactory createFactory(Grammar grammar);
}