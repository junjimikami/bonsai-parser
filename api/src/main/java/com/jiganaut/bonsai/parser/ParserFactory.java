package com.jiganaut.bonsai.parser;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.spi.ParserFactoryProvider;

/**
 * @author Junji Mikami
 *
 */
public interface ParserFactory {

    public static ParserFactory newFactory(Grammar grammar) {
        return ParserFactoryProvider.provider().createFactory(grammar);
    }

    public static ParserFactory loadFactory(String factoryName) {
        return ParserFactoryProvider.provider().loadFactory(factoryName);
    }

    public Parser createParser(Tokenizer tokenizer);

    public Parser createParser(Reader reader);
}
