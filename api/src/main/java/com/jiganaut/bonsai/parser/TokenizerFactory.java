package com.jiganaut.bonsai.parser;

import java.util.stream.Collector;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.spi.ParserProvider;

/**
 *
 * @author Junji Mikami
 *
 */
public interface TokenizerFactory<T, R> {

    public static <T, R> TokenizerFactory<T, R> of(Grammar<T> grammar, Collector<? super T, ?, R> collector) {
        return ParserProvider.load().createTokenizerFactory(grammar, collector);
    }

    public Tokenizer<R> createTokenizer(Tokenizer<T> tokenizer);

    public default Tokenizer<R> createTokenizer(Source<T> source) {
        return createTokenizer(source.toTokenizer());
    }

}
