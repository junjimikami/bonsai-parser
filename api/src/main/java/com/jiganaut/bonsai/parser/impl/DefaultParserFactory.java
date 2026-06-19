package com.jiganaut.bonsai.parser.impl;

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
class DefaultParserFactory<T> implements ParserFactory<T> {

    private final Grammar<T> grammar;

    DefaultParserFactory(Grammar<T> grammar) {
        assert grammar != null;
        this.grammar = grammar;
    }

    @Override
    public Parser<T> createParser(Tokenizer<T> tokenizer) {
        Objects.requireNonNull(tokenizer, () -> Message.VALIDATION_PARAMETER_NULL.format("tokenizer"));
        return new DefaultParser<>(grammar, tokenizer);
    }

}
