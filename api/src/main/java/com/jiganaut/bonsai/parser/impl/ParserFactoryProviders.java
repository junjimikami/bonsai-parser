package com.jiganaut.bonsai.parser.impl;

import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import com.jiganaut.bonsai.parser.ParserFactory;

/**
 * @author Junji Mikami
 *
 */
public final class ParserFactoryProviders {
    private static final ParserFactoryProvider provider = new DefaultParserFactoryProvider();

    private ParserFactoryProviders() {
    }

    public static ParserFactoryProvider provider() {
        return provider;
    }

    public static ParserFactory loadFactory(String factoryName, ClassLoader cl) {
        return ServiceLoader.load(ParserFactory.class, cl).stream()
                .filter(p -> p.type().getCanonicalName().equals(factoryName))
                .map(Provider::get)
                .findFirst()
                .get();
    }
}
