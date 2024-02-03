package com.unitedjiga.common.parsing;

import java.io.Reader;

import com.unitedjiga.common.parsing.grammar.Grammar;
import com.unitedjiga.common.parsing.impl.ParserService;

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
