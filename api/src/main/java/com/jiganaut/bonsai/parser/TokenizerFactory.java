package com.jiganaut.bonsai.parser;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.impl.TokenizerFactoryProviders;

/**
 * 
 * @author Junji Mikami
 *
 */
public interface TokenizerFactory {

    public static TokenizerFactory newFactory(Grammar grammar) {
        return TokenizerFactoryProviders.provider().createFactory(grammar);
    }
    
    public static TokenizerFactory loadFactory(String factoryName, ClassLoader cl) {
        return TokenizerFactoryProviders.loadFactory(factoryName, cl);
    }

    public static TokenizerFactory loadFactory(String factoryName) {
        return TokenizerFactoryProviders.loadFactory(factoryName, null);
    }

    public Tokenizer createTokenizer(Reader reader);
    public Tokenizer createTokenizer(Tokenizer tokenizer);
}
