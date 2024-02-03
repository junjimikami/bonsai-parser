package com.jiganaut.bonsai.parser.impl;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.TokenizerFactory;

public final class TokenizerService {
    private static final DefaultTokenizerProvider provider = new DefaultTokenizerProvider();

    private TokenizerService() {
    }

    public static TokenizerFactory createFactory(Grammar grammar) {
        return provider.createFactory(grammar);
    }

    public static TokenizerFactory loadFactory(String factoryName, ClassLoader cl) {
        return provider.loadFactory(factoryName, cl);
    }
}
