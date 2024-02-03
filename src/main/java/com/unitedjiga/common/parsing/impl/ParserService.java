package com.unitedjiga.common.parsing.impl;

import com.unitedjiga.common.parsing.ParserFactory;
import com.unitedjiga.common.parsing.grammar.Grammar;

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
