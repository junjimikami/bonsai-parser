package com.jiganaut.bonsai.sample;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.grammar.Rules;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.TokenizerFactory;

public class CrLfTokenizerFactory implements TokenizerFactory {
    private static final ProductionSet GRAMMAR = ProductionSet.builder()
            .add("CRLF", Rules.concat(
                    Rules.pattern("\r"),
                    Rules.pattern("\n").opt()))
//            .add("CRLF", Rules.pattern("(?s)[^\r].*"))
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
