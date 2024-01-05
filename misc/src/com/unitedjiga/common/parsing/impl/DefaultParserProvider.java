package com.unitedjiga.common.parsing.impl;

import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import com.unitedjiga.common.parsing.ParserFactory;
import com.unitedjiga.common.parsing.grammar.Grammar;

class DefaultParserProvider {

    ParserFactory createFacotry(Grammar grammar) {
        return new DefaultParserFactory(grammar);
    }

    ParserFactory loadFacotry(String factoryName, ClassLoader cl) {
        return ServiceLoader.load(ParserFactory.class, cl).stream()
                .filter(p -> p.type().getCanonicalName().equals(factoryName))
                .map(Provider::get)
                .findFirst()
                .get();
    }
}
