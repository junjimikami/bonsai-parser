package com.jiganaut.bonsai.parser.impl;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.ParserFactory;

/**
 * @author Junji Mikami
 *
 */
public final class ParserService {
    private static final DefaultParserProvider provider = new DefaultParserProvider();

    private ParserService() {
    }

    public static ParserFactory createFactory(Grammar grammar) {
        return provider.createFacotry(grammar);
    }

    public static ParserFactory loadFactory(String factoryName, ClassLoader cl) {
        return provider.loadFacotry(factoryName, cl);
    }
}
