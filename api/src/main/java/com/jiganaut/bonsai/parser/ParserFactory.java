package com.jiganaut.bonsai.parser;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.impl.ParserFactoryProviders;

/**
 * @author Junji Mikami
 *
 */
public interface ParserFactory {

    public static ParserFactory newFactory(Grammar grammar) {
        return ParserFactoryProviders.provider().createFactory(grammar);
    }

    public static ParserFactory loadFactory(String factoryName, ClassLoader cl) {
        return ParserFactoryProviders.loadFactory(factoryName, cl);
    }

    public static ParserFactory loadFactory(String factoryName) {
        return ParserFactoryProviders.loadFactory(factoryName, null);
    }

    public Parser createParser(Tokenizer tokenizer);

    public Parser createParser(Reader reader);
}
