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

    public static TokenizerFactory newFactory(Grammar grammar) {
        return TokenizerFactoryProvider.provider().createFactory(grammar);
    }

    public static TokenizerFactory loadFactory(String factoryName) {
        return TokenizerFactoryProvider.provider().loadFactory(factoryName);
    }

    public Tokenizer createTokenizer(Reader reader);
    public Tokenizer createTokenizer(Tokenizer tokenizer);
}
