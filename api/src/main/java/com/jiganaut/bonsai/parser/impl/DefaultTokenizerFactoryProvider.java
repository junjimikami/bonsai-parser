package com.jiganaut.bonsai.parser.impl;

import java.util.Objects;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.parser.TokenizerFactory;
import com.jiganaut.bonsai.parser.spi.TokenizerFactoryProvider;

public class DefaultTokenizerFactoryProvider implements TokenizerFactoryProvider {
    private static final TokenizerFactoryProvider PROVIDER = new DefaultTokenizerFactoryProvider();

    private DefaultTokenizerFactoryProvider() {
    }

    public static TokenizerFactoryProvider provider() {
        return PROVIDER;
    }

    @Override
    public TokenizerFactory createFactory(ProductionSet productionSet) {
        Objects.requireNonNull(productionSet, Message.NULL_PARAMETER.format());
        return new DefaultTokenizerFactory(productionSet);
    }

    @Override
    public TokenizerFactory loadFactory(String factoryName) {
        return ServiceLoader.load(TokenizerFactory.class).stream()
                .filter(p -> p.type().getCanonicalName().equals(factoryName))
                .map(Provider::get)
                .findFirst()
                .orElseThrow();
    }
}
