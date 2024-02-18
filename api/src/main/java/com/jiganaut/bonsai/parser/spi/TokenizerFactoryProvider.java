package com.jiganaut.bonsai.parser.spi;

import java.io.Reader;
import java.util.ServiceLoader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.TokenizerFactory;
import com.jiganaut.bonsai.parser.impl.DefaultTokenizerFactoryProvider;

public interface TokenizerFactoryProvider {
    public static TokenizerFactoryProvider provider() {
        return ServiceLoader.load(TokenizerFactoryProvider.class)
                .findFirst()
                .orElseGet(DefaultTokenizerFactoryProvider::provider);
    }
    public TokenizerFactory createFactory(Grammar grammar);
    public TokenizerFactory loadFactory(String factoryName);

    public default Tokenizer createTokenizer(Grammar grammar, Reader reader) {
        return createFactory(grammar).createTokenizer(reader);
    }
    public default Tokenizer createTokenizer(Grammar grammar, Tokenizer tokenizer) {
        return createFactory(grammar).createTokenizer(tokenizer);
    }
}