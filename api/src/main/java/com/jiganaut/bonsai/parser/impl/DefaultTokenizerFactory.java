package com.jiganaut.bonsai.parser.impl;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.TokenizerFactory;

class DefaultTokenizerFactory implements TokenizerFactory {
    private final ProductionSet productionSet;

    DefaultTokenizerFactory(ProductionSet productionSet) {
        assert productionSet != null;
        this.productionSet = productionSet;
    }

    @Override
    public Tokenizer createTokenizer(Reader r) {
        var tokenizer = new ReaderTokenizer(r);
        return createTokenizer(tokenizer);
    }

    @Override
    public Tokenizer createTokenizer(Tokenizer tokenizer) {
        return new DefaultTokenizer(productionSet, tokenizer);
    }

}
