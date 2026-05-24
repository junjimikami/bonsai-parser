package com.jiganaut.bonsai.parser;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.spi.ParserProvider;

/**
 * @author Junji Mikami
 *
 */
public interface ParserFactory {

    public static ParserFactory of(Grammar grammar) {
        return ParserProvider.load().createParserFactory(grammar);
    }

    public Parser createParser(Tokenizer tokenizer);

    public default Parser createParser(Source source) {
        return createParser(source.toTokenizer());
    }
}
