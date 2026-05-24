package com.jiganaut.bonsai.parser;

import java.util.stream.Collector;

import com.jiganaut.bonsai.grammar.Grammar;

/**
 *
 * @author Junji Mikami
 */
public interface Source {

    public Source addLayer(Grammar grammar, Collector<CharSequence, ?, String> collector);
    public Tokenizer toTokenizer();
    public default Parser toParser(Grammar grammar) {
        return ParserFactory.of(grammar).createParser(this);
    }

}
