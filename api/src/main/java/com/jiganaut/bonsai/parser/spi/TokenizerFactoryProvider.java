package com.jiganaut.bonsai.parser.spi;

import java.util.ServiceLoader;

import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.parser.TokenizerFactory;
import com.jiganaut.bonsai.parser.impl.DefaultTokenizerFactoryProvider;

public interface TokenizerFactoryProvider {
    public static TokenizerFactoryProvider provider() {
        return ServiceLoader.load(TokenizerFactoryProvider.class)
                .findFirst()
                .orElseGet(DefaultTokenizerFactoryProvider::provider);
    }
    public TokenizerFactory createFactory(ProductionSet productionSet);
    public TokenizerFactory loadFactory(String factoryName);

}