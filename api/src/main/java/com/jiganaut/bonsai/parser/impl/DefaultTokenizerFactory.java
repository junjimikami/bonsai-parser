package com.jiganaut.bonsai.parser.impl;

import java.io.Reader;
import java.util.Objects;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.TokenizerFactory;

class DefaultTokenizerFactory implements TokenizerFactory {
    private final Grammar grammar;

    DefaultTokenizerFactory(Grammar grammar) {
        assert grammar != null;
        this.grammar = grammar;
    }

    @Override
    public Tokenizer createTokenizer(Reader r) {
        Objects.requireNonNull(r, Message.NULL_PARAMETER.format());
        var tokenizer = new ReaderTokenizer(r);
        return createTokenizer(tokenizer);
    }

    @Override
    public Tokenizer createTokenizer(Tokenizer tokenizer) {
        Objects.requireNonNull(tokenizer, Message.NULL_PARAMETER.format());
        return new DefaultTokenizer(grammar, tokenizer);
    }

}
