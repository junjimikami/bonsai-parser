package com.jiganaut.bonsai.parser;

import java.util.stream.Collector;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.spi.ParserProvider;

/**
 * 
 * @author Junji Mikami
 *
 */
public interface TokenizerFactory {

    public static TokenizerFactory of(Grammar grammar, Collector<CharSequence, ?, String> collector) {
        return ParserProvider.load().createTokenizerFactory(grammar, collector);
    }

    public Tokenizer createTokenizer(Tokenizer tokenizer);

    public default Tokenizer createTokenizer(Source source) {
        return createTokenizer(source.toTokenizer());
    }
}
