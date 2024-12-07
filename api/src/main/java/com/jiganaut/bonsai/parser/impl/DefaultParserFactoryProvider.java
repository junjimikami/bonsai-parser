package com.jiganaut.bonsai.parser.impl;

import java.util.Objects;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.ParserFactory;
import com.jiganaut.bonsai.parser.spi.ParserFactoryProvider;

public class DefaultParserFactoryProvider implements ParserFactoryProvider {
    private static final ParserFactoryProvider PROVIDER = new DefaultParserFactoryProvider();

    private DefaultParserFactoryProvider() {
    }

    public static ParserFactoryProvider provider() {
        return PROVIDER;
    }

    @Override
    public ParserFactory createFactory(Grammar grammar) {
        Objects.requireNonNull(grammar, Message.NULL_PARAMETER.format());
        return new DefaultParserFactory(grammar);
    }

    @Override
    public ParserFactory loadFactory(String factoryName) {
        return ServiceLoader.load(ParserFactory.class).stream()
                .filter(p -> p.type().getName().equals(factoryName))
                .map(Provider::get)
                .findFirst()
                .orElseThrow(() -> new ServiceConfigurationError(Message.FACTORY_NOT_FOUND.format(factoryName)));
    }
}
