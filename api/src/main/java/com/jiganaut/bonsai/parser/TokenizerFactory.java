package com.jiganaut.bonsai.parser;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.spi.TokenizerFactoryProvider;

/**
 * 
 * @author Junji Mikami
 *
 */
public interface TokenizerFactory {

    public static TokenizerFactory of(Grammar grammar) {
        return TokenizerFactoryProvider.provider().createFactory(grammar);
    }

    public static TokenizerFactory load(String factoryName) {
        return TokenizerFactoryProvider.provider().loadFactory(factoryName);
    }

    public static TokenizerFactory load(Class<?> factoryClass) {
        return load(factoryClass.getName());
    }

    public Tokenizer createTokenizer(Reader reader);
    public Tokenizer createTokenizer(Tokenizer tokenizer);
}
