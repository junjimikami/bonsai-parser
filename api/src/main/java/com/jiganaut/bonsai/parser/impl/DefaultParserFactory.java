package com.jiganaut.bonsai.parser.impl;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.Parser;
import com.jiganaut.bonsai.parser.ParserFactory;
import com.jiganaut.bonsai.parser.Tokenizer;

/**
 * @author Junji Mikami
 *
 */
class DefaultParserFactory implements ParserFactory {
    private final Grammar grammar;

    DefaultParserFactory(Grammar grammar) {
        assert grammar != null;
        this.grammar = grammar;
    }

    @Override
    public Parser createParser(Tokenizer tokenizer) {
        assert tokenizer != null;
        return new DefaultParser(grammar, tokenizer);
    }

    @Override
    public Parser createParser(Reader reader) {
        var tokenizer = new ReaderTokenizer(reader);
        return createParser(tokenizer);
    }

}
