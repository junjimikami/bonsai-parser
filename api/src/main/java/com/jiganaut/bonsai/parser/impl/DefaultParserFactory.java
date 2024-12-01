package com.jiganaut.bonsai.parser.impl;

import java.io.Reader;
import java.util.Objects;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.impl.Message;
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
        Objects.requireNonNull(tokenizer, Message.NULL_PARAMETER.format());
        return new DefaultParser(grammar, tokenizer);
    }

    @Override
    public Parser createParser(Reader reader) {
        Objects.requireNonNull(reader, Message.NULL_PARAMETER.format());
        var tokenizer = new ReaderTokenizer(reader);
        return createParser(tokenizer);
    }

}
