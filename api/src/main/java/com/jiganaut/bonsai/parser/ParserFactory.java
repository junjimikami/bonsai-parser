package com.jiganaut.bonsai.parser;

import java.io.Reader;

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

    public static ParserFactory load(String factoryName) {
        return ParserProvider.load().loadParserFactory(factoryName);
    }

    public static ParserFactory load(Class<?> factoryClass) {
        return load(factoryClass.getName());
    }

    public Parser createParser(Tokenizer tokenizer);

    public Parser createParser(Reader reader);
}
