package com.unitedjiga.common.parsing.grammar.impl;

import com.unitedjiga.common.parsing.grammar.spi.GrammarProvider;

/**
 * 
 * @author Junji Mikami
 *
 */
public final class GrammarProviders {
    private static final DefaultGrammarProvider provider = new DefaultGrammarProvider();

    private GrammarProviders() {
    }

    public static GrammarProvider provider() {
        return provider;
    }

}
