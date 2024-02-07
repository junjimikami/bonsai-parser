package com.jiganaut.bonsai.parser.impl;

import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import com.jiganaut.bonsai.parser.TokenizerFactory;

public final class TokenizerFactoryProviders {
    private static final TokenizerFactoryProvider provider = new DefaultTokenizerFactoryProvider();

    private TokenizerFactoryProviders() {
    }

    public static TokenizerFactoryProvider provider() {
        return provider;
    }

    public static TokenizerFactory loadFactory(String factoryName, ClassLoader cl) {
        return ServiceLoader.load(TokenizerFactory.class, cl).stream()
                .filter(p -> p.type().getCanonicalName().equals(factoryName))
                .map(Provider::get)
                .findFirst()
                .orElseThrow();
    }

}
