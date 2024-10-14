package com.jiganaut.bonsai.parser.spi;

import java.util.ServiceLoader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.ParserFactory;
import com.jiganaut.bonsai.parser.impl.DefaultParserFactoryProvider;

public interface ParserFactoryProvider {
    public static ParserFactoryProvider provider() {
        return ServiceLoader.load(ParserFactoryProvider.class)
                .findFirst()
                .orElseGet(DefaultParserFactoryProvider::provider);
    }
    public ParserFactory createFactory(Grammar grammar);
    public ParserFactory loadFactory(String factoryName);

}