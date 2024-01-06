package com.unitedjiga.common.parsing.impl;

import java.util.Objects;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import com.unitedjiga.common.parsing.TokenizerFactory;
import com.unitedjiga.common.parsing.grammar.Grammar;

class DefaultTokenizerProvider {

    TokenizerFactory createFactory(Grammar grammar) {
        Objects.requireNonNull(grammar, Message.NON_NULL_REQUIRED.format());
        return new DefaultTokenizerFactory(grammar);
    }

    TokenizerFactory loadFactory(String factoryName, ClassLoader cl) {
        return ServiceLoader.load(TokenizerFactory.class, cl).stream()
                .filter(p -> p.type().getCanonicalName().equals(factoryName))
                .map(Provider::get)
                .findFirst()
                .get();
    }

}
