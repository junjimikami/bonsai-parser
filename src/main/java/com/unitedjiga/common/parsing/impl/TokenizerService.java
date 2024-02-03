package com.unitedjiga.common.parsing.impl;

import com.unitedjiga.common.parsing.TokenizerFactory;
import com.unitedjiga.common.parsing.grammar.Grammar;

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
