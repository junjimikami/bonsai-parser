package com.jiganaut.bonsai.sample;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.Rules;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.TokenizerFactory;

public class WordTokenizerFactory implements TokenizerFactory {
    private static final Grammar GRAMMAR = Grammar.builder()
            .add("WORD", Rules.pattern("\\w").oneOrMore())
            .add("WORD", Rules.pattern("(?s)\\W.*"))
            .build();
    private static final TokenizerFactory FACTORY = TokenizerFactory.newFactory(GRAMMAR);;

    @Override
    public Tokenizer createTokenizer(Reader arg0) {
        return FACTORY.createTokenizer(arg0);
    }

    @Override
    public Tokenizer createTokenizer(Tokenizer arg0) {
        return FACTORY.createTokenizer(arg0);
    }

}
