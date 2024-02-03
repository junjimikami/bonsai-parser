package com.jiganaut.bonsai.parser.impl;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.Grammar;
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
        var tokenizer = new ReaderTokenizer(r);
        return createTokenizer(tokenizer);
    }

    @Override
    public Tokenizer createTokenizer(Tokenizer tokenizer) {
        return new DefaultTokenizer(grammar, tokenizer);
    }

}
