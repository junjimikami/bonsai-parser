package com.jiganaut.bonsai.grammar.impl;

import com.jiganaut.bonsai.grammar.spi.GrammarProvider;

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
