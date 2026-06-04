package com.jiganaut.bonsai.parser;

import java.util.stream.Collector;

import com.jiganaut.bonsai.grammar.Grammar;

/**
 *
 * @author Junji Mikami
 */
public interface Source<T> {

    public <R> Source<R> addLayer(Grammar<T> grammar, Collector<? super T, ?, R> collector);

    public Tokenizer<T> toTokenizer();

    public default Parser<T> toParser(Grammar<T> grammar) {
        return ParserFactory.of(grammar).createParser(this);
    }

}
