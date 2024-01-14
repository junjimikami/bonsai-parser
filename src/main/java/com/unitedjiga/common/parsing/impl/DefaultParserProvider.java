package com.unitedjiga.common.parsing.impl;

import java.util.Objects;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import com.unitedjiga.common.parsing.ParserFactory;
import com.unitedjiga.common.parsing.grammar.Grammar;

class DefaultParserProvider {

    ParserFactory createFacotry(Grammar grammar) {
        Objects.requireNonNull(grammar, Message.NULL_PARAMETER.format());
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
