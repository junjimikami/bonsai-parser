package com.jiganaut.bonsai.parser;

import java.io.Closeable;
import java.io.Reader;

import com.jiganaut.bonsai.grammar.Grammar;

/**
 * 
 * @author Junji Mikami
 */
public interface Parser extends Closeable {

    public static Parser of(Grammar grammar, Reader reader) {
        return ParserFactory.of(grammar).createParser(reader);
    }

    public static Parser of(Grammar grammar, Tokenizer tokenizer) {
        return ParserFactory.of(grammar).createParser(tokenizer);
    }

    public Tree parse();
}
