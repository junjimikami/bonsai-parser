package com.jiganaut.bonsai.parser.impl;

import java.util.stream.Collector;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.Source;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.TokenizerFactory;

/**
 *
 * @author Junji Mikami
 */
class DefaultSource<T> implements Source<T> {

    final Tokenizer<T> tokenizer;

    DefaultSource(Tokenizer<T> tokenizer) {
        assert tokenizer != null;
        this.tokenizer = tokenizer;
    }

    @Override
    public <R> Source<R> addLayer(Grammar<T> grammar, Collector<? super T, ?, R> collector) {
        var tokenizer = TokenizerFactory.of(grammar, collector).createTokenizer(this.tokenizer);
        return new DefaultSource<>(tokenizer);
    }

    @Override
    public Tokenizer<T> toTokenizer() {
        return tokenizer;
    }

}
