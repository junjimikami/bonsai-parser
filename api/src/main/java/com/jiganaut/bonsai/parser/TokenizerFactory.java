package com.jiganaut.bonsai.parser;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.spi.ParserProvider;

/**
 * 
 * @author Junji Mikami
 *
 */
public interface TokenizerFactory {

    public static TokenizerFactory of(Grammar grammar) {
        return ParserProvider.load().createTokenizerFactory(grammar);
    }

    public static TokenizerFactory load(String factoryName) {
        return ParserProvider.load().loadTokenizerFactory(factoryName);
    }

    public static TokenizerFactory load(Class<?> factoryClass) {
        return load(factoryClass.getName());
    }

    public Tokenizer createTokenizer(Reader reader);
    public Tokenizer createTokenizer(Tokenizer tokenizer);
}
