package com.jiganaut.bonsai.parser.impl;

import java.util.Objects;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.TokenizerFactory;

class DefaultTokenizerProvider {

    TokenizerFactory createFactory(Grammar grammar) {
        Objects.requireNonNull(grammar, Message.NULL_PARAMETER.format());
        return new DefaultTokenizerFactory(grammar);
    }

    TokenizerFactory loadFactory(String factoryName, ClassLoader cl) {
        return ServiceLoader.load(TokenizerFactory.class, cl).stream()
                .filter(p -> p.type().getCanonicalName().equals(factoryName))
                .map(Provider::get)
                .findFirst()
                .orElseThrow();
    }

}
