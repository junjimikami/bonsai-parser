package com.jiganaut.bonsai.parser;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.impl.ParserService;

/**
 * @author Junji Mikami
 *
 */
public interface ParserFactory {

    public static ParserFactory newFactory(Grammar grammar) {
        return ParserService.createFactory(grammar);
    }

    public static ParserFactory loadFactory(String factoryName, ClassLoader cl) {
        return ParserService.loadFactory(factoryName, cl);
    }

    public static ParserFactory loadFactory(String factoryName) {
        return ParserService.loadFactory(factoryName, null);
    }

    public Parser createParser(Tokenizer tokenizer);

    public Parser createParser(Reader reader);
}
