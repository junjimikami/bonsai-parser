package com.jiganaut.bonsai.parser.impl;

import java.util.Objects;
import java.util.stream.Collector;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.TokenizerFactory;

/**
 *
 * @author Junji Mikami
 */
class DefaultTokenizerFactory<T, R> implements TokenizerFactory<T, R> {

    private final Grammar<T> grammar;
    private final Collector<? super T, ?, R> collector;

    DefaultTokenizerFactory(Grammar<T> grammar, Collector<? super T, ?, R> collector) {
        assert grammar != null;
        assert collector != null;
        this.grammar = grammar;
        this.collector = collector;
    }

    @Override
    public Tokenizer<R> createTokenizer(Tokenizer<T> tokenizer) {
        Objects.requireNonNull(tokenizer, () -> Message.VALIDATION_PARAMETER_NULL.format("tokenizer"));
        return new DefaultTokenizer<>(grammar, tokenizer, collector);
    }

}
