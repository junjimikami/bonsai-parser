package com.jiganaut.bonsai.parser.impl;

import java.util.Objects;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.ParserFactory;

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
