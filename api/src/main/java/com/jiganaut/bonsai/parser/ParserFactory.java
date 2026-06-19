package com.jiganaut.bonsai.parser;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.spi.ParserProvider;

/**
 * @author Junji Mikami
 *
 */
public interface ParserFactory<T> {

    public static <T> ParserFactory<T> of(Grammar<T> grammar) {
        return ParserProvider.load().createParserFactory(grammar);
    }

    public Parser<T> createParser(Tokenizer<T> tokenizer);

    public default Parser<T> createParser(Source<T> source) {
        return createParser(source.toTokenizer());
    }
}
