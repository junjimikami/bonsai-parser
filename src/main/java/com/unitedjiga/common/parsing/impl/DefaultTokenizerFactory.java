package com.unitedjiga.common.parsing.impl;

import java.io.Reader;

import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.TokenizerFactory;
import com.unitedjiga.common.parsing.grammar.Grammar;

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
