package com.jiganaut.bonsai.parser;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.parser.spi.TokenizerFactoryProvider;

/**
 * 
 * @author Junji Mikami
 *
 */
public interface TokenizerFactory {

    public static TokenizerFactory of(ProductionSet productionSet) {
        return TokenizerFactoryProvider.provider().createFactory(productionSet);
    }

    public static TokenizerFactory load(String factoryName) {
        return TokenizerFactoryProvider.provider().loadFactory(factoryName);
    }

    public Tokenizer createTokenizer(Reader reader);
    public Tokenizer createTokenizer(Tokenizer tokenizer);
}
